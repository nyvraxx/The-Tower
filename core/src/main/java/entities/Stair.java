package entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.utils.ObjectSet;

import world.GameWorld;

public class Stair extends Platform {
	ObjectSet<Entity> inContact = new ObjectSet<>();
	ObjectSet<Entity> contained = new ObjectSet<>();

	Fixture base;
	Fixture top;

	protected final float hWidth, hHeight;

	public Stair(int level, float hWidth, float hHeight) {
		super(level, hWidth, hHeight);

		this.hWidth = hWidth;
		this.hHeight = hHeight;

		((PolygonShape) shape).setAsBox(hWidth, hHeight);

		levelTracker.onStairs = true;
		levelTracker.fraction = LevelTracker.Middle;
	}

	public void generateBarriers(GameWorld gameWorld, float epsilon) {
		Barrier bottomNorth = new Barrier(hWidth, epsilon);
		Barrier bottomSouth = new Barrier(hWidth, epsilon);
		Barrier bottomEast = new Barrier(epsilon, hHeight);

		bottomNorth.getLevelTracker().level = getLevelTracker().level;
		bottomSouth.getLevelTracker().level = getLevelTracker().level;
		bottomEast.getLevelTracker().level = getLevelTracker().level;

		Barrier topNorth = new Barrier(hWidth, epsilon);
		Barrier topSouth = new Barrier(hWidth, epsilon);
		Barrier topWest = new Barrier(epsilon, hHeight);

		topNorth.getLevelTracker().level = getLevelTracker().level + 1;
		topSouth.getLevelTracker().level = getLevelTracker().level + 1;
		topWest.getLevelTracker().level = getLevelTracker().level + 1;

		gameWorld.add(bottomNorth);
		gameWorld.add(bottomSouth);
		gameWorld.add(bottomEast);
		gameWorld.add(topNorth);
		gameWorld.add(topSouth);
		gameWorld.add(topWest);

		Vector2 pos = getBody().getPosition();
		float angle = getBody().getAngle();
		bottomNorth.getBody().setTransform(pos.x, pos.y + hHeight, angle);
		bottomSouth.getBody().setTransform(pos.x, pos.y - hHeight, angle);
		bottomEast.getBody().setTransform(pos.x + hWidth, pos.y, angle);

		topNorth.getBody().setTransform(pos.x, pos.y + hHeight, angle);
		topSouth.getBody().setTransform(pos.x, pos.y - hHeight, angle);
		topWest.getBody().setTransform(pos.x - hWidth, pos.y, angle);
	}

	private void beginContainment(Entity entity) {
	}

	private void endContainment(Entity entity) {
	}

	@Override
	public void beginContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {
		if (!(other instanceof Entity)) {
			return;
		}
		Entity entity = (Entity) other;

		entity.getLevelTracker().level = getLevelTracker().level;
		entity.getLevelTracker().fraction = getFraction(entity);

		for (Fixture fixture : entity.getBody().getFixtureList()) {
			fixture.refilter();
		}

		other.getLevelTracker().onStairs = true;
		inContact.add((Entity) other);
	}

	@Override
	public void endContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {
		if (!(other instanceof Entity)) {
			return;
		}

		Entity entity = (Entity) other;

		int dir = getDir(entity);

		if (dir == 1) {
			entity.getLevelTracker().level++;
			entity.getLevelTracker().fraction = 0;
		}

		for (Fixture fixture : entity.getBody().getFixtureList()) {
			fixture.refilter();
		}

		other.getLevelTracker().onStairs = false;
		inContact.remove((Entity) other);
	}

	// -1 for bottom, +1 for top
	private int getDir(Entity other) {
		return getFraction(other) < 0.5 ? -1 : 1;
	}

	private float getFraction(Entity other) {
		Transform thisTransform = getBody().getTransform();
		Vector2 position = thisTransform.getPosition();

		Vector2 localXAxis = thisTransform.getOrientation().nor();

		Vector2 relativePosition = other.getBody().getPosition().sub(position);

		float projection = relativePosition.dot(localXAxis);

		float fraction = (projection / (2 * hWidth)) + 0.5f;

		return MathUtils.clamp(fraction, 0, 1);
	}

	@Override
	public void update(float delta) {
		for (Entity entity : inContact) {
			boolean containsEntityCenter = sensor.testPoint(entity.getBody().getPosition());
			if (containsEntityCenter) {
				boolean added = contained.add(entity);

				if (added) {
					beginContainment(entity);
				}

				float pFraction = entity.getLevelTracker().fraction;
				float fraction = getFraction(entity);
				entity.getLevelTracker().fraction = fraction;

				float min = Math.min(pFraction, fraction);
				float max = pFraction + fraction - min;

				if (min <= 0.5 && 0.5f <= max) {
					for (Fixture fixture : entity.getBody().getFixtureList()) {
						fixture.refilter();
					}
				}
			} else {
				boolean removed = contained.remove(entity);

				if (removed) {
					endContainment(entity);
				}
			}
		}
	}
}

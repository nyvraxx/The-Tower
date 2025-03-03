package entities;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;

public class Stair extends Platform {
	private int level;
	Fixture base;
	Fixture top;

	private final float hWidth, hHeight;

	public Stair(int level, float hWidth, float hHeight) {
		super(level, new PolygonShape());

		this.hWidth = hWidth;
		this.hHeight = hHeight;

		this.level = level;

		((PolygonShape) shape).setAsBox(hWidth, hHeight);

		levelTracker.inTransition = true;
	}

	@Override
	public void beginContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {
		if (!(other instanceof Entity)) {
			return;
		}

		Entity entity = (Entity) other;

		entity.getLevelTracker().inTransition = true;
		entity.getLevelTracker().level = level;
	}

	@Override
	public void endContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {
		if (!(other instanceof Entity)) {
			return;
		}
		Entity entity = (Entity) other;

		int dir = getDir(fixtureOther);
		entity.getLevelTracker().inTransition = false;
		if (dir == 1) {
			entity.getLevelTracker().level++;
		}
	}

	// -1 for bottom, +1 for top
	// TODO
	private int getDir(Fixture other) {
		Transform transform = getBody().getTransform();
		float angle = transform.getRotation();

		Vector2 origin = transform.getPosition();
		Vector2 pos = other.getBody().getPosition();
		pos.x -= origin.x;
		pos.y -= origin.y;

		float angleToOther = MathUtils.atan2(pos.y, pos.x);

		float relAngle = (angle - angleToOther + MathUtils.PI) % MathUtils.PI2 - MathUtils.PI;

		return relAngle > 0 ? -1 : 1;
	}

	@Override
	public void initializeBody(Body body) {
		super.initializeBody(body);
	}
}

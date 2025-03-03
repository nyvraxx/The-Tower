package entities;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Stair extends Platform {
	Fixture base;
	Fixture top;

	public Stair(int level, float hWidth, float hHeight) {
		super(level, new PolygonShape());

		((PolygonShape) shape).setAsBox(hWidth, hHeight);

		levelTracker.inTransition = true;
	}

	@Override
	public void beginContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {
		if (!(other instanceof Entity)) {
			return;
		}

		Entity entity = (Entity) other;

	}

	@Override
	public void endContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {
		if (!(other instanceof Entity)) {
			return;
		}
		Entity entity = (Entity) other;

	}

	//-1 for bottom, +1 for top
	//TODO
	private int getDir(Fixture other) {
		float angle = this.getBody().getTransform().getRotation();
	}

}

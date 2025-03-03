package entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

public interface WorldObject {
	void render(Batch batch);

	Body getBody();

	BodyDef getBodyDef();

	LevelTracker getLevelTracker();

	void initializeBody(Body body);

	void beginContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other);

	void endContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other);

	public default boolean shouldCollide(WorldObject other) {
		return getLevelTracker().shouldCollide(other.getLevelTracker());
	}

	default void update(float delta) {
	}
}

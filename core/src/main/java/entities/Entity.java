package entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

public abstract class Entity implements WorldObject {
	LevelTracker levelTracker;
	private Body body;

	public Entity() {
		levelTracker = new LevelTracker();
	}

	@Override
	public abstract BodyDef getBodyDef();

	protected abstract void configureBody(Body body);

	public Body getBody() {
		return body;
	}

	@Override
	public LevelTracker getLevelTracker() {
		return levelTracker;
	}

	@Override
	public void initializeBody(Body body) {
		this.body = body;
		this.body.setUserData(this);

		configureBody(body);
	}

	public void update(float delta) {
	}

	public void beginContact(Entity other) {
	}

	@Override
	public void beginContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {
	}
	@Override
	public void endContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {
	}
}

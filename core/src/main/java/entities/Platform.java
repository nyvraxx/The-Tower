package entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

public class Platform implements WorldObject {
	LevelTracker levelTracker;

	private static BodyDef defaultBodyDef;
	static {
		defaultBodyDef = new BodyDef();

		defaultBodyDef.type = BodyType.StaticBody;
	}

	public Body getBody() {
		return body;
	}
	
	protected Shape shape;
	private Body body;

	@Override
	public void initializeBody(Body body) {
		this.body = body;
		this.body.setUserData(this);

		body.createFixture(createFixture());
	}

	private FixtureDef createFixture() {
		FixtureDef def = new FixtureDef();

		def.isSensor = true;
		def.shape = shape;
		return def;
	}

	@Override
	public BodyDef getBodyDef() {
		return defaultBodyDef;
	}

	public Platform(int level, Shape shape) {
		levelTracker = new LevelTracker(level);

		this.shape = shape;
	}

	@Override
	public LevelTracker getLevelTracker() {
		return levelTracker;
	}

	@Override
	public void beginContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {
	}

	@Override
	public void endContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {
	}

}

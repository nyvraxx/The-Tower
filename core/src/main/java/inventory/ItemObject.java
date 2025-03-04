package inventory;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

import entities.LevelTracker;
import entities.WorldObject;

public class ItemObject implements WorldObject {
	private static BodyDef defaultBodyDef;
	private static Shape defaultShape;
	private Body body;

	static {
		defaultBodyDef = new BodyDef();

		defaultBodyDef.linearDamping = 8f;
		defaultBodyDef.type = BodyType.StaticBody;
		defaultBodyDef.fixedRotation = true;

		defaultShape = new CircleShape();
		defaultShape.setRadius(0.3f);
	}

	private FixtureDef createFixtureDef() {
		FixtureDef fixtureDef = new FixtureDef();

		fixtureDef.isSensor = true;
		fixtureDef.shape = defaultShape;

		return fixtureDef;
	}

	final Item item;

	private LevelTracker levelTracker;

	public ItemObject(Item item) {
		this.item = item;
		levelTracker = new LevelTracker();
	}

	@Override
	public void render(Batch batch) {
	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public BodyDef getBodyDef() {
		return defaultBodyDef;
	}

	@Override
	public LevelTracker getLevelTracker() {
		return levelTracker;
	}

	@Override
	public void initializeBody(Body body) {
		body.setUserData(this);
		this.body = body;

		body.createFixture(createFixtureDef());
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void beginContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {
	}

	@Override
	public void endContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {
	}

}

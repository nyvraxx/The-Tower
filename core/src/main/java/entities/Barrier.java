package entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;

import util.ImageUtils;

public class Barrier implements WorldObject {
	private Sprite sprite = new Sprite(ImageUtils.OhNoTexture);
	private Body body;
	private LevelTracker levelTracker = new LevelTracker();
	
	private static BodyDef defaultBodyDef;

	float hWidth, hHeight;

	public Barrier(float hWidth, float hHeight) {
		this.hWidth = hWidth;
		this.hHeight = hHeight;
	}

	static {
		defaultBodyDef = new BodyDef();

		defaultBodyDef.type = BodyDef.BodyType.StaticBody;
		defaultBodyDef.allowSleep = true;
		defaultBodyDef.active = true;
		defaultBodyDef.fixedRotation = true;
	}

	@Override
	public BodyDef getBodyDef() {
		return defaultBodyDef;
	}

	private FixtureDef createFixtureDef() {
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.friction = 0.1f;
		fixtureDef.restitution = 0f;
		fixtureDef.shape = new PolygonShape();
		((PolygonShape) fixtureDef.shape).setAsBox(hWidth, hHeight);

		return fixtureDef;
	}

	@Override
	public void render(Batch batch) {
		Transform transform = body.getTransform();

		Vector2 pos = transform.getPosition();

		sprite.setTexture(ImageUtils.OhNoTexture);
		sprite.setOriginCenter();
		sprite.setPosition(pos.x - hWidth, pos.y - hHeight);
		sprite.setSize(2 * hWidth, 2 * hHeight);
		sprite.setRotation(MathUtils.radiansToDegrees * transform.getRotation());
		
		sprite.draw(batch);
	}

	@Override
	public LevelTracker getLevelTracker() {
		return levelTracker;
	}

	@Override
	public void initializeBody(Body body) {
		body.createFixture(createFixtureDef());
		
		body.setUserData(this);
		this.body = body;
	}

	@Override
	public void beginContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {	
	}

	@Override
	public void endContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {		
	}

	@Override
	public Body getBody() {
		return body;
	}
}

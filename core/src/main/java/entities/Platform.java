package entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Transform;

import util.ImageUtils;

public class Platform implements WorldObject {
	LevelTracker levelTracker;
	private Sprite sprite = new Sprite();
	private static BodyDef defaultBodyDef;
	
	private final float hWidth, hHeight;
	
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

	public Platform(int level, float hWidth, float hHeight) {
		levelTracker = new LevelTracker(level);
		
		this.hWidth = hWidth;
		this.hHeight = hHeight;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(hWidth, hHeight);
		
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

	public void render(Batch batch) {
		Transform transform = getBody().getTransform();

		Vector2 pos = transform.getPosition();

		sprite.setTexture(ImageUtils.OhNoTexture);
		sprite.setOriginCenter();
		sprite.setPosition(pos.x - hWidth, pos.y - hHeight);
		sprite.setSize(2 * hWidth, 2 * hHeight);
		sprite.setRotation(MathUtils.radiansToDegrees * transform.getRotation());
		
		sprite.draw(batch);
	}

}

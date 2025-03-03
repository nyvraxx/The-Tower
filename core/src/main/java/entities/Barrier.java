package entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;

import util.ImageUtils;

public class Barrier extends Entity {
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

	@Override
	public void configureBody(Body body) {
		body.createFixture(createFixtureDef());
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
	public void updateSprite(Sprite sprite) {
		Transform transform = getBody().getTransform();

		Vector2 pos = transform.getPosition();

		sprite.setTexture(ImageUtils.OhNoTexture);
		sprite.setOriginCenter();
		sprite.setPosition(pos.x - hWidth, pos.y - hHeight);
		sprite.setSize(2 * hWidth, 2 * hHeight);
		sprite.setRotation(MathUtils.radiansToDegrees * transform.getRotation());
	}
}

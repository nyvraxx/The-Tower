package entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Barrier extends Entity {
	private static BodyDef defaultBodyDef;

	float width, height;

	public Barrier(float width, float height) {
		this.width = width;
		this.height = height;
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
		((PolygonShape) fixtureDef.shape).setAsBox(width, height);

		return fixtureDef;
	}
}

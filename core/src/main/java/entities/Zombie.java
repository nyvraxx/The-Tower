package entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Zombie extends Entity {
	public Zombie() {
		super();
	}

	@Override
	public BodyDef getBodyDef() {
		return defaultBodyDef;
	}

	@Override
	public void configureBody(Body body) {
		body.createFixture(defaultFixtureDef);
	}

	
	private static BodyDef defaultBodyDef;
	private static FixtureDef defaultFixtureDef;

	static {
		defaultBodyDef = new BodyDef();

		defaultBodyDef.type = BodyDef.BodyType.DynamicBody;
		defaultBodyDef.allowSleep = false;
		defaultBodyDef.active = true;
		defaultBodyDef.fixedRotation = true;
		defaultBodyDef.linearDamping = 8f;

		defaultFixtureDef = new FixtureDef();
		defaultFixtureDef.friction = 0.1f;
		defaultFixtureDef.restitution = 0f;
		defaultFixtureDef.shape = new CircleShape();
		defaultFixtureDef.shape.setRadius(0.4f);
		defaultFixtureDef.density = 0.05f;
	}

}

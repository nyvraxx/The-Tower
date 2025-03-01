package entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import items.Inventory;

public class Player extends Entity {
	private float walkAcceleration;
	private float runAcceleration;
	private float staminaChargeRate;

	Inventory inventory;

	boolean running = false;
	private static BodyDef defaultBodyDef;
	private static FixtureDef defaultFixtureDef;

	public float maxStamina;
	public float stamina;

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

	public Player() {
		super();
		
		inventory = new Inventory();

		walkAcceleration = 1f;
		runAcceleration = 2.1f;
		staminaChargeRate = 0.3f;

		maxStamina = 7f;
		stamina = maxStamina;
	}

	public void startRunning() {
		running = true;
	}

	public void stopRunning() {
		running = false;
	}

	@Override
	public void update(float delta) {
		if (!running) {
			stamina = Math.min(maxStamina, stamina + delta * staminaChargeRate);
		}
	}

	public void move(float dx, float dy, float delta) {
		if (dx == 0 && dy == 0)
			return;

		if (running && stamina >= 0) {
			getBody().applyForceToCenter(dx * runAcceleration, dy * runAcceleration, false);
			stamina -= delta;
		} else {
			running = false;
			getBody().applyForceToCenter(dx * walkAcceleration, dy * walkAcceleration, false);
		}
	}

	@Override
	public BodyDef getBodyDef() {
		return defaultBodyDef;
	}

	@Override
	public void configureBody(Body body) {
		body.createFixture(defaultFixtureDef);
	}

}

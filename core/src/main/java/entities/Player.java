package entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Player {
	private float walkSpeed;
	private float runSpeed;
	private float staminaChargeRate;

	boolean running = false;
	private static BodyDef defaultBodyDef;
	private static FixtureDef defaultFixtureDef;

	public float maxStamina;
	public float stamina;
	Body body;

	static {
		defaultBodyDef = new BodyDef();

		defaultBodyDef.type = BodyDef.BodyType.DynamicBody;
		defaultBodyDef.allowSleep = false;
		defaultBodyDef.active = true;
		defaultBodyDef.fixedRotation = true;
		defaultBodyDef.linearDamping = 8f;

		defaultFixtureDef = new FixtureDef();
		defaultFixtureDef.friction = 1f;
		defaultFixtureDef.restitution = 0f;
		defaultFixtureDef.shape = new CircleShape();
		defaultFixtureDef.shape.setRadius(1f);
		defaultFixtureDef.density = 0.013f;
	}

	public Body getBody() {
		return body;
	}

	public Player() {
		walkSpeed = 1f;
		runSpeed = 2.1f;
		staminaChargeRate = 0.7f;

		maxStamina = 7f;
		stamina = maxStamina;
	}
	
	public void startRunning() {
		running = true;
	}
	public void stopRunning() {
		running = false;
	}

	public void update(float delta) {
		if (!running) {
			stamina = Math.min(maxStamina, stamina + delta * staminaChargeRate);
		}
	}

	public void move(float dx, float dy, float delta) {
		if (dx == 0 && dy == 0)
			return;

		if (running && stamina >= 0) {
			body.applyForceToCenter(dx * runSpeed, dy * runSpeed, false);
			stamina -= delta;
		} else {
			running = false;
			body.applyForceToCenter(dx * walkSpeed, dy * walkSpeed, false);
		}
	}

	public void addToWorld(World world, float x, float y) {
		body = world.createBody(defaultBodyDef);
		body.createFixture(defaultFixtureDef);
	}
}

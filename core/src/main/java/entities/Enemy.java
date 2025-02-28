package entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.nyvraxx.apcsagameproject.GameManager;

public class Enemy {
	private static BodyDef defaultBodyDef;
	private static FixtureDef defaultFixtureDef;
	static {
		defaultBodyDef = new BodyDef();

		defaultBodyDef.type = BodyDef.BodyType.DynamicBody;
		defaultBodyDef.allowSleep = false;
		defaultBodyDef.active = true;
		defaultBodyDef.fixedRotation = true;
		defaultBodyDef.angularDamping = 4f;
		defaultBodyDef.linearDamping = 8f;

		defaultFixtureDef = new FixtureDef();
		defaultFixtureDef.friction = 0.1f;
		defaultFixtureDef.restitution = 0f;
		defaultFixtureDef.shape = new CircleShape();
		defaultFixtureDef.shape.setRadius(0.4f);
		defaultFixtureDef.density = 0.05f;
	}

	GameManager gameManager;
	World world;
	private float acceleration;
	Body body;

	public Body getBody() {
		return body;
	}

	public Enemy(GameManager gameManager) {
		acceleration = 0.4f;
		this.gameManager = gameManager;
	}

	public void update(float delta) {
		// TODO implement more sophisticated pathfinding here
		Vector2 thisPos = body.getTransform().getPosition();
		Vector2 playerPos = gameManager.getPlayer().getBody().getTransform().getPosition();
		float dx = -thisPos.x + playerPos.x;
		float dy = -thisPos.y + playerPos.y;
		float epsilon = 0.001f;
		float invMag = (float) (1.0 / Math.sqrt(dx * dx + dy * dy + epsilon));
		dx *= invMag;
		dy *= invMag;
		body.applyForceToCenter(dx * acceleration, dy * acceleration, true);
	}

	public void addToWorld(World world, float x, float y) {
		body = world.createBody(defaultBodyDef);
		body.createFixture(defaultFixtureDef);
	}

}

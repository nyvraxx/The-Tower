package world;

import java.util.HashMap;
import java.util.function.Consumer;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

import entities.Entity;
import math.MutableBoolean;

public class Platform {
	private PlatformListener platformListener = PlatformListener.empty();

	public void setPlatformListener(PlatformListener platformListener) {
		this.platformListener = platformListener;
	}

	HashMap<Entity, MutableBoolean> contacts = new HashMap<>();

	public void addContact(Entity entity) {
		contacts.put(entity, new MutableBoolean(testEntity(entity)));
	}

	public void removeContact(Entity entity) {
		contacts.remove(entity);
	}

	public void updateContacts() {
		contacts.forEach((entity, contains) -> {
			boolean old = contains.val;
			contains.val = testEntity(entity);

			if (old == true && contains.val == false) {
				platformListener.endContact(entity);
			}
		});
	}

	public void forEachContact(Consumer<Entity> consumer) {
		contacts.forEach((entity, contact) -> {
			if (contact.val) {
				consumer.accept(entity);
			}
		});
	}

	private boolean testEntity(Entity entity) {
		if (entity.getLevel() != getLevel()) {
			return false;
		}

		for (Fixture fixture : body.getFixtureList()) {
			if (fixture.testPoint(entity.getBody().getTransform().getPosition())) {
				return true;
			}
		}

		return false;
	}

	private Shape shape;
	private Body body;

	final int level;

	public Platform(int level, Shape shape) {
		this.level = level;
		this.shape = shape;
	}

	public int getLevel() {
		return level;
	}

	public boolean contains(float x, float y) {
		return false;
	}

	public BodyDef getBodyDef() {
		BodyDef bodyDef = new BodyDef();

		return bodyDef;
	}

	public Body getBody() {
		return body;
	}

	private void setBody(Body body) {
		body.setUserData(this);

		this.body = body;
	}

	public void initializeBody(Body body) {
		setBody(body);

		body.createFixture(createFixtureDef());
		body.setType(BodyType.StaticBody);
	}

	private FixtureDef createFixtureDef() {
		FixtureDef fixtureDef = new FixtureDef();

		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;

		return fixtureDef;
	}

}

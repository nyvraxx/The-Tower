package entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

public abstract class Entity {
	int level = Integer.MIN_VALUE;
	private Body body;

	public Entity() {
	}

	public abstract BodyDef getBodyDef();

	public abstract void configureBody(Body body);

	public Body getBody() {
		return body;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void setBody(Body body) {
		body.setUserData(this);
		
		this.body = body;
	}

	public void update(float delta) {
	}

	public void beginContact(Entity other) {
	}

}

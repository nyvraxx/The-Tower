package entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

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
		
		if (body != null) {
			for (Fixture fixture : body.getFixtureList()) {
				fixture.refilter();
			}
		}
	}

	public void initializeBody(Body body) {
		this.body = body;
		this.body.setUserData(this);

		configureBody(body);
	}

	public void update(float delta) {
	}

	public void beginContact(Entity other) {
	}

}

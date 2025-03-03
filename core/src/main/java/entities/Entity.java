package entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

import util.ImageUtils;

public abstract class Entity implements WorldObject {
	LevelTracker levelTracker;
	private Body body;
	private Sprite sprite = new Sprite(ImageUtils.OhNoTexture);

	public Entity() {
		levelTracker = new LevelTracker();
		
		sprite.setSize(1f, 1f);
	}

	@Override
	public abstract BodyDef getBodyDef();

	protected abstract void configureBody(Body body);

	public Body getBody() {
		return body;
	}

	@Override
	public LevelTracker getLevelTracker() {
		return levelTracker;
	}

	@Override
	public void initializeBody(Body body) {
		this.body = body;
		this.body.setUserData(this);

		configureBody(body);
	}

	public void update(float delta) {
	}

	@Override
	public void beginContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {
	}

	@Override
	public void endContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other) {
	}

	public boolean interactable() {
		return false;
	}

	public abstract void updateSprite(Sprite sprite);
	
	public void render(Batch batch) {
		updateSprite(sprite);
		
		sprite.draw(batch);
	}
}

package world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import entities.Entity;

public class GameWorld {
	Array<Entity> entities;

	private ContactFilter contactFilter = new ContactFilter() {
		@Override
		public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
			Object userDataA = fixtureA.getBody().getUserData();
			Object userDataB = fixtureB.getBody().getUserData();

			Entity entityA = (Entity) userDataA;
			Entity entityB = (Entity) userDataB;

			if (entityA.getLevel() == entityB.getLevel())
				return true;

			return false;
		}
	};
	private ContactListener contactListener = new ContactListener() {

		@Override
		public void beginContact(Contact contact) {
			Object userDataA = contact.getFixtureA().getBody().getUserData();
			Object userDataB = contact.getFixtureB().getBody().getUserData();

			Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
			Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();

			entityA.beginContact(entityB);
			entityB.beginContact(entityA);
		}

		@Override
		public void endContact(Contact contact) {
		}

		@Override
		public void preSolve(Contact contact, Manifold oldManifold) {

		}

		@Override
		public void postSolve(Contact contact, ContactImpulse impulse) {

		}
	};

	World world;

	GameWorld() {
		entities = new Array<>();

		world = new World(Vector2.Zero, true);

		world.setContactListener(contactListener);
		world.setContactFilter(contactFilter);
	}

	public void update(float delta) {
		for (Entity entity : entities) {
			entity.update(delta);
		}
		world.step(delta, 8, 2);
	}

	public void add(Entity entity) {
		entities.add(entity);

		Body body = world.createBody(entity.getBodyDef());

		entity.initializeBody(body);
	}

	public World getWorld() {
		return world;
	}

	public Array<Entity> getEntities() {
		return entities;
	}

}

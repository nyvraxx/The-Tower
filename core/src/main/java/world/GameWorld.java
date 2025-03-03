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
import entities.Platform;
import entities.WorldObject;

public class GameWorld {
	Array<Platform> platforms;
	Array<Entity> entities;

	private ContactFilter contactFilter = new ContactFilter() {
		@Override
		public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
			Object userDataA = fixtureA.getBody().getUserData();
			Object userDataB = fixtureB.getBody().getUserData();

			WorldObject worldObjectA = (WorldObject) userDataA;
			WorldObject worldObjectB = (WorldObject) userDataB;

			return worldObjectA.shouldCollide(worldObjectB);
		}
	};
	private ContactListener contactListener = new ContactListener() {

		@Override
		public void beginContact(Contact contact) {
			Object userDataA = contact.getFixtureA().getBody().getUserData();
			Object userDataB = contact.getFixtureB().getBody().getUserData();

			WorldObject worldObjectA = (WorldObject) userDataA;
			WorldObject worldObjectB = (WorldObject) userDataB;

			worldObjectA.beginContact(contact.getFixtureA(), contact.getFixtureB(), worldObjectB);
			worldObjectB.beginContact(contact.getFixtureB(), contact.getFixtureA(), worldObjectA);
		}

		@Override
		public void endContact(Contact contact) {
			Object userDataA = contact.getFixtureA().getBody().getUserData();
			Object userDataB = contact.getFixtureB().getBody().getUserData();

			WorldObject worldObjectA = (WorldObject) userDataA;
			WorldObject worldObjectB = (WorldObject) userDataB;

			worldObjectA.endContact(contact.getFixtureA(), contact.getFixtureB(), worldObjectB);
			worldObjectB.endContact(contact.getFixtureB(), contact.getFixtureA(), worldObjectA);
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
		platforms = new Array<>();
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

	public void add(Platform platform) {
		platforms.add(platform);

		Body body = world.createBody(platform.getBodyDef());
		platform.initializeBody(body);
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

	public Array<Platform> getPlatforms() {
		return platforms;
	}

}

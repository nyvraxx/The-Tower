package world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import entities.Entity;

public class GameWorld {
	Array<Platform> platforms = new Array<>();
	Array<Entity> entities;

	private ContactFilter contactFilter = new ContactFilter() {
		@Override
		public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
			Object userDataA = fixtureA.getBody().getUserData();
			Object userDataB = fixtureB.getBody().getUserData();

			if (userDataA instanceof Platform || userDataB instanceof Platform)
				return true;

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

			if (userDataA instanceof Platform || userDataB instanceof Platform) {
				if (userDataA instanceof Platform ^ userDataB instanceof Platform) {
					Platform platform = getPlatform(userDataA, userDataB);
					Entity entity = getEntity(userDataA, userDataB);

					platform.addContact(entity);
				}
				return;// do nothing else
			}

			Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
			Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();

			entityA.beginContact(entityB);
			entityB.beginContact(entityA);
		}

		private Entity getEntity(Object userDataA, Object userDataB) {
			if (userDataA instanceof Entity)
				return (Entity) userDataA;
			else if (userDataB instanceof Entity)
				return (Entity) userDataB;

			return null;
		}

		private Platform getPlatform(Object userDataA, Object userDataB) {
			if (userDataA instanceof Platform)
				return (Platform) userDataA;
			else if (userDataB instanceof Platform)
				return (Platform) userDataB;

			return null;
		}

		@Override
		public void endContact(Contact contact) {
			Object userDataA = contact.getFixtureA().getBody().getUserData();
			Object userDataB = contact.getFixtureB().getBody().getUserData();

			if (userDataA instanceof Platform || userDataB instanceof Platform) {
				if (userDataA instanceof Platform ^ userDataB instanceof Platform) {
					Platform platform = getPlatform(userDataA, userDataB);
					Entity entity = getEntity(userDataA, userDataB);

					platform.removeContact(entity);
				}
				return;// do nothing else
			}
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

		addBaseplate();
	}

	private void addBaseplate() {
		PolygonShape baseplateShape = new PolygonShape();
		baseplateShape.setAsBox(10f, 10f);
		Platform baseplate = new Platform(0, baseplateShape);
		add(baseplate);
	}

	public void update(float delta) {
		for (Entity entity : entities) {
			entity.update(delta);
		}
		world.step(delta, 8, 2);
		for (Platform platform : platforms) {
			platform.updateContacts();
		}
	}

	public void add(Entity entity) {
		entities.add(entity);

		Body body = world.createBody(entity.getBodyDef());

		entity.initializeBody(body);
	}

	public World getWorld() {
		return world;
	}

	public void add(Platform platform) {
		platforms.add(platform);

		Body body = world.createBody(platform.getBodyDef());

		platform.initializeBody(body);
		platform.setPlatformListener(new PlatformListener() {

			@Override
			public void endContact(Entity entity) {
				GameWorld.this.endContact(platform, entity);
			}
		});
	}

	private void endContact(Platform platform, Entity entity) {
		Vector2 pos = entity.getBody().getTransform().getPosition();
		int level = platform.getLevel();

		PlatformFinderQueryCallback query = new PlatformFinderQueryCallback(level, pos.x, pos.y);

		float r = 0.001f;
		world.QueryAABB(query, pos.x - r, pos.y - r, pos.x + r, pos.y + r);

		entity.setLevel(query.max);
	}

	private class PlatformFinderQueryCallback implements QueryCallback {
		private int max = 0;
		final int level;
		final float x, y;

		PlatformFinderQueryCallback(int level, float x, float y) {
			this.level = level;
			this.x = x;
			this.y = y;
		}

		@Override
		public boolean reportFixture(Fixture fixture) {
			if (!fixture.testPoint(x, y)) {
				return true;
			}

			Object userData = fixture.getBody().getUserData();

			if (userData instanceof Platform) {
				Platform platform = (Platform) userData;

				if (platform.level > level) {
					return true;
				}

				if (platform.level > max) {
					max = platform.level;
				}
			}
			return true;
		}
	}

	public Array<Entity> getEntities() {
		return entities;
	}

}

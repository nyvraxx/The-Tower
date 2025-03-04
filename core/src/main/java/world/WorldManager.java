package world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.viewport.FitViewport;

import entities.Barrier;
import entities.Entity;
import entities.Platform;
import entities.Player;
import entities.Stair;
import entities.WorldObject;
import entities.Zombie;
import util.GeometryUtils;

public class WorldManager {
	Stage stage;

	ObjectSet<Entity> visibleEntities;
	Array<Vector2> visionPolygon;

	Player player;
	GameWorld gameWorld;
	OrthographicCamera camera;
	FitViewport viewport;

	private float viewWidth = 16f * 0.4f, viewHeight = 10f * 0.4f;

	public WorldManager() {
		visibleEntities = new ObjectSet<>();
		visionPolygon = new Array<>();

		gameWorld = new GameWorld();
		camera = new OrthographicCamera();
		camera.position.set(0, 0, 0);
		viewport = new FitViewport(viewWidth, viewHeight, camera);
		stage = new Stage(getViewport());

		player = new Player();
		player.getLevelTracker().level = 0;
		gameWorld.add(player);

		// TODO debug code
		for (int i = 0; i < 200; i++) {
			Entity entity = new Zombie();
			entity.getLevelTracker().level = 0;
			gameWorld.add(entity);
			entity.getBody().setTransform(MathUtils.random(2), MathUtils.random(2), MathUtils.random(MathUtils.PI));
		}
		{
			Barrier barrier = new Barrier(5f, 0.1f);
			barrier.getLevelTracker().level = 1;
			gameWorld.add(barrier);
			barrier.getBody().setTransform(-3, -3f, 0);
		}
		{
			Barrier barrier = new Barrier(0.1f, 4f);
			barrier.getLevelTracker().level = 0;
			gameWorld.add(barrier);
			barrier.getBody().setTransform(3, 3f, 0);
		}
		{
			Platform platform = new Stair(0, 1f, 3f);
			gameWorld.add(platform);
			platform.getBody().setTransform(-1, 1, 2.3f);
		}
		{
			Platform platform = new Stair(1, 1f, 3f);
			gameWorld.add(platform);
			platform.getBody().setTransform(3, 1, 0);
		}
	}

	public void update(float delta) {
		gameWorld.update(delta);
		updateRaycasts();

		Vector2 playerPos = player.getBody().getPosition();
		camera.position.lerp(new Vector3(playerPos.x, playerPos.y, 0), 18f * delta);
		camera.update();
	}

	public void handleInput(float delta) {
		float dx = 0;
		float dy = 0;

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			dy++;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			dy--;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			dx--;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			dx++;
		}

		if (dx == 0 && dy == 0)
			return;

		float invMag = 1.0f / (float) Math.sqrt(dx * dx + dy * dy);
		dx *= invMag;
		dy *= invMag;

		if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
			player.startRunning();
		} else if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			player.stopRunning();
		}

		player.move(dx, dy, delta);
	}

	public Camera getCamera() {
		return camera;
	}

	public GameWorld getGameWorld() {
		return gameWorld;
	}

	public Stage getStage() {
		return stage;
	}

	public FitViewport getViewport() {
		return viewport;
	}

	public Player getPlayer() {
		return player;
	}

	public boolean isVisible(WorldObject worldObject) {
		if (!worldObject.shouldCollide(player)) {
			return false;
		}

		if (worldObject instanceof Entity) {
			return visibleEntities.contains((Entity) worldObject);
		} else {
			return true;
		}
	}

	private void raycast(RayCastFirstObject rayCastFirstObject, float x, float y, float toX, float toY, float r) {
		float normX = toX - x;
		float normY = toY - y;

		float invMag = 1.0f / (float) Math.sqrt(normX * normX + normY * normY);
		normX *= invMag;
		normY *= invMag;

		rayCastFirstObject.found = false;
		gameWorld.world.rayCast(rayCastFirstObject, x, y, x + r * normX, y + r * normY);

		if (rayCastFirstObject.found) {
			if (rayCastFirstObject.first instanceof Entity) {
				visibleEntities.add((Entity) rayCastFirstObject.first);
			}
			visionPolygon.add(new Vector2(rayCastFirstObject.x, rayCastFirstObject.y));
		} else {
			visionPolygon.add(new Vector2(x + r * normX, y + r * normY));
		}
	}

	private void updateRaycasts() {
		visionPolygon.clear();
		visibleEntities.clear();

		Vector2 playerPos = player.getBody().getPosition();
		float x = playerPos.x;
		float y = playerPos.y;
		float r = 10f;

		for (WorldObject worldObject : gameWorld.worldObjects) {
			if (worldObject == player || !worldObject.shouldCollide(player)) {
				continue;
			}

			RayCastFirstObject rayCastFirstObject = new RayCastFirstObject(player);

			final float epsilon = 0.01f;
			GeometryUtils.getVertices(worldObject.getBody(), vec -> {
				raycast(rayCastFirstObject, x, y, vec.x, vec.y, r);
				raycast(rayCastFirstObject, x, y, vec.x + epsilon, vec.y, r);
				raycast(rayCastFirstObject, x, y, vec.x - epsilon, vec.y, r);
				raycast(rayCastFirstObject, x, y, vec.x, vec.y + epsilon, r);
				raycast(rayCastFirstObject, x, y, vec.x, vec.y - epsilon, r);

			});
		}
		int numFree = 50;
		for (int i = 0; i < numFree; i++) {
			RayCastFirstObject rayCastFirstObject = new RayCastFirstObject(player);

			float angle = i * MathUtils.PI2 / numFree;
			float normX = MathUtils.cos(angle);
			float normY = MathUtils.sin(angle);

			raycast(rayCastFirstObject, x, y, x + normX, y + normY, r);
		}

		visionPolygon.sort((a, b) -> {
			return Float.compare(a.cpy().sub(playerPos).angleRad(), b.cpy().sub(playerPos).angleRad());
		});
	}

	private static class RayCastFirstObject implements RayCastCallback {
		float x, y;
		boolean found = false;
		WorldObject first;
		WorldObject origin;

		RayCastFirstObject(WorldObject origin) {
			this.origin = origin;
		}

		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			WorldObject other = (WorldObject) fixture.getBody().getUserData();
			if (other instanceof Platform) {
				return -1;
			}
			if (!origin.shouldCollide(other)) {
				return -1;
			}

			found = true;

			first = (WorldObject) fixture.getBody().getUserData();
			x = point.x;
			y = point.y;
			return fraction;
		}
	}

	public float[] getVisionPolygon() {
		float[] points = new float[visionPolygon.size * 2];

		for (int i = 0; i < visionPolygon.size; i++) {
			points[2 * i] = visionPolygon.get(i).x;
			points[2 * i + 1] = visionPolygon.get(i).y;
		}
		return points;
	}
}

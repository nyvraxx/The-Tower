package world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import entities.Barrier;
import entities.Couch;
import entities.Entity;
import entities.LevelTracker;
import entities.Player;
import entities.Stair;
import entities.WorldObject;
import entities.Zombie;
import util.GeometryUtils;

public class WorldManager {
	Stage stage;

	VectorArray visionPolygon;

	private Raycaster raycaster;

	private ViewingFrustrum viewingFrustrum;
	Player player;
	GameWorld gameWorld;
	OrthographicCamera camera;
	FitViewport viewport;
private Couch couch;
	private float viewWidth = 16f * 0.45f, viewHeight = 10f * 0.45f;

	public WorldManager() {
		viewingFrustrum = new ViewingFrustrum();

		visionPolygon = new VectorArray();

		gameWorld = new GameWorld();
		camera = new OrthographicCamera();
		camera.position.set(0, 0, 0);
		viewport = new FitViewport(viewWidth, viewHeight, camera);
		stage = new Stage(getViewport());

		player = new Player();
		player.getLevelTracker().level = 0;
		gameWorld.add(player);

		raycaster = new Raycaster(gameWorld.world);

		// TODO debug code
		for (int i = 0; i < 10; i++) {
			Entity entity = new Zombie();
			entity.getLevelTracker().level = 0;
			gameWorld.add(entity);
			entity.getBody().setTransform(MathUtils.random(2), MathUtils.random(2), MathUtils.random(MathUtils.PI));
		}
		for (int i = 0; i < 10; i++) {
			Entity entity = new Zombie();
			entity.getLevelTracker().level = 1;
			gameWorld.add(entity);
			entity.getBody().setTransform(MathUtils.random(2), MathUtils.random(2), MathUtils.random(MathUtils.PI));
		}
		for (int i = 0; i < 1; i++) {
			Entity entity = new Couch(0.3f, 0.6f);
			couch= (Couch) entity;
			entity.getLevelTracker().level = 0;
			gameWorld.add(entity);
			entity.getBody().setTransform(MathUtils.random(2), MathUtils.random(2), MathUtils.random(MathUtils.PI));
		}
		{
			Barrier barrier = new Barrier(5f, 0.1f);
			barrier.getLevelTracker().level = 0;
			gameWorld.add(barrier);
			barrier.getBody().setTransform(-3, -1.4f, 0);
		}
		{
			Barrier barrier = new Barrier(0.1f, 1f);
			barrier.getLevelTracker().level = 0;
			gameWorld.add(barrier);
			barrier.getBody().setTransform(0.4f, 3, 0.3f);

		}
		{
			Stair platform = new Stair(0, 0.8f, 1f);
			gameWorld.add(platform);
			platform.getBody().setTransform(-1, 1, 0f);
			platform.generateBarriers(gameWorld, 0.01f);
		}
		{
			Barrier barrier = new Barrier(1f, .01f);
			barrier.getLevelTracker().level = 2;
			gameWorld.add(barrier);
			barrier.getBody().setTransform(3f, -0.5f, 0f);

		}
		{
			Barrier barrier = new Barrier(1f, .01f);
			barrier.getLevelTracker().level = 1;
			gameWorld.add(barrier);
			barrier.getBody().setTransform(3f, 2.5f, 0f);

		}
		{
			Stair platform = new Stair(1, 0.5f, 1f);
			gameWorld.add(platform);
//			platform.generateBarriers(gameWorld, 0.1f);
			platform.getBody().setTransform(3, 1, 0);
		}
	}

	public void update(float delta) {
		System.out.println(couch.getLevelTracker());
		gameWorld.update(delta);

		Vector2 playerPos = player.getBody().getPosition();
		camera.position.lerp(new Vector3(playerPos.x, playerPos.y, 0), 18f * delta);
		camera.update();
		viewingFrustrum.update(camera);

		updateVisible();
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
		if (!player.getLevelTracker().isVisible(worldObject.getLevelTracker())) {
			return false;
		}

		if (worldObject.alwaysVisible()) {
			return true;
		} else {
			return true;
		}
	}

	private void updateVisible() {
		visionPolygon.clear();

		Vector2 playerPos = player.getBody().getPosition();
		LevelTracker rayLevelTracker = player.getLevelTracker();

		float x = playerPos.x;
		float y = playerPos.y;
		float r = 10f;

		final float epsilonOffset = 0.001f;
		viewingFrustrum.updateVisible(gameWorld.world);

		for (WorldObject visible : viewingFrustrum.getVisible()) {
			if (!visible.blocksVision()) {
				continue;
			}
			if (!player.getLevelTracker().isVisible(visible.getLevelTracker())) {
				continue;
			}

			GeometryUtils.getVertices(visible.getBody(), vec -> {
				visionPolygon.add(raycaster.shootRay(rayLevelTracker, x, y, vec.x, vec.y, r));

				Vector2 diff = new Vector2(vec.x - x, vec.y - y);

				diff.rotateRad(epsilonOffset);
				visionPolygon.add(raycaster.shootRay(rayLevelTracker, x, y, x + diff.x, y + diff.y, r));
				diff.rotateRad(epsilonOffset);
				visionPolygon.add(raycaster.shootRay(rayLevelTracker, x, y, x + diff.x, y + diff.y, r));

				diff.set(vec.x - x, vec.y - y);
				diff.rotateRad(-epsilonOffset);
				visionPolygon.add(raycaster.shootRay(rayLevelTracker, x, y, x + diff.x, y + diff.y, r));
				diff.rotateRad(-epsilonOffset);
				visionPolygon.add(raycaster.shootRay(rayLevelTracker, x, y, x + diff.x, y + diff.y, r));
			});
		}

		int numFree = 16;
		for (int i = 0; i < numFree; i++) {
			float angle = i * MathUtils.PI2 / numFree;
			float normX = MathUtils.cos(angle);
			float normY = MathUtils.sin(angle);

			visionPolygon.add(raycaster.shootRay(rayLevelTracker, x, y, x + normX, y + normY, r).cpy());
		}

		visionPolygon.sort((a, b) -> {
			float dxA = a.x - playerPos.x;
			float dyA = a.y - playerPos.y;
			float dxB = b.x - playerPos.x;
			float dyB = b.y - playerPos.y;
			float angleA = MathUtils.atan2(dxA, dyA);
			float angleB = MathUtils.atan2(dxB, dyB);

			return Float.compare(angleA, angleB);
		});
	}

	public float[] getVisionPolygon() {
		return visionPolygon.trimmed();
	}

	public ViewingFrustrum getViewingFrustrum() {
		return viewingFrustrum;
	}
}

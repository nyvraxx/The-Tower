package world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
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

	VectorArray visionPolygon;

	private Raycaster raycaster;

	private ViewingFrustrum viewingFrustrum;
	Player player;
	GameWorld gameWorld;
	OrthographicCamera camera;
	FitViewport viewport;

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
		for (int i = 0; i < 20; i++) {
			Entity entity = new Zombie();
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
		if (!worldObject.shouldCollide(player)) {
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
		float x = playerPos.x;
		float y = playerPos.y;
		float r = 10f;

		final float epsilon = 0.001f;
		viewingFrustrum.updateVisible(gameWorld.world);

		for (WorldObject visible : viewingFrustrum.getVisible()) {
			if(!visible.blocksVision()) {
				continue;
			}
			
			GeometryUtils.getVertices(visible.getBody(), vec -> {
				visionPolygon.add(raycaster.shootRay(player.getLevelTracker(), x, y, vec.x, vec.y, r));
				visionPolygon.add(raycaster.shootRay(player.getLevelTracker(), x, y, vec.x + epsilon, vec.y, r));
				visionPolygon.add(raycaster.shootRay(player.getLevelTracker(), x, y, vec.x - epsilon, vec.y, r));
				visionPolygon.add(raycaster.shootRay(player.getLevelTracker(), x, y, vec.x, vec.y + epsilon, r));
				visionPolygon.add(raycaster.shootRay(player.getLevelTracker(), x, y, vec.x, vec.y - epsilon, r));
			});
		}

		int numFree = 16;
		for (int i = 0; i < numFree; i++) {
			float angle = i * MathUtils.PI2 / numFree;
			float normX = MathUtils.cos(angle);
			float normY = MathUtils.sin(angle);

			visionPolygon.add(raycaster.shootRay(player.getLevelTracker(), x, y, x + normX, y + normY, r).cpy());
		}

		visionPolygon.sort((a, b) -> {
			float angleA = MathUtils.atan2(a.x - playerPos.x, a.y - playerPos.y);
			float angleB = MathUtils.atan2(b.x - playerPos.x, b.y - playerPos.y);

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

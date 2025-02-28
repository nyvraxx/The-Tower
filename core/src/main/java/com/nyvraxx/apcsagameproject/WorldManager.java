package com.nyvraxx.apcsagameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import entities.Enemy;
import entities.Player;

public class WorldManager {
	Stage stage;
	World world;
	Player player;

	OrthographicCamera camera;
	FitViewport viewport;

	Array<Enemy> enemies = new Array<Enemy>();
	private float viewWidth = 10f, viewHeight = 10f;

	public WorldManager() {
		camera = new OrthographicCamera();
		camera.position.set(0, 0, 0);
		viewport = new FitViewport(viewWidth, viewHeight, camera);
		stage = new Stage(viewport);

		world = new World(Vector2.Zero, true);

		player = new Player();
		getPlayer().addToWorld(world, 0, 0);

		// TODO debug code{
		for (int i = 0; i < 10; i++) {
			Enemy enemy = new Enemy(this);
			enemy.addToWorld(world, MathUtils.random(10), MathUtils.random(10));
			enemies.add(enemy);
		}
	}

	public void update(float delta) {
		for (int i = 0; i < enemies.size; i++) {
			enemies.get(i).update(delta);
		}

		world.step(delta, 8, 2);
		getPlayer().update(delta);

		Vector2 playerPos = getPlayer().getBody().getPosition();
		camera.position.lerp(new Vector3(playerPos.x, playerPos.y, 0), 18f * delta);
		camera.update();
	}

	public Player getPlayer() {
		return player;
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
}

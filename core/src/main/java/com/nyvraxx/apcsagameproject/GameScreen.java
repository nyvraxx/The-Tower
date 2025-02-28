package com.nyvraxx.apcsagameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {
	float worldWidth = 10f, worldHeight = 10f;

	SpriteBatch spriteBatch;
	Box2DDebugRenderer debugRenderer;

	OrthographicCamera camera;
	FitViewport viewport;
	Stage stage;
	GameManager gameManager;

	public GameScreen(GameManager gameManager) {
		this.gameManager = gameManager;

		spriteBatch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.position.set(0, 0, 0);
		viewport = new FitViewport(worldWidth, worldHeight, camera);
		stage = new Stage(viewport);

		Gdx.input.setInputProcessor(stage);

		debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		input(delta);
		gameManager.update(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		Vector2 playerPos = gameManager.player.getBody().getPosition();
		camera.position.lerp(new Vector3(playerPos.x, playerPos.y, 0), 0.3f);
		camera.update();

		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		spriteBatch.draw(new Texture("test background map.png"), 0, 0, 10f, 10f);
		spriteBatch.end();

		debugRenderer.render(gameManager.world, camera.combined);
		gameManager.world.step(delta, 6, 2);
	}

	private void input(float delta) {
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
			gameManager.player.startRunning();
		} else if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			gameManager.player.stopRunning();
		}
		
		System.out.println(gameManager.player.stamina);
		gameManager.player.move(dx, dy, delta);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {

	}

}

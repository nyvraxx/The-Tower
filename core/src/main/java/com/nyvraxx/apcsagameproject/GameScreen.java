package com.nyvraxx.apcsagameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameScreen implements Screen {
	float worldWidth = 10f, worldHeight = 10f;

	Box2DDebugRenderer debugRenderer;

	SpriteBatch spriteBatch;
	OrthographicCamera camera;
	FitViewport viewport;
	Stage stage;
	GameManager gameManager;

	public GameScreen(GameManager gameManager) {
		this.gameManager = gameManager;
		spriteBatch = new SpriteBatch();
		camera = new OrthographicCamera();
		viewport = new FitViewport(worldWidth, worldHeight, camera);
		stage = new Stage(viewport);

		debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.setProjectionMatrix(camera.combined);

		gameManager.world.createBody(new BodyDef());

		debugRenderer.render(gameManager.world, camera.combined);
		gameManager.world.step(delta, 6, 2);
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

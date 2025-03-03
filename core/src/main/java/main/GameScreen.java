package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

import world.WorldManager;

public class GameScreen implements Screen {
	float worldWidth = 10f, worldHeight = 10f;

	GameRenderer gameRenderer;
	InputMultiplexer inputMultiplexer;
	
	WorldManager worldManager;

	public GameScreen(WorldManager worldManager) {
		gameRenderer = new GameRenderer(worldManager);
		
		inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(worldManager.getStage());
		
		this.worldManager = worldManager;
		
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		input(delta);
		worldManager.update(delta);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//TODO temp debug drawing methods	
		gameRenderer.render(delta);
	}

	private void input(float delta) {
		worldManager.handleInput(delta);
	}

	@Override
	public void resize(int width, int height) {
		worldManager.getViewport().update(width, height);
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

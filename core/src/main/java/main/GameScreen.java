package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;

import world.WorldManager;

public class GameScreen implements Screen {
	GameWorldRenderer gameWorldRenderer;
	InputMultiplexer inputMultiplexer;
	
	WorldManager worldManager;

	public GameScreen(WorldManager worldManager) {
		gameWorldRenderer = new GameWorldRenderer(worldManager);
				
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

		gameWorldRenderer.render(delta);
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

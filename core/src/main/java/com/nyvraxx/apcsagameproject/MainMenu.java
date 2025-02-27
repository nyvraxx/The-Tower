package com.nyvraxx.apcsagameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class MainMenu implements Screen {
	Stage stage;
	SpriteBatch spriteBatch;

	public MainMenu() {
		stage = new Stage(new FitViewport(1f, 1f));
		spriteBatch = new SpriteBatch();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {
		spriteBatch.setProjectionMatrix(stage.getCamera().combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//just for testing
		spriteBatch.begin();
		spriteBatch.draw(new Texture("ohno.png"), 0, 0, 0.5f, 1f);
		spriteBatch.end();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		// Resize your screen here. The parameters represent the new window size.
	}

	@Override
	public void pause() {
		// Invoked when your application is paused.
	}

	@Override
	public void resume() {
		// Invoked when your application is resumed after pause.
	}

	@Override
	public void hide() {
		// This method is called when another screen replaces this one.
	}

	@Override
	public void dispose() {
		// Destroy screen's assets here.
	}
}
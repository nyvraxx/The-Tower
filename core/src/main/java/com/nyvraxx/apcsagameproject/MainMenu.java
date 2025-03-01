package com.nyvraxx.apcsagameproject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import world.WorldManager;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class MainMenu implements Screen {
	Main main;
	Stage stage;
	SpriteBatch spriteBatch;

	public MainMenu(Main main) {
		this.main = main;
		stage = new Stage(new FitViewport(1f, 1f));
		spriteBatch = new SpriteBatch();

		stage.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				main.setScreen(new GameScreen(new WorldManager()));
				return true; // Return true if the event was handled
			}
		});
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

		// just for testing
		spriteBatch.begin();
		spriteBatch.draw(new Texture("ohno.png"), 0, 0, 1f, 1f);
		spriteBatch.end();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
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
		stage.dispose();
		spriteBatch.dispose();
	}
}
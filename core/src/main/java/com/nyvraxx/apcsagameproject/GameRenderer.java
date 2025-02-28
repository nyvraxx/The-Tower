package com.nyvraxx.apcsagameproject;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

public class GameRenderer {
	WorldManager worldManager;
	SpriteBatch spriteBatch;
	Box2DDebugRenderer debugRenderer;

	public GameRenderer(WorldManager worldManager) {
		spriteBatch = new SpriteBatch();
		this.worldManager = worldManager;

		debugRenderer = new Box2DDebugRenderer();
	}

	public void render(float delta) {
		spriteBatch.setProjectionMatrix(worldManager.camera.combined);
		spriteBatch.begin();
		spriteBatch.draw(new Texture("test background map.png"), 0, 0, 10f, 10f);
		spriteBatch.end();

		debugRenderer.render(worldManager.world, worldManager.camera.combined);
	}
}

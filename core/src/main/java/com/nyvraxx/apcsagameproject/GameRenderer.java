package com.nyvraxx.apcsagameproject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import entities.Entity;
import world.WorldManager;

public class GameRenderer {
	WorldManager worldManager;
	SpriteBatch spriteBatch;
	Box2DDebugRenderer debugRenderer;
	ShapeRenderer shapeRenderer;

	public GameRenderer(WorldManager worldManager) {
		spriteBatch = new SpriteBatch();
		this.worldManager = worldManager;

		debugRenderer = new Box2DDebugRenderer();
		shapeRenderer = new ShapeRenderer();
	}

	public void render(float delta) {
		spriteBatch.setProjectionMatrix(worldManager.getCamera().combined);

		debugRenderer.render(worldManager.getGameWorld().getWorld(), worldManager.getCamera().combined);
		shapeRenderer.setProjectionMatrix(worldManager.getCamera().combined);
		
		shapeRenderer.setAutoShapeType(true);
		shapeRenderer.begin();
		
		for (Entity entity : worldManager.getGameWorld().getEntities()) {
			shapeRenderer.setColor(new Color(entity.getLevel() * 0.2f + 0.5f, 0f, 0f, 1f));
			shapeRenderer.circle(entity.getBody().getTransform().getPosition().x, entity.getBody().getTransform().getPosition().y, 0.1f, 10);
		}
		shapeRenderer.end();
	}
}

package main;

import java.util.Arrays;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import entities.Entity;
import entities.Platform;
import entities.Player;
import entities.WorldObject;
import world.WorldManager;

public class GameWorldRenderer {
	WorldManager worldManager;
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;
	Box2DDebugRenderer debugRenderer;

	public GameWorldRenderer(WorldManager worldManager) {
		spriteBatch = new SpriteBatch();
		this.worldManager = worldManager;

		debugRenderer = new Box2DDebugRenderer();
		shapeRenderer = new ShapeRenderer();
	}

	public void render(float delta) {
		Player player = worldManager.getPlayer();

		spriteBatch.setProjectionMatrix(worldManager.getCamera().combined);
		spriteBatch.begin();
		for (WorldObject worldObject : worldManager.getGameWorld().getWorldObjects()) {
			if (worldObject instanceof Platform && worldObject.shouldCollide(player)) {
				worldObject.render(spriteBatch);
			}
		}
		for (WorldObject worldObject : worldManager.getGameWorld().getWorldObjects()) {
			if (!(worldObject instanceof Platform) && !(worldObject instanceof Entity)
					&& worldObject.shouldCollide(player)) {
				worldObject.render(spriteBatch);
			}
		}
		for (WorldObject worldObject : worldManager.getGameWorld().getWorldObjects()) {
			if (worldObject instanceof Entity && worldObject != worldManager.getPlayer()) {
				if (worldManager.isVisible(worldObject)) {
					worldObject.render(spriteBatch);
				}
			}
		}
		spriteBatch.end();

		shapeRenderer.setColor(1f, 0f, 0, 1f);
		shapeRenderer.setAutoShapeType(true);
		shapeRenderer.setProjectionMatrix(worldManager.getCamera().combined);
		shapeRenderer.begin();
		float[] points = worldManager.getVisionPolygon();
		for (int i = 0; i < points.length; i += 2) {
			shapeRenderer.circle(points[i], points[i + 1], 0.05f, 10);
		}
		shapeRenderer.polygon(points);

		shapeRenderer.end();

		debugRenderer.render(worldManager.getGameWorld().getWorld(), worldManager.getCamera().combined);
	}
}

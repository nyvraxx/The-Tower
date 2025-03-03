package main;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;

import entities.Entity;
import entities.Platform;
import entities.Player;
import world.WorldManager;

public class GameWorldRenderer {
	WorldManager worldManager;
	SpriteBatch spriteBatch;
	Box2DDebugRenderer debugRenderer;

	public GameWorldRenderer(WorldManager worldManager) {
		spriteBatch = new SpriteBatch();
		this.worldManager = worldManager;

		debugRenderer = new Box2DDebugRenderer();
	}

	public void render(float delta) {
		Player player = worldManager.getPlayer();

		spriteBatch.setProjectionMatrix(worldManager.getCamera().combined);
		spriteBatch.begin();
		for (Platform platform : worldManager.getGameWorld().getPlatforms()) {
			if (platform.shouldCollide(player))
				platform.render(spriteBatch);
		}
		for (Entity entity : worldManager.getGameWorld().getEntities()) {
			if (entity.shouldCollide(player)) {
				entity.render(spriteBatch);
			}
		}
		spriteBatch.end();

		debugRenderer.render(worldManager.getGameWorld().getWorld(), worldManager.getCamera().combined);
	}
}

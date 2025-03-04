package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;

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
		shapeRenderer = new ShapeRenderer(1000000);
	}

	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		Player player = worldManager.getPlayer();

		float[] points = worldManager.getVisionPolygon();
		shapeRenderer.setProjectionMatrix(worldManager.getCamera().combined);
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
		shapeRenderer.polygon(points);
		shapeRenderer.end();

//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
//		Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
//
//		Gdx.gl.glColorMask(false, false, false, false); // Disable color writing
//		Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 0xFF); // Always pass stencil test
//		Gdx.gl.glStencilOp(GL20.GL_REPLACE, GL20.GL_REPLACE, GL20.GL_REPLACE); // Replace stencil buffer
//
//
//		Gdx.gl.glColorMask(true, true, true, true); // Re-enable color writing
//		Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 1, 0xFF); // Only draw where stencil == 1
//		Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP); // Keep stencil buffer unchanged

		spriteBatch.setProjectionMatrix(worldManager.getCamera().combined);
		spriteBatch.begin();

		Array<WorldObject> onScreen = worldManager.getViewingFrustrum().getVisible();

		for (WorldObject worldObject : onScreen) {
			if (!worldManager.isVisible(worldObject))
				continue;

			if (worldObject.alwaysVisible() && !worldObject.blocksVision()) {
				worldObject.render(spriteBatch);
			}
		}
		for (WorldObject worldObject : onScreen) {
			if (!worldManager.isVisible(worldObject))
				continue;

			if (worldObject.alwaysVisible() && worldObject.blocksVision()) {
				worldObject.render(spriteBatch);
			}
		}

		for (WorldObject worldObject : onScreen) {
			if (!worldManager.isVisible(worldObject))
				continue;

			if (!worldObject.alwaysVisible()) {
				worldObject.render(spriteBatch);
			}
		}
		spriteBatch.end();
		Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);

		debugRenderer.render(worldManager.getGameWorld().getWorld(), worldManager.getCamera().combined);
	}
}

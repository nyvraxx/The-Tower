package main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ShortArray;

import entities.WorldObject;
import world.WorldManager;

public class GameWorldRenderer {
	WorldManager worldManager;
	SpriteBatch spriteBatch;
	ShapeRenderer shapeRenderer;
	Box2DDebugRenderer debugRenderer;

	private EarClippingTriangulator earClippingTriangulator = new EarClippingTriangulator();

	public GameWorldRenderer(WorldManager worldManager) {
		spriteBatch = new SpriteBatch();
		this.worldManager = worldManager;

		debugRenderer = new Box2DDebugRenderer();
		shapeRenderer = new ShapeRenderer(1000000);
	}

	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_STENCIL_BUFFER_BIT);
		Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);
		Gdx.gl.glClearStencil(0);
		Gdx.gl.glDisable(GL20.GL_DEPTH_TEST);

		spriteBatch.setProjectionMatrix(worldManager.getCamera().combined);
		shapeRenderer.setProjectionMatrix(worldManager.getCamera().combined);
		spriteBatch.begin();

		float[] points = worldManager.getVisionPolygon();
		Array<WorldObject> onScreen = worldManager.getViewingFrustrum().getVisibleWorldObjects();
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

		spriteBatch.end();

		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.setAutoShapeType(true);
		shapeRenderer.begin();
		shapeRenderer.polygon(points);
		shapeRenderer.end();
//
//		shapeRenderer.setProjectionMatrix(worldManager.getCamera().combined);
//		shapeRenderer.setColor(1, 0, 0, 1);
//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//
//		ShortArray triangles1 = earClippingTriangulator.computeTriangles(points);
//		for (int i = 0; i < triangles1.size; i += 3) {
//			int a = triangles1.get(i);
//			int b = triangles1.get(i + 1);
//			int c = triangles1.get(i + 2);
//
//			shapeRenderer.triangle(points[2 * a], points[2 * a + 1], points[2 * b], points[2 * b + 1], points[2 * c],
//					points[2 * c + 1]);
//		}
//
//		for (int i = 0; i < points.length; i+=2) {
//			shapeRenderer.setColor(0, 0, 1, 1);
//			shapeRenderer.circle(points[i], points[i + 1], 0.05f, 10);
//		}
//		shapeRenderer.end();
//
		Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);
		Gdx.gl.glColorMask(false, false, false, false); // Disable color writing
		Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 0xFF); // Always pass stencil test
		Gdx.gl.glStencilOp(GL20.GL_REPLACE, GL20.GL_REPLACE, GL20.GL_REPLACE); // Replace stencil buffer

		// draws the mask
		shapeRenderer.setProjectionMatrix(worldManager.getCamera().combined);
		shapeRenderer.setColor(1, 0, 0, 1);
		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		ShortArray triangles = earClippingTriangulator.computeTriangles(points);
		for (int i = 0; i < triangles.size; i += 3) {
			int a = triangles.get(i);
			int b = triangles.get(i + 1);
			int c = triangles.get(i + 2);

			shapeRenderer.triangle(points[2 * a], points[2 * a + 1], points[2 * b], points[2 * b + 1], points[2 * c],
					points[2 * c + 1]);
		}
		shapeRenderer.end();

		Gdx.gl.glColorMask(true, true, true, true); // Re-enable color writing
		Gdx.gl.glStencilFunc(GL20.GL_EQUAL, 1, 0xFF); // Only draw where stencil == 1
		Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP); // Keep stencil buffer unchanged

		spriteBatch.begin();

		for (WorldObject worldObject : onScreen) {
			if (!worldManager.isVisible(worldObject))
				continue;

			if (!worldObject.alwaysVisible()) {
				worldObject.render(spriteBatch);
			}
		}
		spriteBatch.end();

		Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);
	}
}

package world;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;

import entities.LevelTracker;
import entities.WorldObject;

public class Raycaster {
	private Vector2 returnVec = new Vector2();
	private RayCastFirstObject callback;
	private World world;

	public Raycaster(World world) {
		this.world = world;

		callback = new RayCastFirstObject();
	}

	public Vector2 shootRay(LevelTracker origin, float x, float y, float toX, float toY, float maxDist) {
		float normX = toX - x;
		float normY = toY - y;

		float invMag = 1.0f / (float) Math.sqrt(normX * normX + normY * normY);
		normX *= invMag;
		normY *= invMag;

		callback.reset(origin);

		float destX = x + maxDist * normX;
		float destY = y + maxDist * normY;
		world.rayCast(callback, x, y, destX, destY);

		if (callback.found) {
			returnVec.set(callback.firstX, callback.firstY);
		} else {
			returnVec.set(destX, destY);
		}

		return returnVec;
	}

	private static class RayCastFirstObject implements RayCastCallback {
		float firstX, firstY;
		boolean found = false;
		LevelTracker origin;

		RayCastFirstObject() {
		}

		void reset(LevelTracker origin) {
			this.origin = origin;
			firstX = 0;
			firstY = 0;
			found = false;
		}

		@Override
		public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
			WorldObject other = (WorldObject) fixture.getBody().getUserData();
			if (!other.blocksVision()) {
				return -1;
			}
			if (!origin.shouldCollide(((WorldObject) other.getBody().getUserData()).getLevelTracker())) {
				return -1;
			}

			found = true;

			firstX = point.x;
			firstY = point.y;
			return fraction;
		}
	}

}

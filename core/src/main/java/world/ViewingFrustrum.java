package world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;

import entities.WorldObject;

public class ViewingFrustrum {
	private Array<Fixture> visible = new Array<>();
	private Array<WorldObject> visibleWorldObjects = new Array<>();
	private ObjectSet<WorldObject> seenWorldObjects = new ObjectSet<>();

	private QueryCallback callback = new QueryCallback() {
		@Override
		public boolean reportFixture(Fixture fixture) {
			visible.add(fixture);
			return true;
		}
	};

	Vector3 lowerLeft = new Vector3();
	Vector3 upperRight = new Vector3();

	public void updateCamera(Camera camera) {
		lowerLeft.set(0, Gdx.graphics.getHeight(), 0);
		upperRight.set(Gdx.graphics.getWidth(), 0, 0);

		camera.unproject(lowerLeft);
		camera.unproject(upperRight);
	}

	public void updateVisible(World world) {
		visible.clear();
		visibleWorldObjects.clear();
		seenWorldObjects.clear();

		world.QueryAABB(callback, left(), lower(), right(), upper());

		for (Fixture fixture : visible) {
			WorldObject worldObject = (WorldObject) fixture.getBody().getUserData();
			if (seenWorldObjects.add(worldObject)) {
				visibleWorldObjects.add(worldObject);
			}
		}
	}

	public Array<Fixture> getVisible() {
		return visible;
	}

	public float left() {
		return lowerLeft.x;
	}

	public float right() {
		return upperRight.x;
	}

	public float lower() {
		return lowerLeft.y;
	}

	public float upper() {
		return upperRight.y;
	}

	public Array<WorldObject> getVisibleWorldObjects() {
		return visibleWorldObjects;
	}
}

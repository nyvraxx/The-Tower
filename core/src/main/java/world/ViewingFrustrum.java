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
	private Array<WorldObject> visible = new Array<>();
	ObjectSet<WorldObject> seen = new ObjectSet<>();

	private QueryCallback callback = new QueryCallback() {
		@Override
		public boolean reportFixture(Fixture fixture) {
			WorldObject worldObject = (WorldObject) fixture.getBody().getUserData();

			if (seen.add(worldObject)) {
				visible.add(worldObject);
			}

			return true;
		}
	};

	Vector3 lowerLeft = new Vector3();
	Vector3 upperRight = new Vector3();

	public void update(Camera camera) {
		lowerLeft.set(0, Gdx.graphics.getHeight(), 0);
		upperRight.set(Gdx.graphics.getWidth(), 0, 0);

		camera.unproject(lowerLeft);
		camera.unproject(upperRight);
	}

	public void updateVisible(World world) {
		visible.clear();
		seen.clear();

		world.QueryAABB(this.callback, left(), lower(), right(), upper());
	}

	public Array<WorldObject> getVisible() {
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
}

package util;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

public class GeometryUtils {
	private static final int CircleApproxEdges = 40;
	private static final float CircleApproxEdgesInterval = MathUtils.PI2 / CircleApproxEdges;

	/**
	 * 
	 * @param body
	 * @param consumer
	 * @apiNote the Vector2 reference is reused
	 */
	public static void getVertices(Body body, Consumer<Vector2> consumer) {
		Vector2 vec = body.getPosition();
		float x = vec.x;
		float y = vec.y;

		float angle = body.getAngle();

		for (Fixture fixture : body.getFixtureList()) {
			Shape shape = fixture.getShape();
			getVertices(shape, vec, local -> {
				consumer.accept(local.rotateRad(angle).add(x, y));
			});
		}
	}

	public static boolean testPoint(Body body, float x, float y) {
		for (Fixture fixture : body.getFixtureList()) {
			if (fixture.testPoint(x, y)) {
				return true;
			}
		}

		return false;
	}

	public static boolean completelyContains(Body bigBody, Body smallBody) {
		AtomicBoolean found = new AtomicBoolean(true);

		getVertices(smallBody, vec -> {
			if (!found.get()) {
				return;
			}
			if (!testPoint(bigBody, vec.x, vec.y)) {
				found.set(false);
				return;
			}
		});

		return found.get();
	}

	private static void getVertices(Shape shape, Vector2 vec, Consumer<Vector2> consumer) {
		if (shape instanceof CircleShape) {
			float r = ((CircleShape) shape).getRadius();
			for (int i = 0; i < CircleApproxEdges; i++) {
				float angle = i * CircleApproxEdgesInterval;

				vec.x = r * MathUtils.cos(angle);
				vec.y = r * MathUtils.sin(angle);

				consumer.accept(vec);
			}
		} else if (shape instanceof PolygonShape) {
			PolygonShape polygonShape = (PolygonShape) shape;

			for (int i = 0; i < polygonShape.getVertexCount(); i++) {
				polygonShape.getVertex(i, vec);

				consumer.accept(vec);
			}
		}
	}
}

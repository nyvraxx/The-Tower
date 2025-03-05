package util;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.Transform;

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
		Vector2 vec = body.getPosition().cpy();
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

	public static void getVertices(Fixture fixture, Consumer<Vector2> consumer) {
		Transform transform = fixture.getBody().getTransform();
		Vector2 vec = transform.getPosition().cpy();
		float x = vec.x;
		float y = vec.y;

		float angle = transform.getRotation();

		Shape shape = fixture.getShape();
		getVertices(shape, vec, local -> {
			consumer.accept(local.rotateRad(angle).add(x, y));
		});
	}

	/**
	 * @apiNote do not modify the vectors or retain any references or unexpected
	 *          things might occur
	 */
	public static void getEdges(Fixture fixture, BiConsumer<Vector2, Vector2> consumer) {
		Vector2 prev = new Vector2();
		Vector2 cur = new Vector2();
		Vector2 first = new Vector2(Float.NaN, 0);

		getVertices(fixture, vec -> {
			cur.set(vec);

			if (Float.isNaN(first.x)) {
				first.set(vec);
				prev.set(vec);
				return;
			}

			consumer.accept(prev, cur);

			prev.set(vec);
		});

		consumer.accept(cur, first);
	}

	public static void getCircleEdgeIntersection(Vector2 p1, Vector2 p2, Vector2 center, float radius, Vector2 result1,
			Vector2 result2) {
		float x1x2Diff = p2.x - p1.x;
		float y1y2Diff = p2.y - p1.y;
		float dx1 = p1.x - center.x;
		float dy1 = p1.y - center.y;
		float a = x1x2Diff * x1x2Diff + y1y2Diff * y1y2Diff;
		float b = 2 * (dx1 * x1x2Diff + dy1 * y1y2Diff);
		float c = dx1 * dx1 + dy1 * dy1 - radius * radius;

		float discriminant = b * b - 4 * a * c;

		if (discriminant < 0) {
			result1.x = Float.NaN;
			result1.y = Float.NaN;
			result2.x = Float.NaN;
			result2.y = Float.NaN;
			return;
		}

		float invDenom = 0.5f / a;
		float sqrtTerm = (float) Math.sqrt(discriminant);

		float t1 = invDenom * (-b + sqrtTerm);
		float t2 = invDenom * (-b - sqrtTerm);

		if (0 <= t1 && t1 <= 1) {
			result1.x = p1.x + t1 * x1x2Diff;
			result1.y = p1.y + t1 * y1y2Diff;
		} else {
			result1.x = Float.NaN;
			result1.y = Float.NaN;
		}
		if (0 <= t2 && t2 <= 1) {
			result2.x = p1.x + t2 * x1x2Diff;
			result2.y = p1.y + t2 * y1y2Diff;
		} else {
			result2.x = Float.NaN;
			result2.y = Float.NaN;
		}
	}
}

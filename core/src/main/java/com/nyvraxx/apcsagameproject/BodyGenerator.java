package com.nyvraxx.apcsagameproject;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class BodyGenerator {
	private BodyGenerator() {
	}

	public static void setSquare(Body body, float r) {
		PolygonShape square = new PolygonShape();
		square.setAsBox(r, r);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = square;
		fixtureDef.density = 1f; // Mass
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0.2f; // Bounciness

		body.createFixture(fixtureDef);

		square.dispose();
	}
}

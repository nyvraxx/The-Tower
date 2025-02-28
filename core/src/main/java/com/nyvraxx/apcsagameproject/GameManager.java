package com.nyvraxx.apcsagameproject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import entities.Player;

public class GameManager {
	World world;
	Player player;

	public GameManager() {
		world = new World(Vector2.Zero, true);

		player = new Player();
		player.addToWorld(world, 0, 0);
	}

	public void update(float delta) {
		player.update(delta);
	}
}

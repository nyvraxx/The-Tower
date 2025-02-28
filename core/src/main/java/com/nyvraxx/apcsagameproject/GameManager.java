package com.nyvraxx.apcsagameproject;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import entities.Enemy;
import entities.Player;

public class GameManager {
	World world;
	Player player;

	Array<Enemy> enemies = new Array<Enemy>();

	public GameManager() {
		world = new World(Vector2.Zero, true);

		player = new Player();
		getPlayer().addToWorld(world, 0, 0);

		// TODO debug code{
		for (int i = 0; i < 10; i++) {
			Enemy enemy = new Enemy(this);
			enemy.addToWorld(world, MathUtils.random(10), MathUtils.random(10));
			enemies.add(enemy);
		}
	}

	public void update(float delta) {
		for (int i = 0; i < enemies.size; i++) {
			enemies.get(i).update(delta);
		}

		getPlayer().update(delta);
	}

	public Player getPlayer() {
		return player;
	}
}

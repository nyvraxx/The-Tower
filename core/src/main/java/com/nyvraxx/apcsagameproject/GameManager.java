package com.nyvraxx.apcsagameproject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameManager {
	World world;
	
	public GameManager() {
		world = new World(Vector2.Zero, true);
	}
	
	
}

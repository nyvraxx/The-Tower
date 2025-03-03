package main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.Box2D;
/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {
	
	@Override
	public void create() {
		Box2D.init();
		
		setScreen(new MainMenu(this));
	}
}
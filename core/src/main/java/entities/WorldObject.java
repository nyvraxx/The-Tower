package entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;

public interface WorldObject {
	BodyDef getBodyDef();

	LevelTracker getLevelTracker();

	void initializeBody(Body body);

	void beginContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other);

	void endContact(Fixture fixtureSelf, Fixture fixtureOther, WorldObject other);

	public default boolean shouldCollide(WorldObject other) {
		LevelTracker levA = getLevelTracker();
		LevelTracker levB = other.getLevelTracker();

		if (levA.inTransition && levB.inTransition) {
			return levA.level == levB.level;
		} else if (levA.inTransition ^ levB.inTransition) {
			// make levA the one in transition
			if (levB.inTransition) {
				LevelTracker temp = levA;
				levA = levB;
				levB = temp;
			}

			int levA1 = levA.level;
			int levA2 = levA.level + 1;

			return levB.level == levA1 || levB.level == levA2;
		} else {
			return levA.level == levB.level;
		}
	}
}

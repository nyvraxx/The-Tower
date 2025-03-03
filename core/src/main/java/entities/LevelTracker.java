package entities;

public class LevelTracker {
	public int level;
	public boolean inTransition = false;

	public LevelTracker() {
		level = Integer.MIN_VALUE;
	}

	public LevelTracker(int level) {
		this.level = level;
	}

	@Override
	public String toString() {
		if (inTransition) {
			return "level " + level + ", " + (level + 1);
		} else {
			return "level " + level;
		}
	}

	public boolean shouldCollide(LevelTracker other) {
		LevelTracker levA = this;
		LevelTracker levB = other;

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

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
}

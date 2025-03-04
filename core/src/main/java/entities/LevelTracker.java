package entities;

public class LevelTracker implements Cloneable {
	public static final float Middle = -0.5f;
	public int level;
	public float fraction = 0;
	public boolean onStairs = false;

	public LevelTracker() {
		level = Integer.MIN_VALUE;
	}

	public LevelTracker(int level) {
		this.level = level;
	}

	@Override
	public String toString() {
		if (onStairs) {
			return "level " + level + " frac: " + String.format("%1.3f", +fraction) + " " + onStairs + " rounded:"
					+ getRoundedLevel();
		} else {
			return "level " + level;
		}
	}

	private int getRoundedLevel() {
		if (fraction >= 0.5)
			return level + 1;
		else
			return level;
	}

	/**
	 * 
	 * @param other
	 * @return if the levelTracker is visible from this objects perspective This
	 *         method is not commutative!
	 */
	public boolean isVisible(LevelTracker other) {
		LevelTracker levA = this;
		LevelTracker levB = other;

		if (levA.onStairs && levB.onStairs) {
			return levA.level == levB.level;
		} else if (levA.onStairs && !levB.onStairs) {
			if (levA.getRoundedLevel() == levB.level)
				return true;
			else
				return false;
		} else if (!levA.onStairs && levB.onStairs) {
			if (levA.level == levB.level + 1)
				return true;

			return levA.level == levB.level;
		} else {
			return levA.level == levB.level;
		}
	}

	public boolean shouldCollide(LevelTracker other) {
		LevelTracker levA = this;
		LevelTracker levB = other;

		if (levA.onStairs && levB.onStairs) {
			return levA.level == levB.level;
		} else if (levA.onStairs ^ levB.onStairs) {
			// make levA the one in transition
			if (levB.onStairs) {
				LevelTracker temp = levA;
				levA = levB;
				levB = temp;
			}

			if (levA.fraction == LevelTracker.Middle) {
				return levB.level == levA.level || levB.level == levA.level + 1;
			} else {
				return levA.getRoundedLevel() == levB.level;
			}
		} else {
			return levA.level == levB.level;
		}
	}

	public LevelTracker clone() {
		LevelTracker levelTracker = new LevelTracker();

		levelTracker.level = level;
		levelTracker.fraction = fraction;
		levelTracker.onStairs = onStairs;

		return levelTracker;
	}

	public void round() {
		if (fraction >= 0.5) {
			level++;
		}

		fraction = 0;
	}
}

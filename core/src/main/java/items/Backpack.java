package items;

import com.badlogic.gdx.utils.Array;

public class Backpack {

	Array<ItemPlacement> items;

	boolean[][] occupied;
	int width, height;

	public Backpack(int width, int height) {
		super();
		this.width = width;
		this.height = height;

		occupied = new boolean[width][height];

		items = new Array<>();
	}

	private void trustedAdd(ItemPlacement itemPlacement) {
		int width = itemPlacement.getWidth();
		int height = itemPlacement.getHeight();
		
		int x = itemPlacement.x;
		int y = itemPlacement.y;

		items.add(new ItemPlacement(itemPlacement.item, x, y, itemPlacement.rotation));

		for (int i = x; i < x + width; i++) {
			for (int j = y; j < y + height; j++) {
				if (occupied[i][j]) {// TODO maybe add another exception class here
					throw new RuntimeException();
				}
				occupied[i][j] = true;
			}
		}
	}

	public void remove(ItemPlacement itemPlacement) {
		if (!items.contains(itemPlacement, false)) {
			throw new IllegalArgumentException("Item not found.");
		}

		int width = itemPlacement.getWidth();
		int height = itemPlacement.getHeight();

		int x = itemPlacement.x;
		int y = itemPlacement.y;

		items.removeValue(itemPlacement, false);

		for (int i = x; i < x + width; i++) {
			for (int j = y; j < y + height; j++) {
				occupied[i][j] = false;
			}
		}

	}

	public boolean canAdd(ItemPlacement itemPlacement) {
		int x = itemPlacement.x;
		int y = itemPlacement.y;

		int width = itemPlacement.getWidth();
		int height = itemPlacement.getHeight();

		if (x < 0 || y < 0 || x + width >= this.width || y + height >= this.height) {
			return false;
		}

		for (int i = x; i < x + width; i++) {
			for (int j = y; j < y + height; j++) {
				if (occupied[i][j]) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * @param itemPlacement
	 * @return true if added, false it not added
	 * @apiNote ItemPlacement instance should not be reused for other items if the
	 *          method returns true
	 */
	public boolean tryAdd(ItemPlacement itemPlacement) {
		boolean valid = canAdd(itemPlacement);

		if (valid) {
			trustedAdd(itemPlacement);
		}

		return valid;
	}

}

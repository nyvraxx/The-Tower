package items;

public class ItemPlacement {
	public static final int DOWN = 0;// default
	public static final int RIGHT = 1;

	final Item item;
	/**
	 * positions are zero-indexed
	 */
	final int x, y;
	int rotation;

	public ItemPlacement(Item item, int x, int y, int rotation) {
		this.item = item;
		this.x = x;
		this.y = y;
	}

	public int getWidth() {
		return rotation == DOWN ? item.getInventoryWidth() : item.getInventoryHeight();
	}

	public int getHeight() {
		return rotation == RIGHT ? item.getInventoryHeight() : item.getInventoryWidth();
	}
}

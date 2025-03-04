package world;

import java.util.Arrays;
import java.util.Comparator;

import com.badlogic.gdx.math.Vector2;

import entities.WorldObject;

/**
 * @apiNote this class is not thread safe
 */
public class VectorArray {
	private Vector2[] compareVecs = new Vector2[] { new Vector2(), new Vector2(), };

	private float[] arr;
	private int count;

	public void clear() {
		count = 0;
	}

	public VectorArray() {
		arr = new float[32];
		count = 0;
	}

	public void add(float x, float y) {
		ensureCapacity();

		arr[count] = x;
		arr[count + 1] = y;
		count += 2;
	}

	public int size() {
		return count >> 1;
	}

	public float x(int index) {
		checkBounds(index);
		return arr[index * 2];
	}

	public float y(int index) {
		checkBounds(index);
		return arr[index * 2 + 1];
	}

	private void ensureCapacity() {
		if (count >= arr.length) {
			arr = Arrays.copyOf(arr, arr.length * 2);
		}
	}

	private void checkBounds(int index) {
		if (index < 0 || index >= size()) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
	}

	public void sort(Comparator<Vector2> comparator) {
		if (size() != 0)
			quicksort(0, size() - 1, comparator);
	}

	private void quicksort(int low, int high, Comparator<Vector2> comparator) {
		if (low < high) {
			int pivotIndex = partition(low, high, comparator);
			quicksort(low, pivotIndex - 1, comparator);
			quicksort(pivotIndex + 1, high, comparator);
		}
	}

	private int compare(float x1, float y1, float x2, float y2, Comparator<Vector2> comparator) {
		compareVecs[0].x = x1;
		compareVecs[0].y = y1;
		compareVecs[1].x = x2;
		compareVecs[1].y = y2;

		return comparator.compare(compareVecs[0], compareVecs[1]);
	}

	private int partition(int low, int high, Comparator<Vector2> comparator) {
		float pivotX = x(high);
		float pivotY = y(high);

		int i = low - 1;

		for (int j = low; j < high; j++) {
			if (compare(x(j), y(j), pivotX, pivotY, comparator) < 0) {
				i++;
				swap(i, j);
			}
		}
		swap(i + 1, high);
		return i + 1;
	}

	private void swap(int i, int j) {
		float tempX = arr[i * 2];
		float tempY = arr[i * 2 + 1];
		arr[i * 2] = arr[j * 2];
		arr[i * 2 + 1] = arr[j * 2 + 1];
		arr[j * 2] = tempX;
		arr[j * 2 + 1] = tempY;
	}

	/**
	 * @return the array of points ordered in x, y, x, y,...
	 * @apiNote do not modify the array!
	 */
	public float[] getArr() {
		return arr;
	}

	public void add(Vector2 vector2) {
		add(vector2.x, vector2.y);
	}

	public float[] trimmed() {
		return Arrays.copyOf(arr, count);
	}
}

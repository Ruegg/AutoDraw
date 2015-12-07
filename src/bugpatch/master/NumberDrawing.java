package bugpatch.master;

import java.awt.Point;
import java.util.HashMap;

public class NumberDrawing {

	public HashMap<Integer, Point> getInt(int x, int y, int height, int number) {
		if (number > 0 && number < 10) {
			switch (number) {
			case 0: return get0(x, y, height);
			case 1: return get1(x, y, height);
			case 2: return get2(x, y, height);
			case 3: return get3(x, y, height);
			case 4: return get4(x, y, height);
			case 5: return get5(x, y, height);
			case 6: return get6(x, y, height);
			case 7: return get7(x, y, height);
			case 8: return get8(x, y, height);
			case 9: return get9(x, y, height);			
			}
			return null;
		} else {
			return null;
		}
	}

	public HashMap<Integer, Point> get0(int x, int y, int height) {
		return null;
	}

	public HashMap<Integer, Point> get1(int x, int y, int height) {
		return null;
	}

	public HashMap<Integer, Point> get2(int x, int y, int height) {
		return null;
	}

	public HashMap<Integer, Point> get3(int x, int y, int height) {
		return null;
	}

	public HashMap<Integer, Point> get4(int x, int y, int height) {
		HashMap<Integer, Point> N4 = new HashMap<Integer, Point>();
		int y1 = y + (height / 2);
		N4.put(0, new Point(x, y1));
		int x2 = x + (height / 3);
		N4.put(1, new Point(x2, y1));
		N4.put(2, new Point(x2, y));
		N4.put(3, new Point(x2, (y + height)));
		N4.put(4, new Point(x2 + (height / 3), y));
		return N4;
	}

	public HashMap<Integer, Point> get5(int x, int y, int height) {
		return null;
	}

	public HashMap<Integer, Point> get6(int x, int y, int height) {
		return null;
	}

	public HashMap<Integer, Point> get7(int x, int y, int height) {
		return null;
	}

	public HashMap<Integer, Point> get8(int x, int y, int height) {
		return null;
	}

	public HashMap<Integer, Point> get9(int x, int y, int height) {
		return null;
	}
}

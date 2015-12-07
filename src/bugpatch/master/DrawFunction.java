package bugpatch.master;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DrawFunction {

	public HashMap<Integer, String> circleDrawHistory = new HashMap<Integer, String>();
	public HashMap<Integer, String> angleDrawHistory = new HashMap<Integer, String>();
	public HashMap<Integer, String> triangleDrawHistory = new HashMap<Integer, String>();
	public HashMap<Integer, String> rectangleDrawHistory = new HashMap<Integer, String>();
	public HashMap<Integer, ImageHData> imageDrawHistory = new HashMap<Integer, ImageHData>();
	// public NumberDrawing nd = new NumberDrawing();

	public JButton backButton;

	public DrawFunction(JButton b) {
		backButton = b;
	}
	/*
	 * public void drawNumber(int x, int y, int n) { String number = "" + n;
	 * Robot bot = null; try { bot = new Robot(); } catch (AWTException e) {
	 * System.out.println("Problem creating robot"); }
	 * 
	 * Point lastPoint = null;
	 * 
	 * for (char c : number.toCharArray()) { int curNum = Integer.parseInt("" +
	 * c); HashMap<Integer, Point> points = null; if (lastPoint != null) {
	 * points = nd.getInt(lastPoint.x, lastPoint.y, 40, curNum); } else { points
	 * = nd.getInt(x, y, 40, curNum); } for (int b = 0; b != points.size(); b++)
	 * { if (b == 0) { bot.mousePress(InputEvent.BUTTON1_MASK); } Point curPoint
	 * = points.get(b); if (b != points.size() - 1) { bot.delay(10);
	 * bot.mouseMove(curPoint.x, curPoint.y); } else { bot.delay(10);
	 * bot.mouseRelease(InputEvent.BUTTON1_MASK); bot.mouseMove(curPoint.x,
	 * curPoint.y); lastPoint = curPoint; } } } bot = null; }
	 */

	public void drawCircle(double radius, int scale, boolean history) {
		MarkupSort data = new MarkupSort();
		data.setLargeTitle("Circle");
		data.setShape("circle");
		data.addTitle("Sizes");

		radius = radius * scale;

		data.addData("Radius - " + radius, "Sizes");
		data.addData("Scale - " + scale, "Sizes");

		int x = (int) MouseInfo.getPointerInfo().getLocation().getX();
		int y = (int) MouseInfo.getPointerInfo().getLocation().getY();

		Robot bot = null;
		try {
			bot = new Robot();
		} catch (AWTException e) {
			backButton.setEnabled(true);
			System.out.println("Problem creating robot");
		}

		double slice = 2 * Math.PI / (radius * scale);
		for (int i = 0; i < (radius * scale); i++) {
			double angle = slice * i;
			int newX = (int) (x + radius * Math.cos(angle));
			int newY = (int) (y + radius * Math.sin(angle));
			if (i == 0) {
				bot.mouseMove(newX, newY);
				bot.mousePress(InputEvent.BUTTON1_MASK);
			} else {
				bot.mouseMove(newX, newY);
			}
		}
		bot.mouseRelease(InputEvent.BUTTON1_MASK);

		if (history) {
			int runningTime = AutoDraw.runningTime;
			AutoDraw.history.put(runningTime, data.exportMarkup());
			String redrawData = radius + "!" + scale;
			circleDrawHistory.put(runningTime, redrawData);
		}
		backButton.setEnabled(true);
	}

	public void drawImage(JLabel progressLabel, File selectedImage, int delay, BufferedImage i, boolean history) {
		MarkupSort data = new MarkupSort();
		data.setShape("Image");
		if (selectedImage != null) {
			data.setLargeTitle(selectedImage.getName());
		}
		data.addTitle("Information");
		data.addData("Delay- " + delay, "Information");

		BufferedImage image = null;
		if (i == null) {
			try {
				image = ImageIO.read(selectedImage);
			} catch (IOException e) {
				progressLabel.setText("An error had occured");
				backButton.setEnabled(true);
				System.out.println("Exception thrown loading image");
			}
		} else {
			image = i;
		}

		if (image == null) {
			progressLabel.setText("An error had occured");
			backButton.setEnabled(true);
			System.out.println("Exception thrown loading image");
			return;
		}
		data.addTitle("Actual Size");

		data.addData("Width - " + image.getWidth(), "Actual Size");
		data.addData("Height - " + image.getHeight(), "Actual Size");

		progressLabel.setText("Resizing image");
		if (image.getHeight() > 300) {
			int scaleFactor = image.getHeight() / 300;
			int height = image.getHeight() / scaleFactor;
			int width = image.getWidth() / scaleFactor;
			image = toBufferedImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH));
		}

		int xS = (int) MouseInfo.getPointerInfo().getLocation().getX();
		int yS = (int) MouseInfo.getPointerInfo().getLocation().getY();

		Robot bot = null;
		try {
			bot = new Robot();
		} catch (AWTException e) {
			System.out.println("Problem creating robot");
			backButton.setEnabled(true);
		}

		HashMap<Integer, Point> totalPoints = new HashMap<Integer, Point>();

		ArrayList<Point> pressList = new ArrayList<Point>();
		ArrayList<Point> releaseList = new ArrayList<Point>();

		progressLabel.setText("Proccessing drawing points...0%");

		for (int y = 0; y < image.getHeight(); y++) {
			boolean lastPress = false;
			for (int x = 0; x < image.getWidth(); x++) {
				int totalLoop = image.getHeight() * image.getWidth();
				int current = (y * image.getWidth()) + x;
				float percent = (current * 100.0f) / totalLoop;
				progressLabel.setText("Proccessing drawing points..." + percent + "%");
				int RGB = image.getRGB(x, y);
				int red = (RGB & 0x00ff0000) >> 16;
				int blue = RGB & 0x000000ff;
				int green = (RGB & 0x0000ff00) >> 8;
				boolean press = false;
				if (red < 240 && green < 240 && blue < 240) {// Not white
					if ((RGB >> 24) != 0x00) {// Not transparent
						press = true;
					}
				}
				if (x != 0) {
					if (lastPress != press) {
						lastPress = press;
						if (press) {
							pressList.add(new Point(x, y));
							totalPoints.put(totalPoints.size() + 1, new Point(x, y));
						} else {
							releaseList.add(new Point(x, y));
							totalPoints.put(totalPoints.size() + 1, new Point(x, y));
						}
					}
				} else {
					lastPress = press;
					if (press) {
						pressList.add(new Point(x, y));
						totalPoints.put(totalPoints.size() + 1, new Point(x, y));
					}
				}
			}
			lastPress = false;
		}

		progressLabel.setText("Drawing image...0%");

		for (int x = 1; x != totalPoints.size(); x++) {
			float percent = (x * 100.0f) / totalPoints.size();
			progressLabel.setText("Drawing image..." + percent + "%");
			Point currentPoint = totalPoints.get(x);
			if (pressList.contains(currentPoint)) {
				bot.delay(delay);
				bot.mouseRelease(InputEvent.BUTTON1_MASK);
			} else if (releaseList.contains(currentPoint)) {
				bot.delay(delay);
				bot.mousePress(InputEvent.BUTTON1_MASK);
			}

			bot.delay(delay);
			int pointX = (int) currentPoint.x;
			int pointY = (int) currentPoint.y;
			bot.mouseMove(xS + pointX, yS + pointY);
		}
		progressLabel.setText("Drawing complete.");
		/*
		 * for (int y = 0; y < image.getHeight(); y++) {; for (int x = 0; x <
		 * image.getWidth(); x++) { bot.delay(5); bot.mouseMove(xS+x, yS+y);
		 * Point p = new Point(x,y); if(pressList.contains(p)){ bot.delay(8);
		 * bot.mousePress(InputEvent.BUTTON1_MASK); }else
		 * if(releaseList.contains(p)){ bot.delay(8);
		 * bot.mouseRelease(InputEvent.BUTTON1_MASK); } } }
		 */

		if (history) {
			int runningTime = AutoDraw.runningTime;
			AutoDraw.history.put(runningTime, data.exportMarkup());
			ImageHData idata = new ImageHData(image, delay);
			imageDrawHistory.put(runningTime, idata);
		}

		bot = null;
		backButton.setEnabled(true);
	}

	public BufferedImage toBufferedImage(Image image) {
		if (!(image instanceof BufferedImage)) {
			BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
					BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = bufferedImage.createGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();
			return bufferedImage;
		} else {
			return (BufferedImage) image;
		}
	}

	public void drawTriangle(double a, double b, double c, double size, boolean history) {
		MarkupSort data = new MarkupSort();
		data.setShape("Triangle");
		data.setLargeTitle("Triangle");
		data.addTitle("Lengths");
		data.addData("a - " + a, "Lengths");
		data.addData("b -" + b, "Lengths");
		data.addData("c - " + c, "Lengths");
		data.addTitle("Size");
		data.addData("Scale - " + size, "Size");
		int x = (int) MouseInfo.getPointerInfo().getLocation().getX();
		int y = (int) MouseInfo.getPointerInfo().getLocation().getY();

		int rA = (int) a;
		int rB = (int) b;
		int rC = (int) c;

		Robot bot = null;
		try {
			bot = new Robot();
		} catch (AWTException e) {
			backButton.setEnabled(true);
			System.out.println("Problem creating robot");
		}

		double angleA = (Math.pow(rB, 2) + Math.pow(rC, 2) - Math.pow(rA, 2));
		angleA = angleA / (2 * rB * rC);
		angleA = StrictMath.acos(angleA);
		angleA = StrictMath.toDegrees(angleA);

		double angleB = (Math.pow(rC, 2) + Math.pow(rA, 2) - Math.pow(rB, 2));
		angleB = angleB / (2 * rC * rA);
		angleB = StrictMath.acos(angleB);
		angleB = StrictMath.toDegrees(angleB);

		double angleC = 180.0 - angleB - angleA;

		data.addTitle("Angles");
		data.addData("A - " + Math.round(angleA), "Angles");
		data.addData("B - " + Math.round(angleB), "Angles");
		data.addData("C - " + Math.round(angleC), "Angles");

		angleB = angleB - (90 - angleC);

		double radianA = angleA * Math.PI / 180;
		double radianB = angleB * Math.PI / 180;
		double radianC = angleC * Math.PI / 180;

		int CaX = (int) (x + (size * a) * Math.cos(radianC));
		int CaY = (int) (y - (size * a) * Math.sin(radianC));

		int BcX = (int) (CaX + (size * c) * Math.sin(radianB));
		int BcY = (int) (CaY + (size * c) * Math.cos(radianB));

		bot.mouseMove((int) (x + (size * b)), y);

		bot.mousePress(InputEvent.BUTTON1_MASK);

		bot.delay(100);
		bot.mouseMove(x, y);

		bot.delay(100);
		bot.mouseMove(CaX, CaY);

		bot.delay(100);
		bot.mouseMove(BcX, BcY);

		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		if (history) {
			int runningTime = AutoDraw.runningTime;
			AutoDraw.history.put(runningTime, data.exportMarkup());
			String redrawData = a + "!" + b + "!" + c + "!" + size;
			triangleDrawHistory.put(runningTime, redrawData);
		}
		backButton.setEnabled(true);
	}

	public void drawRectangle(double width, double height, boolean history) {
		MarkupSort data = new MarkupSort();
		data.setLargeTitle("Rectangle");
		data.setShape("rectangle");
		data.addTitle("Sizes");
		data.addData("Width - " + width, "Sizes");
		data.addData("Height - " + height, "Sizes");

		int x = (int) MouseInfo.getPointerInfo().getLocation().getX();
		int y = (int) MouseInfo.getPointerInfo().getLocation().getY();
		int rW = (int) width;
		int rH = (int) height;
		Robot bot = null;
		try {
			bot = new Robot();
		} catch (AWTException e) {
			System.out.println("Problem creating robot");
		}
		bot.mousePress(InputEvent.BUTTON1_MASK);
		bot.mouseMove(x, y + rH);
		bot.delay(100);
		bot.mouseMove(x + rW, y + rH);
		bot.delay(100);
		bot.mouseMove(x + rW, y);
		bot.delay(100);
		bot.mouseMove(x, y);
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		bot = null;

		if (history) {
			int runningTime = AutoDraw.runningTime;
			AutoDraw.history.put(runningTime, data.exportMarkup());
			String redrawData = width + "!" + height;
			rectangleDrawHistory.put(runningTime, redrawData);
		}
		backButton.setEnabled(true);
	}

	public void drawAngle(double angle, int length, int quadrant, boolean history) {
		MarkupSort data = new MarkupSort();
		data.setLargeTitle("Angle");
		data.setShape("angle");
		data.addTitle("Sizes");
		data.addData("Angle - " + angle, "Sizes");
		data.addData("Length - " + length, "Sizes");
		data.addTitle("Placement");
		data.addData("Quadrant - " + quadrant, "Placement");

		int x = (int) MouseInfo.getPointerInfo().getLocation().getX();
		int y = (int) MouseInfo.getPointerInfo().getLocation().getY();
		Double cAngle = angle * Math.PI / 180;
		Robot bot = null;
		try {
			bot = new Robot();
		} catch (AWTException e) {
			System.out.println("Problem creating robot");
		}
		bot.mousePress(InputEvent.BUTTON1_MASK);
		if (quadrant == 2 || quadrant == 3) {
			bot.mouseMove(x + length, y);
		} else if (quadrant == 1 || quadrant == 4) {
			bot.mouseMove(x - length, y);
		}
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		Double drawCordX = 0.0;
		Double drawCordY = 0.0;
		if (quadrant == 1) {
			drawCordX = (x - length) + length * Math.cos(cAngle);
			drawCordY = y - length * Math.sin(cAngle);
		} else if (quadrant == 2) {
			drawCordX = (x + length) - length * Math.cos(cAngle);
			drawCordY = y - length * Math.sin(cAngle);
		} else if (quadrant == 3) {
			drawCordX = (x + length) - length * Math.cos(cAngle);
			drawCordY = y + length * Math.sin(cAngle);
		} else if (quadrant == 4) {
			drawCordX = (x - length) + length * Math.cos(cAngle);
			drawCordY = y + length * Math.sin(cAngle);
		}
		bot.mousePress(InputEvent.BUTTON1_MASK);
		bot.mouseMove(drawCordX.intValue(), drawCordY.intValue());
		bot.mouseRelease(InputEvent.BUTTON1_MASK);
		bot = null;

		if (history) {
			int runningTime = AutoDraw.runningTime;
			AutoDraw.history.put(runningTime, data.exportMarkup());
			String redrawData = angle + "!" + length + "!" + quadrant;
			angleDrawHistory.put(runningTime, redrawData);
		}
		backButton.setEnabled(true);
	}
}

package bugpatch.master;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class DrawGUI extends JFrame {

	public JPanel jp;

	public DrawFunction df;

	public JFrame currentFrame;

	public JPanel statusJP;

	public JLabel progressLabel;

	public boolean isStatusPanel = false;

	public JButton backButton;

	public boolean dialogeOpen = false;

	public DrawGUI() {
		super("AutoDraw");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 80);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		setLocation((int) width / 5, (int) height / 5);
		setAlwaysOnTop(true);
		setResizable(false);
		jp = new JPanel(new FlowLayout());
		Color c = new Color(38, 78, 92);
		jp.setBackground(c);
		jp.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		jp.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				bringCorrectFrameToFront();
			}
		});
		add(jp);
		statusJP = new JPanel(new FlowLayout());
		statusJP.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		progressLabel = new JLabel("");
		backButton = new JButton("Back");
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				disableStatusPanel();
			}
		});
		statusJP.add(progressLabel);
		statusJP.add(backButton);
		loadAndPrepareComponents();
		df = new DrawFunction(backButton);
		setVisible(true);
	}

	public void bringCorrectFrameToFront() {
		if (dialogeOpen) {
			if (currentFrame != null) {
				currentFrame.toFront();
				currentFrame.repaint();
			}
		}
	}

	public void disableMainFrame() {
		if (!dialogeOpen) {
			for (Component c : jp.getComponents()) {
				c.setEnabled(false);
				c.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						bringCorrectFrameToFront();
					}
				});
			}
			setFocusable(false);
			dialogeOpen = true;
		}
	}

	public void enableMainFrame() {
		if (dialogeOpen) {
			for (Component c : jp.getComponents()) {
				c.setEnabled(true);
			}
			setFocusable(true);
			dialogeOpen = false;
		}
	}

	public void enableStatusPanel() {
		if (!isStatusPanel) {
			this.remove(jp);
			jp.revalidate();
			jp.repaint();
			this.add(statusJP);
			progressLabel.setText("");
			jp.revalidate();
			jp.repaint();
			isStatusPanel = true;
		}
	}

	public void disableStatusPanel() {
		if (isStatusPanel) {
			this.remove(statusJP);
			jp.revalidate();
			jp.repaint();
			this.add(jp);
			jp.revalidate();
			jp.repaint();
			isStatusPanel = false;
		}
	}

	public void loadAndPrepareComponents() {
		JButton circleButton = new JButton("○");
		circleButton = configureBarButton(circleButton);
		jp.add(circleButton);
		circleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!dialogeOpen) {
					circleDialogue();
				}
			}
		});

		JButton angleButton = new JButton("∠");
		angleButton = configureBarButton(angleButton);
		jp.add(angleButton);
		angleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!dialogeOpen) {
					angleDialogue();
				}
			}
		});

		JButton triangleButton = new JButton("Δ");
		triangleButton = configureBarButton(triangleButton);
		jp.add(triangleButton);
		triangleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!dialogeOpen) {
					triangleDialogue();
				}
			}
		});

		JButton rectangleButton = new JButton("□");
		rectangleButton = configureBarButton(rectangleButton);
		jp.add(rectangleButton);
		rectangleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!dialogeOpen) {
					rectangleDialogue();
				}
			}
		});

		JButton imageButton = new JButton("Load Image");
		imageButton = configureBarButton(imageButton);
		jp.add(imageButton);
		imageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!dialogeOpen) {
					imageDialogue();
				}
			}
		});

		JButton historyButton = new JButton("History");
		historyButton = configureBarButton(historyButton);
		jp.add(historyButton);
		historyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!dialogeOpen) {
					historyDialogue();
				}
			}
		});

		JButton informationButton = new JButton("i");
		informationButton = configureBarButton(informationButton);
		jp.add(informationButton);
		informationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame info = new JFrame();
				info.setAlwaysOnTop(true);
				info.setResizable(false);
				info.setLayout(new GridLayout(3, 3));
				JLabel credit = new JLabel();
				credit.setText("<html>Developed by Andre Ruegg</html>");
				JLabel version = new JLabel();
				version.setText("<html>Beta Version 0.02</html>");
				info.add(credit);
				info.add(version);
				info.pack();
				info.setVisible(true);
			}
		});
	}

	public void imageDialogue() {
		disableMainFrame();
		JFrame imageD = new JFrame();
		currentFrame = imageD;
		imageD.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				enableMainFrame();
			}
		});
		imageD.setAlwaysOnTop(true);
		imageD.setResizable(false);
		imageD.setLocation(AutoDraw.drawGUI.getX(), AutoDraw.drawGUI.getY());
		imageD.setLayout(new GridLayout(2, 2));
		JLabel delayLabel = new JLabel("Delay");
		JTextArea delayInput = new JTextArea();
		delayInput.setText("5");
		imageD.add(delayLabel);
		imageD.add(delayInput);
		imageD.add(new JLabel(" "));
		JButton drawimage = new JButton("Draw Image");
		drawimage.setBackground(Color.DARK_GRAY);
		drawimage.setForeground(Color.WHITE);
		imageD.add(drawimage);
		drawimage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isDouble(delayInput.getText())) {
					int delay = Integer.parseInt(delayInput.getText());
					JFileChooser imageChooser = new JFileChooser();
					int fileChosen = imageChooser.showOpenDialog(null);
					if (fileChosen == JFileChooser.APPROVE_OPTION) {
						imageD.dispose();
						enableMainFrame();
						File selectedImage = imageChooser.getSelectedFile();

						if (!AutoDraw.isDrawing) {
							enableStatusPanel();
							drawWait(5);
							new java.util.Timer().schedule(new java.util.TimerTask() {
								@Override
								public void run() {
									df.drawImage(progressLabel, selectedImage, delay, null, true);
								}
							}, 5000);
						} else {
							JOptionPane.showMessageDialog(null, "Program is still drawing", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {
					imageD.dispose();
					enableMainFrame();
					JOptionPane.showMessageDialog(null, "Only numbers should be put into the textboxes", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		imageD.pack();
		imageD.setVisible(true);
	}

	public void circleDialogue() {
		disableMainFrame();
		JFrame circleD = new JFrame();
		currentFrame = circleD;
		circleD.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				enableMainFrame();
			}
		});
		circleD.setAlwaysOnTop(true);
		circleD.setResizable(false);
		circleD.setLocation(AutoDraw.drawGUI.getX(), AutoDraw.drawGUI.getY());
		circleD.setLayout(new GridLayout(5, 2));
		JLabel radiusLabel = new JLabel("Radius");
		JTextArea radiusInput = new JTextArea();
		circleD.add(radiusLabel);
		circleD.add(radiusInput);
		circleD.add(new JLabel(" "));
		circleD.add(new JLabel(" "));
		JLabel scaleLabel = new JLabel("Scale");
		JTextArea scaleInput = new JTextArea();
		circleD.add(scaleLabel);
		circleD.add(scaleInput);
		circleD.add(new JLabel(" "));
		circleD.add(new JLabel(" "));
		circleD.add(new JLabel(" "));
		JButton drawcircle = new JButton("Draw");
		drawcircle.setBackground(Color.DARK_GRAY);
		drawcircle.setForeground(Color.WHITE);
		circleD.add(drawcircle);
		drawcircle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isDouble(radiusInput.getText()) && isDouble(scaleInput.getText())) {
					circleD.dispose();
					enableMainFrame();
					if (!AutoDraw.isDrawing) {
						enableStatusPanel();
						drawWait(5);
						new java.util.Timer().schedule(new java.util.TimerTask() {
							@Override
							public void run() {
								df.drawCircle(Double.valueOf(scaleInput.getText()),
										Integer.valueOf(radiusInput.getText()), true);
							}
						}, 5000);
					} else {
						JOptionPane.showMessageDialog(null, "Program is still drawing", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					circleD.dispose();
					enableMainFrame();
					JOptionPane.showMessageDialog(null, "Only numbers should be put into the textboxes", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		circleD.pack();
		circleD.setVisible(true);
	}

	public void historyDialogue() {
		disableMainFrame();
		int opened = AutoDraw.runningTime;
		JFrame historyD = new JFrame();
		currentFrame = historyD;
		historyD.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				enableMainFrame();
			}
		});
		historyD.setAlwaysOnTop(true);
		historyD.setResizable(false);
		historyD.setLocation(AutoDraw.drawGUI.getX(), AutoDraw.drawGUI.getY());
		historyD.setLayout(new BorderLayout());
		List<String> list = new ArrayList<String>();
		for (Entry<Integer, String> e : AutoDraw.history.entrySet()) {
			String data = e.getValue();
			String[] splitData = data.split("!");
			String typeOfShape = splitData[0];
			int secondsAgo = AutoDraw.runningTime - e.getKey();
			String full = "Drawn " + secondsAgo + " seconds ago - " + typeOfShape;
			list.add(full);
		}
		String[] listArray = list.toArray(new String[list.size()]);
		JList historyL = new JList(listArray);
		historyD.add(new JScrollPane(historyL), BorderLayout.CENTER);

		JButton detailsButton = new JButton("Details");
		detailsButton.setBackground(Color.DARK_GRAY);
		detailsButton.setForeground(Color.WHITE);
		historyD.add(detailsButton, BorderLayout.SOUTH);

		detailsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (list.size() > 0) {
					String value = (String) historyL.getSelectedValue();
					String sub1 = value.substring(6, value.length());
					String[] split = sub1.split(" ");
					final int secondStamp = opened - Integer.valueOf(split[0]);
					String data = AutoDraw.history.get(secondStamp);
					String[] dataSplit = data.split("!");
					String markup = dataSplit[1];

					JFrame details = new JFrame();
					details.setAlwaysOnTop(true);
					details.setResizable(false);

					JLabel markupData = new JLabel(markup);
					details.add(markupData, BorderLayout.CENTER);

					JButton redrawButton = new JButton("Redraw");
					redrawButton.setBackground(Color.DARK_GRAY);
					redrawButton.setForeground(Color.WHITE);
					redrawButton.setFocusable(false);
					details.add(redrawButton, BorderLayout.SOUTH);

					redrawButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {

							if (dataSplit[0].equalsIgnoreCase("circle")) {
								String redrawData = df.circleDrawHistory.get(secondStamp);
								String[] splitData = redrawData.split("!");
								double radius = Double.parseDouble(splitData[0]);
								int scale = Integer.parseInt(splitData[1]);
								details.dispose();
								enableMainFrame();
								if (!AutoDraw.isDrawing) {
									enableStatusPanel();
									drawWait(5);
									new java.util.Timer().schedule(new java.util.TimerTask() {
										@Override
										public void run() {
											df.drawCircle(radius, scale, false);
										}
									}, 5000);
								} else {
									JOptionPane.showMessageDialog(null, "Program is still drawing", "Error",
											JOptionPane.ERROR_MESSAGE);
								}
							} else if (dataSplit[0].equalsIgnoreCase("triangle")) {
								String redrawData = df.triangleDrawHistory.get(secondStamp);
								String[] splitData = redrawData.split("!");
								double a = Double.parseDouble(splitData[0]);
								double b = Double.parseDouble(splitData[1]);
								double c = Double.parseDouble(splitData[2]);
								double size = Double.parseDouble(splitData[3]);
								details.dispose();
								enableMainFrame();
								if (!AutoDraw.isDrawing) {
									drawWait(5);
									enableStatusPanel();
									new java.util.Timer().schedule(new java.util.TimerTask() {
										@Override
										public void run() {
											df.drawTriangle(a, b, c, size, false);
										}
									}, 5000);
								} else {
									JOptionPane.showMessageDialog(null, "Program is still drawing", "Error",
											JOptionPane.ERROR_MESSAGE);
								}
							} else if (dataSplit[0].equalsIgnoreCase("image")) {
								ImageHData idata = df.imageDrawHistory.get(secondStamp);
								if (!AutoDraw.isDrawing) {
									enableStatusPanel();
									drawWait(5);
									new java.util.Timer().schedule(new java.util.TimerTask() {
										@Override
										public void run() {
											df.drawImage(progressLabel, null, idata.getDelay(), idata.getImage(),
													false);
										}
									}, 5000);
								} else {
									JOptionPane.showMessageDialog(null, "Program is still drawing", "Error",
											JOptionPane.ERROR_MESSAGE);
								}
							} else if (dataSplit[0].equalsIgnoreCase("rectangle")) {
								String redrawData = df.rectangleDrawHistory.get(secondStamp);
								String[] splitData = redrawData.split("!");
								double width = Double.parseDouble(splitData[0]);
								double height = Double.parseDouble(splitData[1]);
								if (!AutoDraw.isDrawing) {
									enableStatusPanel();
									drawWait(5);
									new java.util.Timer().schedule(new java.util.TimerTask() {
										@Override
										public void run() {
											df.drawRectangle(width, height, false);
										}
									}, 5000);
								} else {
									JOptionPane.showMessageDialog(null, "Program is still drawing", "Error",
											JOptionPane.ERROR_MESSAGE);
								}
							} else if (dataSplit[0].equalsIgnoreCase("angle")) {
								String redrawData = df.triangleDrawHistory.get(secondStamp);
								String[] splitData = redrawData.split("!");
								Double angle = Double.parseDouble(splitData[0]);
								int length = Integer.parseInt(splitData[1]);
								int quadrant = Integer.parseInt(splitData[2]);
								if (!AutoDraw.isDrawing) {
									enableStatusPanel();
									drawWait(5);
									new java.util.Timer().schedule(new java.util.TimerTask() {
										@Override
										public void run() {
											df.drawAngle(angle, length, quadrant, false);
										}
									}, 5000);
								} else {
									JOptionPane.showMessageDialog(null, "Program is still drawing", "Error",
											JOptionPane.ERROR_MESSAGE);
								}
							}
						}
					});

					details.pack();
					details.setVisible(true);
				} else {
					JOptionPane.showMessageDialog(null, "There are no entries to select", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		historyD.pack();
		historyD.setVisible(true);
	}

	public void triangleDialogue() {
		disableMainFrame();
		JFrame triangleD = new JFrame();
		currentFrame = triangleD;
		triangleD.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				enableMainFrame();
			}
		});
		triangleD.setAlwaysOnTop(true);
		triangleD.setResizable(false);
		triangleD.setLocation(AutoDraw.drawGUI.getX(), AutoDraw.drawGUI.getY());
		triangleD.setLayout(new GridLayout(10, 2));
		JLabel oppositeLabel = new JLabel("Opposite(a)");
		JTextArea oppositeInput = new JTextArea();
		triangleD.add(oppositeLabel);
		triangleD.add(oppositeInput);
		triangleD.add(new JLabel(" "));
		triangleD.add(new JLabel(" "));
		JLabel adjacentLabel = new JLabel("Adjacent(b)");
		JTextArea adjacentInput = new JTextArea();
		triangleD.add(adjacentLabel);
		triangleD.add(adjacentInput);
		triangleD.add(new JLabel(" "));
		triangleD.add(new JLabel(" "));
		JLabel hypotenuseLabel = new JLabel("Hypotenuse(c)");
		JTextArea hypotenuseInput = new JTextArea();
		triangleD.add(hypotenuseLabel);
		triangleD.add(hypotenuseInput);
		triangleD.add(new JLabel(" "));
		triangleD.add(new JLabel(" "));
		JLabel sizeLabel = new JLabel("Scale size(px)");
		JTextArea sizeInput = new JTextArea();
		triangleD.add(sizeLabel);
		triangleD.add(sizeInput);
		triangleD.add(new JLabel(" "));
		triangleD.add(new JLabel(" "));
		triangleD.add(new JLabel(" "));
		JButton drawtriangle = new JButton("Draw");
		drawtriangle.setBackground(Color.DARK_GRAY);
		drawtriangle.setForeground(Color.WHITE);
		triangleD.add(drawtriangle);
		drawtriangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isDouble(oppositeInput.getText()) && isDouble(adjacentInput.getText())
						&& isDouble(hypotenuseInput.getText()) && isDouble(sizeInput.getText())) {
					double opposite = Double.valueOf(oppositeInput.getText());
					double adjacent = Double.valueOf(adjacentInput.getText());
					double hypotenuse = Double.valueOf(hypotenuseInput.getText());
					if ((opposite + adjacent) > hypotenuse && (adjacent + hypotenuse) > opposite
							&& (opposite + hypotenuse) > adjacent) {
						triangleD.dispose();
						enableMainFrame();
						if (!AutoDraw.isDrawing) {
							enableStatusPanel();
							drawWait(5);
							new java.util.Timer().schedule(new java.util.TimerTask() {
								@Override
								public void run() {
									df.drawTriangle(opposite, adjacent, hypotenuse, Double.valueOf(sizeInput.getText()),
											true);
								}
							}, 5000);
						} else {
							JOptionPane.showMessageDialog(null, "Program is still drawing", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null,
								"Incorrect triangle, any sum of two sides must be greater than the last side", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					triangleD.dispose();
					enableMainFrame();
					JOptionPane.showMessageDialog(null, "Only numbers should be put into the textboxes", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		triangleD.pack();
		triangleD.setVisible(true);
	}

	public void rectangleDialogue() {
		disableMainFrame();
		JFrame rectangleD = new JFrame();
		currentFrame = rectangleD;
		rectangleD.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				enableMainFrame();
			}
		});
		rectangleD.setAlwaysOnTop(true);
		rectangleD.setResizable(false);
		rectangleD.setLocation(AutoDraw.drawGUI.getX(), AutoDraw.drawGUI.getY());
		rectangleD.setLayout(new GridLayout(5, 2));
		JLabel widthLabel = new JLabel("Width");
		JTextArea widthInput = new JTextArea();
		rectangleD.add(widthLabel);
		rectangleD.add(widthInput);
		rectangleD.add(new JLabel(" "));
		rectangleD.add(new JLabel(" "));
		JLabel heightLabel = new JLabel("height");
		JTextArea heightInput = new JTextArea();
		rectangleD.add(heightLabel);
		rectangleD.add(heightInput);
		rectangleD.add(new JLabel(" "));
		rectangleD.add(new JLabel(" "));
		rectangleD.add(new JLabel(" "));
		JButton drawrectangle = new JButton("Draw");
		drawrectangle.setBackground(Color.DARK_GRAY);
		drawrectangle.setForeground(Color.WHITE);
		rectangleD.add(drawrectangle);
		drawrectangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isDouble(widthInput.getText()) && isDouble(heightInput.getText())) {
					rectangleD.dispose();
					enableMainFrame();
					if (!AutoDraw.isDrawing) {
						enableStatusPanel();
						drawWait(5);
						new java.util.Timer().schedule(new java.util.TimerTask() {
							@Override
							public void run() {
								df.drawRectangle(Double.valueOf(widthInput.getText()),
										Double.valueOf(heightInput.getText()), true);
							}
						}, 5000);
					} else {
						JOptionPane.showMessageDialog(null, "Program is still drawing", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					rectangleD.dispose();
					enableMainFrame();
					JOptionPane.showMessageDialog(null, "Only numbers should be put into the textboxes", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		rectangleD.pack();
		rectangleD.setVisible(true);
	}

	public void angleDialogue() {
		disableMainFrame();
		JFrame angleD = new JFrame();
		currentFrame = angleD;
		angleD.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				enableMainFrame();
			}
		});
		angleD.setAlwaysOnTop(true);
		angleD.setResizable(false);
		angleD.setLocation(AutoDraw.drawGUI.getX(), AutoDraw.drawGUI.getY());
		angleD.setLayout(new GridLayout(8, 2));
		JLabel angleLabel = new JLabel("Angle°");
		JTextArea angleInput = new JTextArea();
		angleD.add(angleLabel);
		angleD.add(angleInput);
		angleD.add(new JLabel(" "));
		angleD.add(new JLabel(" "));
		JLabel lengthLabel = new JLabel("Length(px)");
		JTextArea lengthInput = new JTextArea();
		angleD.add(lengthLabel);
		angleD.add(lengthInput);
		angleD.add(new JLabel(" "));
		angleD.add(new JLabel(" "));
		JLabel quadrantLabel = new JLabel("Quadrant");
		String[] quadrantStrings = { "1", "2", "3", "4" };
		JComboBox quadrantInput = new JComboBox(quadrantStrings);
		angleD.add(quadrantLabel);
		angleD.add(quadrantInput);
		angleD.add(new JLabel(" "));
		angleD.add(new JLabel(" "));
		angleD.add(new JLabel(" "));
		JButton drawAngle = new JButton("Draw");
		drawAngle.setBackground(Color.DARK_GRAY);
		drawAngle.setForeground(Color.WHITE);
		angleD.add(drawAngle);
		drawAngle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isDouble(angleInput.getText()) && isDouble(lengthInput.getText())) {
					angleD.dispose();
					enableMainFrame();
					if (!AutoDraw.isDrawing) {
						enableStatusPanel();
						drawWait(5);
						new java.util.Timer().schedule(new java.util.TimerTask() {
							@Override
							public void run() {
								df.drawAngle(Integer.valueOf(angleInput.getText()),
										Integer.valueOf(lengthInput.getText()), quadrantInput.getSelectedIndex() + 1,
										true);
							}
						}, 5000);
					} else {
						JOptionPane.showMessageDialog(null, "Program is still drawing", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					angleD.dispose();
					enableMainFrame();
					JOptionPane.showMessageDialog(null, "Only numbers should be put into the textboxes", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		angleD.pack();
		angleD.setVisible(true);
	}

	public void drawWait(int seconds) {
		backButton.setVisible(false);
		progressLabel.setText("<html>Please position your mouse<br>Drawing in " + (seconds + 1) + "</html>");
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				String currentTimeText = progressLabel.getText();
				int begin = currentTimeText.indexOf("Drawing in ") + 11;
				int end = currentTimeText.length() - 7;
				int current = Integer.valueOf(currentTimeText.substring(begin, end));
				progressLabel.setText("<html>Please position your mouse<br>Drawing in " + (current - 1) + "</html>");
				if (current == 1) {
					t.cancel();
					backButton.setVisible(true);
					backButton.setEnabled(false);
				}
			}
		}, 0, 1000);
	}

	public boolean isDouble(String s) {
		try {
			Double.valueOf(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public JButton configureBarButton(JButton b) {
		int red = 52;
		int green = 109;
		int blue = 128;
		Color c = new Color(red, green, blue);
		b.setBackground(c);
		b.setFocusable(false);
		b.setForeground(Color.WHITE);
		return b;
	}
}

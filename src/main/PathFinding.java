package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.Timer;

public class PathFinding extends JPanel implements ActionListener, MouseListener {
	private static final long serialVersionUID = -6729330960826818866L;

	class CellDetails {
		int parentI, parentJ;
		double f, g, h;
	};

	class Pair {
		int first, second;

		public Pair(int first, int second) {
			this.first = first;
			this.second = second;
		}
	};

	class pPair {
		double db;
		Pair pair;

		public pPair(double db, Pair pair) {
			this.db = db;
			this.pair = pair;
		}
	};

	private final int WIDTH;
	private final int HEIGHT;

	private Timer timer;
	private int delay = 5;

	private final int squareWidth = 25;
	private final int squareHeight = 25;

	public MapGenerator squaresMap;

	private boolean startSelected = false;
	private boolean targetSelected = false;
	private boolean waitForInput = false;

	public static boolean selectStart = false;
	private int startX = 0;
	private int startY = 0;

	public static boolean selectTarget = false;
	private int targetX = 0;
	private int targetY = 0;

	public static boolean clearMap = false;

	private static boolean startAStar = false;

	SelectButton selectButton;
	TargetButton targetButton;
	StartAStarButton startAStarButton;
	ClearButton clearButton;

	public PathFinding(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.setLayout(null);
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		squaresMap = new MapGenerator((HEIGHT - 10) / squareHeight, (WIDTH - 425) / squareWidth, squareWidth,
				squareHeight);
		timer = new Timer(delay, this);
		timer.start();
		this.addMouseListener(this);

		selectButton = new SelectButton();
		selectButton.setBounds(980 + 10 + squareWidth, HEIGHT - 8 - squareHeight * 12, squareWidth * 14, squareHeight);
		this.add(selectButton);

		targetButton = new TargetButton();
		targetButton.setBounds(980 + 10 + squareWidth, HEIGHT - 8 - squareHeight * 14, squareWidth * 14, squareHeight);
		this.add(targetButton);

		startAStarButton = new StartAStarButton();
		startAStarButton.setBounds(980 + 10 + squareWidth, HEIGHT - 8 - squareHeight * 16, squareWidth * 14,
				squareHeight);
		this.add(startAStarButton);

		clearButton = new ClearButton();
		clearButton.setBounds(980 + 10 + squareWidth, HEIGHT - 8 - squareHeight * 18, squareWidth * 14, squareHeight);
		this.add(clearButton);
	}

	@Override
	public void paint(Graphics g) {
		// background
		g.setColor(Color.gray);
		g.fillRect(0, 0, WIDTH, HEIGHT);

		// border
		g.setColor(new Color(173, 216, 230));
		g.fillRect(0, 0, 5, HEIGHT);
		g.fillRect(0, 0, WIDTH, 5);
		g.fillRect(WIDTH - 5, 0, 5, HEIGHT);
		g.fillRect(0, HEIGHT - 5, WIDTH, 5);

		// middle line
		g.setColor(new Color(173, 216, 230));
		g.fillRect(980 + 1, 0, 5, HEIGHT);

		// drawing map
		squaresMap.draw((Graphics2D) g);

		// right side info
		g.setColor(Color.white);
		g.fillRect(980 + 10 + squareWidth, HEIGHT - 5 - squareHeight * 2, squareWidth, squareHeight);
		g.setColor(Color.black);
		g.setFont(new Font("serif", Font.BOLD, 35));
		g.drawString(" - UNVISITED NODE", 980 + 10 + squareWidth * 2, HEIGHT - 5 - squareHeight);

		g.setColor(Color.green);
		g.fillRect(980 + 10 + squareWidth, HEIGHT - 5 - squareHeight * 4, squareWidth, squareHeight);
		g.setColor(Color.black);
		g.setFont(new Font("serif", Font.BOLD, 35));
		g.drawString(" - START NODE", 980 + 10 + squareWidth * 2, HEIGHT - 5 - squareHeight * 3);

		g.setColor(Color.red);
		g.fillRect(980 + 10 + squareWidth, HEIGHT - 5 - squareHeight * 6, squareWidth, squareHeight);
		g.setColor(Color.black);
		g.setFont(new Font("serif", Font.BOLD, 35));
		g.drawString(" - TARGET NODE", 980 + 10 + squareWidth * 2, HEIGHT - 5 - squareHeight * 5);

		g.setColor(Color.black);
		g.fillRect(980 + 10 + squareWidth, HEIGHT - 5 - squareHeight * 8, squareWidth, squareHeight);
		g.setColor(Color.black);
		g.setFont(new Font("serif", Font.BOLD, 35));
		g.drawString(" - BORDER", 980 + 10 + squareWidth * 2, HEIGHT - 5 - squareHeight * 7);

		g.setColor(Color.yellow);
		g.fillRect(980 + 10 + squareWidth, HEIGHT - 5 - squareHeight * 10, squareWidth, squareHeight);
		g.setColor(Color.black);
		g.setFont(new Font("serif", Font.BOLD, 35));
		g.drawString(" - PATH NODE", 980 + 10 + squareWidth * 2, HEIGHT - 5 - squareHeight * 9);

		g.setColor(new Color(173, 216, 230));
		g.fillRect(980 + 10 + squareWidth, HEIGHT - 5 - squareHeight * 12, squareWidth * 14, squareHeight - 4);
		g.fillRect(980 + 10 + squareWidth, HEIGHT - 5 - squareHeight * 14, squareWidth * 14, squareHeight - 4);
		g.fillRect(980 + 10 + squareWidth, HEIGHT - 5 - squareHeight * 16, squareWidth * 14, squareHeight - 4);
		g.fillRect(980 + 10 + squareWidth, HEIGHT - 5 - squareHeight * 18, squareWidth * 14, squareHeight - 4);

		g.dispose();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if (selectStart == true) {
			selectStart = false;
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int mouseX = e.getX();
					int mouseY = e.getY();

					for (int i = 0; i < (HEIGHT - 10) / squareHeight; i++) {
						for (int j = 0; j < (WIDTH - 425) / squareWidth; j++) {
							int squareX = j * squareHeight + 5;
							int squareY = i * squareWidth + 5;

							Rectangle square = new Rectangle(squareX, squareY, squareWidth, squareHeight);
							Rectangle mouseSquare = new Rectangle(mouseX, mouseY, 1, 1);

							if (square.intersects(mouseSquare)) {
								squaresMap.setSquareState(1, startX, startY);
								squaresMap.setSquareState(3, i, j);
								startSelected = true;
								startX = i;
								startY = j;
								i = squaresMap.map.length + 1;
								j = squaresMap.map[0].length + 1;
								repaint();
								revalidate();
							}
						}
					}
					removeMouseListener(this);
				}
			});
		}

		if (selectTarget == true) {
			selectTarget = false;
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int mouseX = e.getX();
					int mouseY = e.getY();

					for (int i = 0; i < (HEIGHT - 10) / squareHeight; i++) {
						for (int j = 0; j < (WIDTH - 425) / squareWidth; j++) {
							int squareX = j * squareHeight + 5;
							int squareY = i * squareWidth + 5;

							Rectangle square = new Rectangle(squareX, squareY, squareWidth, squareHeight);
							Rectangle mouseSquare = new Rectangle(mouseX, mouseY, 1, 1);

							if (square.intersects(mouseSquare)) {
								squaresMap.setSquareState(1, targetX, targetY);
								squaresMap.setSquareState(4, i, j);
								targetSelected = true;
								targetX = i;
								targetY = j;
								i = squaresMap.map.length + 1;
								j = squaresMap.map[0].length + 1;
								repaint();
								revalidate();
							}
						}
					}
					removeMouseListener(this);
				}
			});
		}
		if (startSelected && targetSelected) {
			waitForInput = true;
		}
		if (startAStar) {
			aStarSearch(squaresMap, startX, startY, targetX, targetY);
			startAStar = false;
		}
		if (clearMap) {
			for (int i = 0; i < squaresMap.map.length; i++)
				for (int j = 0; j < squaresMap.map[0].length; j++)
					squaresMap.setSquareState(MapGenerator.WHITESPACE, i, j);
			repaint();
			clearMap = false;
		}
	}

	public static void setSelect(boolean test) {
		selectStart = test;
	}

	public static boolean isSelect() {
		return selectStart;
	}

	public static void setSelectTarget(boolean test) {
		selectTarget = test;
	}

	public static boolean isSelectTarget() {
		return selectTarget;
	}

	public static void setStartAStar(boolean startAStar) {
		PathFinding.startAStar = startAStar;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (waitForInput) {
			waitForInput = false;
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					int mouseX = e.getX();
					int mouseY = e.getY();

					int squareI = 0;
					int squareJ = 0;

					if ((mouseX > 5 && mouseX < 980) && (mouseY > 5 && mouseY < 830)) {
						for (int i = 0; i < (HEIGHT - 10) / squareHeight; i++) {
							for (int j = 0; j < (WIDTH - 425) / squareWidth; j++) {
								int squareX = j * squareHeight + 5;
								int squareY = i * squareWidth + 5;

								Rectangle square = new Rectangle(squareX, squareY, squareWidth, squareHeight);
								Rectangle mouseSquare = new Rectangle(mouseX, mouseY, 1, 1);

								if (square.intersects(mouseSquare)) {
									squareI = i;
									squareJ = j;
								}
							}
						}
						if (squaresMap.map[squareI][squareJ] == MapGenerator.BORDER) {
							squaresMap.setSquareState(MapGenerator.WHITESPACE, squareI, squareJ);
							waitForInput = true;
							repaint();
							revalidate();
						} else if (squaresMap.map[squareI][squareJ] == MapGenerator.WHITESPACE) {
							squaresMap.setSquareState(MapGenerator.BORDER, squareI, squareJ);
							waitForInput = true;
							repaint();
							revalidate();
						}
					}

					removeMouseListener(this);
				}
			});
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	private void aStarSearch(MapGenerator squaresMap, int startX, int startY, int targetX, int targetY) {

		final int ROW = squaresMap.map.length;
		final int COL = squaresMap.map[0].length;
		boolean[][] closedList = new boolean[ROW][COL];
		CellDetails[][] cellDetails = new CellDetails[ROW][COL];

		for (int i = 0; i < ROW; i++) {
			for (int j = 0; j < COL; j++) {
				cellDetails[i][j] = new CellDetails();
			}
		}

		int i, j;

		for (i = 0; i < ROW; i++) {
			for (j = 0; j < COL; j++) {
				cellDetails[i][j].f = Double.MAX_VALUE;
				cellDetails[i][j].g = Double.MAX_VALUE;
				cellDetails[i][j].h = Double.MAX_VALUE;
				cellDetails[i][j].parentI = -1;
				cellDetails[i][j].parentJ = -1;
			}
		}

		// Initializare nod start
		i = startX;
		j = startY;
		// System.out.println(startX + " " + startY + " " + targetX + " " + targetY);
		cellDetails[i][j].f = 0.0;
		cellDetails[i][j].g = 0.0;
		cellDetails[i][j].h = 0.0;
		cellDetails[i][j].parentI = i;
		cellDetails[i][j].parentJ = j;

		ArrayList<pPair> openList = new ArrayList<>();

		openList.add(new pPair(0.0, new Pair(i, j)));

		System.out.println(openList.get(0));

		boolean foundDest = false;

		while (!openList.isEmpty()) {
			pPair pair = openList.get(0);
			openList.remove(0);

			i = pair.pair.first;
			j = pair.pair.second;

			closedList[i][j] = true;

			double gNew, hNew, fNew;

			// 1st Successor NORTH

			if (isValid(i - 1, j, ROW, COL) == true) {
				if (isDestionation(i - 1, j, targetX, targetY) == true) {
					cellDetails[i - 1][j].parentI = i;
					cellDetails[i - 1][j].parentJ = j;
					tracePath(cellDetails, targetX, targetY);
					foundDest = true;
					return;
				} else if (closedList[i - 1][j] == false && squaresMap.map[i - 1][j] != MapGenerator.BORDER) {
					gNew = cellDetails[i][j].g + 1.0;
					hNew = calculateHValue(i - 1, j, targetX, targetY);
					fNew = gNew + hNew;

					if (cellDetails[i - 1][j].f == Double.MAX_VALUE || cellDetails[i - 1][j].f > fNew) {
						// squaresMap.setSquareState(MapGenerator.VISITED, i - 1, j);
						openList.add(new pPair(fNew, new Pair(i - 1, j)));

						cellDetails[i - 1][j].f = fNew;
						cellDetails[i - 1][j].g = gNew;
						cellDetails[i - 1][j].h = hNew;
						cellDetails[i - 1][j].parentI = i;
						cellDetails[i - 1][j].parentJ = j;
						// repaint();
					}
				}
			}

			// 2nd Successor SOUTH

			if (isValid(i + 1, j, ROW, COL) == true) {
				if (isDestionation(i + 1, j, targetX, targetY) == true) {
					cellDetails[i + 1][j].parentI = i;
					cellDetails[i + 1][j].parentJ = j;
					tracePath(cellDetails, targetX, targetY);
					foundDest = true;
					return;
				} else if (closedList[i + 1][j] == false && squaresMap.map[i + 1][j] != MapGenerator.BORDER) {
					gNew = cellDetails[i][j].g + 1.0;
					hNew = calculateHValue(i + 1, j, targetX, targetY);
					fNew = gNew + hNew;

					if (cellDetails[i + 1][j].f == Double.MAX_VALUE || cellDetails[i + 1][j].f > fNew) {
						// squaresMap.setSquareState(MapGenerator.VISITED, i + 1, j);
						openList.add(new pPair(fNew, new Pair(i + 1, j)));

						cellDetails[i + 1][j].f = fNew;
						cellDetails[i + 1][j].g = gNew;
						cellDetails[i + 1][j].h = hNew;
						cellDetails[i + 1][j].parentI = i;
						cellDetails[i + 1][j].parentJ = j;
						// repaint();
					}
				}
			}

			// 3rd Successor EAST

			if (isValid(i, j + 1, ROW, COL) == true) {
				if (isDestionation(i, j + 1, targetX, targetY) == true) {
					cellDetails[i][j + 1].parentI = i;
					cellDetails[i][j + 1].parentJ = j;
					tracePath(cellDetails, targetX, targetY);
					foundDest = true;
					return;
				} else if (closedList[i][j + 1] == false && squaresMap.map[i][j + 1] != MapGenerator.BORDER) {
					gNew = cellDetails[i][j].g + 1.0;
					hNew = calculateHValue(i, j + 1, targetX, targetY);
					fNew = gNew + hNew;

					if (cellDetails[i][j + 1].f == Double.MAX_VALUE || cellDetails[i][j + 1].f > fNew) {
						// squaresMap.setSquareState(MapGenerator.VISITED, i, j + 1);
						openList.add(new pPair(fNew, new Pair(i, j + 1)));

						cellDetails[i][j + 1].f = fNew;
						cellDetails[i][j + 1].g = gNew;
						cellDetails[i][j + 1].h = hNew;
						cellDetails[i][j + 1].parentI = i;
						cellDetails[i][j + 1].parentJ = j;
						// repaint();
					}
				}
			}

			// 4th Successor WEST

			if (isValid(i, j - 1, ROW, COL) == true) {
				if (isDestionation(i, j - 1, targetX, targetY) == true) {
					cellDetails[i][j - 1].parentI = i;
					cellDetails[i][j - 1].parentJ = j;
					tracePath(cellDetails, targetX, targetY);
					foundDest = true;
					return;
				} else if (closedList[i][j - 1] == false && squaresMap.map[i][j - 1] != MapGenerator.BORDER) {
					gNew = cellDetails[i][j].g + 1.0;
					hNew = calculateHValue(i, j - 1, targetX, targetY);
					fNew = gNew + hNew;

					if (cellDetails[i][j - 1].f == Double.MAX_VALUE || cellDetails[i][j - 1].f > fNew) {
						// squaresMap.setSquareState(MapGenerator.VISITED, i, j - 1);
						openList.add(new pPair(fNew, new Pair(i, j - 1)));

						cellDetails[i][j - 1].f = fNew;
						cellDetails[i][j - 1].g = gNew;
						cellDetails[i][j - 1].h = hNew;
						cellDetails[i][j - 1].parentI = i;
						cellDetails[i][j - 1].parentJ = j;
						// repaint();
					}
				}
			}

			// 5th Successor NORTH-EAST

			if (isValid(i - 1, j + 1, ROW, COL) == true) {
				if (isDestionation(i - 1, j + 1, targetX, targetY) == true) {
					cellDetails[i - 1][j + 1].parentI = i;
					cellDetails[i - 1][j + 1].parentJ = j;
					tracePath(cellDetails, targetX, targetY);
					foundDest = true;
					return;
				} else if (closedList[i - 1][j + 1] == false && squaresMap.map[i - 1][j + 1] != MapGenerator.BORDER) {
					gNew = cellDetails[i][j].g + 1.0;
					hNew = calculateHValue(i - 1, j + 1, targetX, targetY);
					fNew = gNew + hNew;

					if (cellDetails[i - 1][j + 1].f == Double.MAX_VALUE || cellDetails[i - 1][j + 1].f > fNew) {
						// squaresMap.setSquareState(MapGenerator.VISITED, i - 1, j + 1);
						openList.add(new pPair(fNew, new Pair(i - 1, j + 1)));

						cellDetails[i - 1][j + 1].f = fNew;
						cellDetails[i - 1][j + 1].g = gNew;
						cellDetails[i - 1][j + 1].h = hNew;
						cellDetails[i - 1][j + 1].parentI = i;
						cellDetails[i - 1][j + 1].parentJ = j;
						// repaint();
					}
				}
			}

			// 6th Successor NORTH-WEST

			if (isValid(i - 1, j - 1, ROW, COL) == true) {
				if (isDestionation(i - 1, j - 1, targetX, targetY) == true) {
					cellDetails[i - 1][j - 1].parentI = i;
					cellDetails[i - 1][j - 1].parentJ = j;
					tracePath(cellDetails, targetX, targetY);
					foundDest = true;
					return;
				} else if (closedList[i - 1][j - 1] == false && squaresMap.map[i - 1][j - 1] != MapGenerator.BORDER) {
					gNew = cellDetails[i][j].g + 1.0;
					hNew = calculateHValue(i - 1, j - 1, targetX, targetY);
					fNew = gNew + hNew;

					if (cellDetails[i - 1][j - 1].f == Double.MAX_VALUE || cellDetails[i - 1][j - 1].f > fNew) {
						// squaresMap.setSquareState(MapGenerator.VISITED, i - 1, j - 1);
						openList.add(new pPair(fNew, new Pair(i - 1, j - 1)));

						cellDetails[i - 1][j - 1].f = fNew;
						cellDetails[i - 1][j - 1].g = gNew;
						cellDetails[i - 1][j - 1].h = hNew;
						cellDetails[i - 1][j - 1].parentI = i;
						cellDetails[i - 1][j - 1].parentJ = j;
						// repaint();
					}
				}
			}

			// 7th Successor SOUTH-EAST

			if (isValid(i + 1, j + 1, ROW, COL) == true) {
				if (isDestionation(i + 1, j + 1, targetX, targetY) == true) {
					cellDetails[i + 1][j + 1].parentI = i;
					cellDetails[i + 1][j + 1].parentJ = j;
					tracePath(cellDetails, targetX, targetY);
					foundDest = true;
					return;
				} else if (closedList[i + 1][j + 1] == false && squaresMap.map[i + 1][j + 1] != MapGenerator.BORDER) {
					gNew = cellDetails[i][j].g + 1.0;
					hNew = calculateHValue(i + 1, j + 1, targetX, targetY);
					fNew = gNew + hNew;

					if (cellDetails[i + 1][j + 1].f == Double.MAX_VALUE || cellDetails[i + 1][j + 1].f > fNew) {
						// squaresMap.setSquareState(MapGenerator.VISITED, i + 1, j + 1);
						openList.add(new pPair(fNew, new Pair(i + 1, j + 1)));

						cellDetails[i + 1][j + 1].f = fNew;
						cellDetails[i + 1][j + 1].g = gNew;
						cellDetails[i + 1][j + 1].h = hNew;
						cellDetails[i + 1][j + 1].parentI = i;
						cellDetails[i + 1][j + 1].parentJ = j;
						// repaint();
					}
				}
			}

			// 8th Successor SOUTH-WEST

			if (isValid(i + 1, j - 1, ROW, COL) == true) {
				if (isDestionation(i + 1, j - 1, targetX, targetY) == true) {
					cellDetails[i + 1][j - 1].parentI = i;
					cellDetails[i + 1][j - 1].parentJ = j;
					tracePath(cellDetails, targetX, targetY);
					foundDest = true;
					return;
				} else if (closedList[i + 1][j - 1] == false && squaresMap.map[i + 1][j - 1] != MapGenerator.BORDER) {
					gNew = cellDetails[i][j].g + 1.0;
					hNew = calculateHValue(i + 1, j - 1, targetX, targetY);
					fNew = gNew + hNew;

					if (cellDetails[i + 1][j - 1].f == Double.MAX_VALUE || cellDetails[i + 1][j - 1].f > fNew) {
						// squaresMap.setSquareState(MapGenerator.VISITED, i + 1, j - 1);
						openList.add(new pPair(fNew, new Pair(i + 1, j - 1)));

						cellDetails[i + 1][j - 1].f = fNew;
						cellDetails[i + 1][j - 1].g = gNew;
						cellDetails[i + 1][j - 1].h = hNew;
						cellDetails[i + 1][j - 1].parentI = i;
						cellDetails[i + 1][j - 1].parentJ = j;
						// repaint();
					}
				}
			}
			if (foundDest == false) {
				System.out.println("Not found");
			}
		}
	}

	private void tracePath(CellDetails[][] cellDetails, int targetX2, int targetY2) {
		int row = targetX2;
		int col = targetY2;

		Stack<Pair> path = new Stack<>();

		while (!(cellDetails[row][col].parentI == row && cellDetails[row][col].parentJ == col)) {
			path.push(new Pair(row, col));
			int tempRow = cellDetails[row][col].parentI;
			int tempCol = cellDetails[row][col].parentJ;
			row = tempRow;
			col = tempCol;
		}

		path.push(new Pair(row, col));

		Pair firstPair = path.peek();
		Pair lastPair = firstPair;

		while (!path.empty()) {
			Pair p = path.peek();
			lastPair = p;
			path.pop();
			squaresMap.setSquareState(MapGenerator.PATH, p.first, p.second);
			System.out.printf("-> (%d, %d)", p.first, p.second);
			repaint();
		}

		squaresMap.setSquareState(MapGenerator.STARTPOSITION, firstPair.first, firstPair.second);
		squaresMap.setSquareState(MapGenerator.TARGETPOSITION, lastPair.first, lastPair.second);

		repaint();
	}

	private double calculateHValue(int i, int j, int targetX2, int targetY2) {
		return (Math.sqrt((i - targetX2) * (i - targetX2) + (j - targetY2) * (j - targetY2)));
	}

	private boolean isDestionation(int i, int j, int targetX2, int targetY2) {
		if (i == targetX2 && j == targetY2)
			return true;
		else
			return false;
	}

	private boolean isValid(int i, int j, int rOW, int cOL) {
		return (i >= 0) && (i < rOW) && (j >= 0) && (j < cOL);
	}
}

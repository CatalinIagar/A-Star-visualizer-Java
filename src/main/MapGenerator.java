package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class MapGenerator extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1623029680935426641L;
	public static final int WHITESPACE = 1;
	public static final int BORDER = 2;
	public static final int STARTPOSITION = 3;
	public static final int TARGETPOSITION = 4;
	public static final int VISITED = 5;
	public static final int PATH = 6;

	private static int squareWidth;
	private static int squareHeight;

	public int map[][];

	@SuppressWarnings("static-access")
	public MapGenerator(int row, int col, int squareWidth, int squareHeight) {
		this.squareWidth = squareWidth;
		this.squareHeight = squareHeight;
		map = new int[row][col];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				map[i][j] = WHITESPACE;
			}
		}
	}

	public void draw(Graphics2D g) {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j] == WHITESPACE) {
					g.setColor(Color.white);
					g.fillRect(j * squareWidth + 5, i * squareHeight + 5, squareWidth, squareHeight);

					g.setStroke(new BasicStroke(1));
					g.setColor(new Color(173, 216, 230));
					g.drawRect(j * squareWidth + 5, i * squareHeight + 5, squareWidth, squareHeight);
				}
				if (map[i][j] == STARTPOSITION) {
					g.setColor(Color.green);
					g.fillRect(j * squareWidth + 5, i * squareHeight + 5, squareWidth, squareHeight);

					g.setStroke(new BasicStroke(1));
					g.setColor(Color.green);
					g.drawRect(j * squareWidth + 5, i * squareHeight + 5, squareWidth, squareHeight);
				}
				if (map[i][j] == TARGETPOSITION) {
					g.setColor(Color.red);
					g.fillRect(j * squareWidth + 5, i * squareHeight + 5, squareWidth, squareHeight);

					g.setStroke(new BasicStroke(1));
					g.setColor(Color.red);
					g.drawRect(j * squareWidth + 5, i * squareHeight + 5, squareWidth, squareHeight);
				}
				if (map[i][j] == BORDER) {
					g.setColor(Color.black);
					g.fillRect(j * squareWidth + 5, i * squareHeight + 5, squareWidth, squareHeight);

					g.setStroke(new BasicStroke(1));
					g.setColor(Color.black);
					g.drawRect(j * squareWidth + 5, i * squareHeight + 5, squareWidth, squareHeight);
				}
				if (map[i][j] == VISITED) {
					g.setColor(new Color(173, 216, 230));
					g.fillRect(j * squareWidth + 5, i * squareHeight + 5, squareWidth, squareHeight);

					g.setStroke(new BasicStroke(1));
					g.setColor(new Color(173, 216, 230));
					g.drawRect(j * squareWidth + 5, i * squareHeight + 5, squareWidth, squareHeight);
				}
				if (map[i][j] == PATH) {
					g.setColor(Color.yellow);
					g.fillRect(j * squareWidth + 5, i * squareHeight + 5, squareWidth, squareHeight);

					g.setStroke(new BasicStroke(1));
					g.setColor(Color.yellow);
					g.drawRect(j * squareWidth + 5, i * squareHeight + 5, squareWidth, squareHeight);
				}
			}
		}
	}

	public void setSquareState(int value, int row, int col) {
		map[row][col] = value;
	}
}

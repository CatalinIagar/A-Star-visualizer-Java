package main;

import javax.swing.JFrame;

public class Main {
	private static final int WIDTH = 1400;
	private static final int HEIGHT = 835;

	public static void main(String[] args) {
		JFrame myFrame = new JFrame("PathFinding visualizer");
		PathFinding pathFindingAlgoritms = new PathFinding(WIDTH, HEIGHT);
		myFrame.setResizable(false);
		myFrame.setVisible(true);
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.add(pathFindingAlgoritms);
		myFrame.pack();
	}

}

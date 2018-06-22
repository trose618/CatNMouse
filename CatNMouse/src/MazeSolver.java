import javax.swing.JFrame;
import java.awt.Color;

public class MazeSolver {

	private static MazePanel maze; // The panel that holds the maze.

	public static void main(String[] args) throws InterruptedException {
		JFrame window = new JFrame("Recursive Maze Solver Demo");
		maze = new MazePanel(10, 10, 10);
		window.setContentPane(maze);
		window.pack();
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(100, 50);
		window.setVisible(true);
		// TODO: call the maze solver method
		try {
			solver2(maze.getRows() - 2, maze.getColumns() - 2);
		} catch (RuntimeException e) {
		}
	}// end of main

	private static void solver2(int r, int c) {

		// base case

		if (r == 1 && c == 1) {
			maze.setColor(r, c, Color.GREEN);
			System.out.println("Solved");
			System.exit(0);
			//throw new RuntimeException();
		} else {
			// if on a white spot, check all four sides

			if (maze.getColor(r, c) != null) {

			} else {
				maze.setColor(r, c, Color.RED);
				solver2(r, c + 1);
				solver2(r, c - 1);
				solver2(r + 1, c);
				solver2(r - 1, c);
				maze.setColor(r, c, Color.YELLOW);
				if ((maze.getColor(r + 1, c) != null && maze.getColor(r + 1, c) != Color.RED)
						&& (maze.getColor(r - 1, c) != null && maze.getColor(r - 1, c) != Color.RED)
						&& (maze.getColor(r, c + 1) != null && maze.getColor(r, c + 1) != Color.RED)
						&& (maze.getColor(r, c - 1) != null) && maze.getColor(r, c - 1) != Color.RED) {
					System.out.println("Unsolvable, resetting...");
					
					maze.create();
					//maze.repaint();
					solver2(maze.getRows() - 2, maze.getColumns() - 2);

				}
				// System.out.println("test");
				// problem, method tries to move to each spot then stops here
				// when it can't move
				// solver2(r, c);
			}

		}

	}// end of solver

	private static void delay(int milliseconds) {
		if (milliseconds > 0) {
			try {
				Thread.sleep(milliseconds);
			} catch (InterruptedException e) {
			}
		}
	}

}// end of class

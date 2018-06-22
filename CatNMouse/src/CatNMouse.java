import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * This class runs a game called CatNMouse which utilizes parts of code from
 * David Eck. This is the local version. Network version coming....soon.
 * 
 * @author Terrance Rose Jr.
 *
 */
public class CatNMouse extends JPanel {

	private static MazePanel maze; // The panel that holds the maze.
	private static ArrayList<int[]> visited = new ArrayList<int[]>();// list of
																		// positions
																		// visited
																		// in
																		// the
																		// maze.
	private static String winner;
	public static Player mouse;
	private static Player cat;
	private static boolean automatic = false;// true if player selects to play
												// against
	// computer
	private static AutoCat cat1 = new AutoCat(maze);
	private static int difficulty;

	public static void main(String[] args) throws InterruptedException {
		JFrame window = new JFrame("Cat N Mouse");
		CatNMouse content = new CatNMouse();
		window.setContentPane(content);
		window.pack();
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLocation(100, 50);
		content.requestFocusInWindow();
		window.setVisible(true);

	}// end of main

	/**
	 * This constructor sets up the maze and panel, prompting the user if they
	 * want to play against another person on the computer, or simply play
	 * against the computer, which is an automated cat.
	 */
	private CatNMouse() {

		JOptionPane.showMessageDialog(maze,
				"Welcome to CatNMouse!\nThis is a maze based game in which the mouse is trying \nto get through the maze without getting eaten by the cat. The\ncat, of course, is trying to eat the mouse before it can get through\nthe maze. If two people want to play, the mouse is controlled with\nthe arrow keys, while the cat is controlled with the ASDW keys.\nThere is also an option for an automated cat. Lastly, if you want to\nreset the game, press the r key. In two player mode, the game will\nprompt, but reset will automatically happen if playing\nagainst the computer.\n\nEnjoy! :-D - T. Rose");

		int a = 0;
		a = JOptionPane.showConfirmDialog(maze, "Would you like to play against another person?",
				"1 player or 2 player?", JOptionPane.YES_NO_OPTION);

		if (a == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
		// if player does not want to play 2 players, disable player 2 controls
		if (a == JOptionPane.NO_OPTION) {
			automatic = true;

			while (true) {
				String number = JOptionPane.showInputDialog(maze,
						"Enter a number for cat speed (between 0 - 1000).\nA smaller number means a faster cat.\nYou will lose INSTANTLY if you enter 0!",
						"Difficulty", JOptionPane.PLAIN_MESSAGE);
				if (number == null) {
					System.exit(0);
				}
				try {
					difficulty = Integer.parseInt(number);
				} catch (NumberFormatException e) {
					JOptionPane.showConfirmDialog(maze, "Please enter a valid number value.");
				}
				if (difficulty >= 0 && difficulty <= 1000) {
					break;
				}
			}
		} else {
			automatic = false;
		}

		maze = new MazePanel(50, 50, 10);
		this.add(maze, BorderLayout.CENTER);

		Listener listener = new Listener();
		this.addKeyListener(listener);

		mouse = new Player(1, 1);
		cat = new Player(maze.getRows() - 2, maze.getColumns() - 2);
		maze.setColor(mouse.r_, mouse.c_, Color.GRAY);
		maze.setColor(cat.r_, cat.c_, Color.ORANGE);
		drawPath();
		maze.repaint();
		if (automatic == true) {
			// AutoCat cat1 = new AutoCat(maze);

			cat1.start();

		}

		if ((maze.getColor(cat.r_ + 1, cat.c_) == Color.BLACK) && (maze.getColor(cat.r_ - 1, cat.c_) == Color.BLACK)
				&& (maze.getColor(cat.r_, cat.c_ + 1) == Color.BLACK)
				&& (maze.getColor(cat.r_, cat.c_ - 1) == Color.BLACK)) {
			reset();
		}

	}

	/**
	 * This class implements movement commands for the cat and mouse and
	 * disables the cat controls when the user plays against the computer. The
	 * mouse is controlled using the arrow keys while the cat is controlled by
	 * the ASDW keys.
	 * 
	 * @author Terrance Rose
	 *
	 */
	private class Listener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent evt) {
			int ch = evt.getKeyCode();// gets the key pressed
			if (ch == KeyEvent.VK_R) {
				if (automatic) {
					AutoCat.running = false;
				} else {
					int answer = JOptionPane.showConfirmDialog(maze, "Are you sure you want to generate a new maze?",
							"Reset", JOptionPane.YES_NO_OPTION);
					if (answer == JOptionPane.YES_OPTION) {
						reset();
					}
				}
			}
			// MOUSE COMMANDS MOVING USING DIRECTIONAL KEYS
			if (ch == KeyEvent.VK_RIGHT) {
				if (mouse.c_ == maze.getColumns() - 1) {
					maze.setColor(mouse.r_, mouse.c_, null);
					mouse.c_ = 0;
					maze.setColor(mouse.r_, mouse.c_, Color.GRAY);
					maze.repaint();

				} else if (maze.getColor(mouse.r_, mouse.c_ + 1) != (Color.BLACK)) {

					maze.setColor(mouse.r_, mouse.c_, null);
					mouse.c_ = mouse.c_ + 1;
					maze.setColor(mouse.r_, mouse.c_, Color.GRAY);
				}
				maze.repaint();

			}

			if (ch == KeyEvent.VK_LEFT) {
				if (mouse.c_ == 0) {
					maze.setColor(mouse.r_, mouse.c_, null);
					mouse.c_ = maze.getColumns() - 1;
					maze.setColor(mouse.r_, mouse.c_, Color.GRAY);
					maze.repaint();

				} else if (maze.getColor(mouse.r_, mouse.c_ - 1) != (Color.BLACK)) {

					maze.setColor(mouse.r_, mouse.c_, null);
					mouse.c_ = mouse.c_ - 1;
					maze.setColor(mouse.r_, mouse.c_, Color.GRAY);
				}
				maze.repaint();

			}

			if (ch == KeyEvent.VK_DOWN) {
				if (maze.getColor(mouse.r_ + 1, mouse.c_) != (Color.BLACK)) {
					maze.setColor(mouse.r_, mouse.c_, null);// change previous
															// position to white
					mouse.r_ = mouse.r_ + 1;
					maze.setColor(mouse.r_, mouse.c_, Color.GRAY);
					maze.repaint();

				}
			}
			if (ch == KeyEvent.VK_UP) {
				if (maze.getColor(mouse.r_ - 1, mouse.c_) != (Color.BLACK)) {
					maze.setColor(mouse.r_, mouse.c_, null);// change previous
															// position to white
					mouse.r_ = mouse.r_ - 1;
					maze.setColor(mouse.r_, mouse.c_, Color.GRAY);
					maze.repaint();

				}
			}
			// CAT COMMANDS MOVING WITH ASDW KEYS
			if (ch == KeyEvent.VK_D && automatic == false) {
				if (cat.c_ == maze.getColumns() - 1) {
					maze.setColor(cat.r_, cat.c_, null);
					cat.c_ = 0;
					maze.setColor(cat.r_, cat.c_, Color.ORANGE);
					maze.repaint();
				} else if (maze.getColor(cat.r_, cat.c_ + 1) != (Color.BLACK)) {

					maze.setColor(cat.r_, cat.c_, null);
					cat.c_ = cat.c_ + 1;
					maze.setColor(cat.r_, cat.c_, Color.ORANGE);
				}
				maze.repaint();
			}
			if (ch == KeyEvent.VK_A && automatic == false) {
				if (cat.c_ == 0) {
					maze.setColor(cat.r_, cat.c_, null);
					cat.c_ = maze.getColumns() - 1;
					maze.setColor(cat.r_, cat.c_, Color.ORANGE);
					maze.repaint();
				} else if (maze.getColor(cat.r_, cat.c_ - 1) != (Color.BLACK)) {

					maze.setColor(cat.r_, cat.c_, null);
					cat.c_ = cat.c_ - 1;
					maze.setColor(cat.r_, cat.c_, Color.ORANGE);
				}
				maze.repaint();
			}
			if (ch == KeyEvent.VK_W && automatic == false) {
				if (maze.getColor(cat.r_ - 1, cat.c_) != (Color.BLACK)) {
					maze.setColor(cat.r_, cat.c_, null);// change previous
														// position to white
					cat.r_ = cat.r_ - 1;
					maze.setColor(cat.r_, cat.c_, Color.ORANGE);
					maze.repaint();
				}
			}
			if (ch == KeyEvent.VK_S && automatic == false) {
				if (maze.getColor(cat.r_ + 1, cat.c_) != (Color.BLACK)) {
					maze.setColor(cat.r_, cat.c_, null);// change previous
														// position to white
					cat.r_ = cat.r_ + 1;
					maze.setColor(cat.r_, cat.c_, Color.ORANGE);
					maze.repaint();
				}
			}
			int a = 0;
			if (mouse.r_ == cat.r_ && mouse.c_ == cat.c_ && automatic == false) {
				a = JOptionPane.showConfirmDialog(maze,
						"The Cat has eaten the mouse! The Cat Wins! Would you like to play again?", "Game Over",
						JOptionPane.YES_NO_OPTION);

				if (a == JOptionPane.YES_OPTION) {
					maze.setColor(mouse.r_, mouse.c_, null);
					maze.setColor(cat.r_, cat.c_, null);
					reset();
				}
				if (a == JOptionPane.NO_OPTION) {
					System.exit(0);
				}
			}
			if (mouse.r_ == maze.getRows() - 2 && mouse.c_ == maze.getColumns() - 2) {
				if (automatic) {
					winner = "mouse";
					cat1.interrupt();
				} else {
					a = JOptionPane.showConfirmDialog(maze,
							"The Mouse has made it to his hole! The Mouse Wins! Would you like to play again?",
							"Game Over", JOptionPane.YES_NO_OPTION);
					if (a == JOptionPane.YES_OPTION) {
						maze.setColor(mouse.r_, mouse.c_, null);
						maze.setColor(cat.r_, cat.c_, null);
						reset();
					}
					if (a == JOptionPane.NO_OPTION) {
						System.exit(0);
					}
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent evt) {

		}

		@Override
		public void keyTyped(KeyEvent evt) {

		}
	}// end of private class

	/**
	 * Resets the game state
	 */
	private static void reset() {

		maze.create();
		mouse = new Player(1, 1);
		cat = new Player(maze.getRows() - 2, maze.getColumns() - 2);
		maze.setColor(mouse.r_, mouse.c_, Color.GRAY);
		maze.setColor(cat.r_, cat.c_, Color.ORANGE);
		drawPath();
		maze.repaint();
		if (automatic == true) {

			visited.clear();
			AutoCat newCat = new AutoCat(maze);
			cat1 = newCat;
			cat1.start();
		}
		if ((maze.getColor(cat.r_ + 1, cat.c_) == Color.BLACK) && (maze.getColor(cat.r_ - 1, cat.c_) == Color.BLACK)
				&& (maze.getColor(cat.r_, cat.c_ + 1) == Color.BLACK)
				&& (maze.getColor(cat.r_, cat.c_ - 1) == Color.BLACK)) {
			reset();
		}
	}

	/**
	 * draws a straight path through the middle of the maze
	 */
	private static void drawPath() {
		for (int i = 0; i < maze.getColumns(); i++) {

			maze.setColor(maze.getRows() / 2, maze.getColumns() - 1 - i, null);
		}
	}

	/**
	 * Thread class to run the automated cat for CatNMouse. Constructor needs
	 * the maze the cat will be in, searching for its food. Runtime exception is
	 * called when autoCat method reaches a game ending condition such as the
	 * cat eating the mouse or the mouse making it to the end of the maze. It
	 * prompts user and asks if they want to play again.
	 * 
	 * @author Terrance Rose Jr.
	 *
	 */
	static class AutoCat extends Thread {
		MazePanel maze_;
		volatile static boolean running;

		AutoCat(MazePanel maze) {
			maze_ = maze;
			running = true;
		}

		public void run() {
			try {
				autoCat(maze.getRows() - 2, maze.getColumns() - 2);
			} catch (RuntimeException e) {

				if (AutoCat.running == false) {// if running is false, that
												// means
												// reset was called
					reset();
				} else {
					int a;
					a = JOptionPane.showConfirmDialog(maze,
							"Game over, the " + winner + " won! Would you like to play again?", "Game Over",
							JOptionPane.YES_NO_OPTION);

					if (a == JOptionPane.YES_OPTION) {
						maze.setColor(mouse.r_, mouse.c_, null);
						maze.setColor(cat.r_, cat.c_, null);

						reset();
					}
					if (a == JOptionPane.NO_OPTION) {
						System.exit(0);
					}
				}
			}
		}

		/**
		 * Based off of David Eck's Solver method. This method automates the
		 * cat's movements. Throws a RuntimeException when a game ending
		 * condition is met.
		 * 
		 * @param r
		 *            row where the cat is placed
		 * @param c
		 *            column where the cat is placed
		 */
		private static void autoCat(int r, int c) {

			if (running == false) {
				throw new RuntimeException();
			}

			if (r == 1 && c == 1) {
				CatNMouse.visited.clear();
			}
			if (r == mouse.r_ && c == mouse.c_) {// if cat runs into mouse
				winner = "cat";
				throw new RuntimeException();// runtime exception will prompt
												// player about state of game

			}
			if (mouse.r_ == maze.getRows() - 2 && mouse.c_ == maze.getColumns() - 2) {
				winner = "mouse";
				throw new RuntimeException();// if mouse makes it to the end
			}

			else {
				// if on a white spot, check all four sides
				if (maze.getColor(r, c) == Color.BLACK || CatNMouse.hasBeenVisited(r, c)) {

				} else {
					maze.setColor(r, c, Color.ORANGE);
					CatNMouse.setVisited(r, c);
					delay(difficulty);
					maze.repaint();
					if (maze.getColor(r, c - 1) == Color.ORANGE) {
						maze.setColor(r, c - 1, null);
					}
					if (c == 1 && (maze.getColor(r, maze.getColumns() - 1) == Color.ORANGE)) {
						maze.setColor(r, maze.getColumns() - 1, null);
					}
					if (c == maze.getColumns() - 1) {
						autoCat(r, 1);
						maze.setColor(r, c, null);
					} else {
						autoCat(r, c + 1);// move right
					}
					maze.setColor(r, c, null);
					if (c == 1) {
						autoCat(r, maze.getColumns() - 1);
						maze.setColor(r, c, null);
					} else {
						autoCat(r, c - 1);// move left
					}
					maze.setColor(r, c, null);
					autoCat(r + 1, c);// move down
					maze.setColor(r, c, null);
					autoCat(r - 1, c);// move up
					maze.setColor(r, c, null);

				}

			}

		}// end of autoCat
	}

	/**
	 * Adds a position to the list of positions visited by the cat.
	 * 
	 * @param r
	 *            the row of the position to add
	 * @param c
	 *            the column of the position to add
	 */
	public static void setVisited(int r, int c) {
		int[] temp = new int[2];
		temp[0] = r;
		temp[1] = c;
		visited.add(temp);
	}

	/**
	 * Checks if a spot has been visited by the cat.
	 * 
	 * @param r
	 *            row of the spot being checked
	 * @param c
	 *            column of the spot being checked
	 * @return true if the spot has been visited, false if not.
	 */
	public static boolean hasBeenVisited(int r, int c) {

		if (visited.size() == 0) {
			return false;
		}
		for (int i = 0; i < visited.size(); i++) {
			if (visited.get(i)[0] == r && visited.get(i)[1] == c) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets a delay for a given amount of milliseconds
	 * 
	 * @param milliseconds
	 *            - amount of milliseconds to delay for.
	 */
	private static void delay(int milliseconds) {
		if (milliseconds > 0) {
			try {
				Thread.sleep(milliseconds);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * This class represents a player who has a position in the maze represented
	 * by c - columns and r - rows
	 * 
	 * @author Terrance Rose
	 *
	 */
	private static class Player {
		int c_;// columns
		int r_;// rows

		public Player(int r, int c) {
			c_ = c;
			r_ = r;
		}
	}
}// end of class

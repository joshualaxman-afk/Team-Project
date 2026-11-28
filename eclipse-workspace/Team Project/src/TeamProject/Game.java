package TeamProject;

import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdDraw;

public class Game {
	private static boolean gameOver = false;
	private static Block block;
	private static Board board;

	public static void main(String[] args) {
		startGame();
	}

	private static void startGame() {
		String[] level = { "###      ", "###      ", "#######  ", " ########", "    ##G  " };

		board = new Board(level, '#', 'G');
		block = new Block(new Position(1, 1), board);

		UI.init(board);
		UI.drawBoard(block, board);
		while (!gameOver) {
			if (StdDraw.hasNextKeyTyped()) {
				switch (StdDraw.nextKeyTyped()) {
				case 'w' -> block.moveUp();
				case 's' -> block.moveDown();
				case 'a' -> block.moveLeft();
				case 'd' -> block.moveRight();
				}
			}
			StdDraw.pause(20);
		}
	}

	public static void lostGame() {
		gameOver = true;
		System.out.print("Lost Game");
		UI.drawLoseScreen();
	}

	public static void wonGame() {
		gameOver = true;
		System.out.print("Win Game");
		UI.drawWinScreen();
	}
}

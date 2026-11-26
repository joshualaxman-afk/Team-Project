package TeamProject;

public class Board {
	private Point winPoint;
	private boolean[][] board;

	public Board(Point winPoint, boolean[][] hasBlockAt) {
		this.winPoint = winPoint;
		this.board = hasBlockAt;
	}

	// INVARIANT: blockChar != winChar.
	public Board(String[] boardLines, char blockChar, char winChar) {
		int sizeX = Integer.MIN_VALUE;
		int sizeY = boardLines.length;
		for (String line : boardLines) {
			sizeX = Math.max(sizeX, line.length());
		}

		this.board = new boolean[sizeY][sizeX];
		for (int y = 0; y < boardLines.length; y++) {
			String line = boardLines[y];
			for (int x = 0; x < line.length(); x++) {
				char currentChar = line.charAt(x);
				this.board[y][x] = currentChar == blockChar;
				if (currentChar == winChar) {
					this.winPoint = new Point(x, y);
				}
			}
		}
	}

	public int getWidth() {
		return board[0].length;
	}

	public int getHeight() {
		return board.length;
	}

	public Point getWinPoint() {
		return winPoint;
	}

	public boolean hasBlockAt(Point point) {
		if (point.y() < 0 || point.y() >= board.length || point.x() < 0 || point.x() >= board[0].length) {
			return false;
		}
		return this.board[point.y()][point.x()];
	}

	public boolean hasBlockAt(int x, int y) {
		return this.hasBlockAt(new Point(x, y));
	}

	public boolean isFailurePosition(Position position) {
		if(this.isWinPosition(position)) {
			return false;
		}
		
		for (int i = 0; i < position.points().length; i++) {
			Point point = position.points()[i];
			if (!this.hasBlockAt(point)) {
				return true;
			}
		}

		return false;
	}

	public boolean isWinPosition(Position position) {
		if (position.points().length != 1) {
			return false;
		}

		Point point = position.points()[0];
		return point.equals(winPoint);
	}
}

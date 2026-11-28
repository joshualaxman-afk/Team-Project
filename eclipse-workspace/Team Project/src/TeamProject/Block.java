package TeamProject;

public class Block {
	private Board board;
	private Position position; // Position of the block in the grid
	private final Position startingPosition;

	public Block(Position position, Board board) {
		this.board = board;
		this.position = position;
		this.startingPosition = position;
	}

	public Position getPosition() {
		return this.position;
	}
	
	public Position getStartingPosition() {
		return this.startingPosition;
	}

	public void moveUp() {
		this.move(Direction.UP);
	}

	public void moveDown() {
		this.move(Direction.DOWN);
	}

	public void moveLeft() {
		this.move(Direction.LEFT);
	}

	public void moveRight() {
		this.move(Direction.RIGHT);
	}

	private void move(Direction direction) {
		this.position = this.position.goToDirection(direction);
		this.checkState();
	}

	private void checkState() {
		if (this.board.isFailurePosition(this.position)) {
			Game.lostGame();
			return;
		}

		if (this.board.isWinPosition(this.position)) {
			Game.wonGame();
			return;
		}

		UI.drawBoard(this, board);
	}
}

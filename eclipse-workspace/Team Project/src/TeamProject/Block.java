package TeamProject;

public class Block {
	Board board;
	Position pos; // Position of the block in the grid 

	 public Block(Position pos, Board board) {
	        this.pos = pos;
	        this.board = board;
	    }

	    public Position getPosition() {
	        return pos;
	    }

	    public void moveUp()    { move(Direction.UP); }
	    public void moveDown()  { move(Direction.DOWN); }
	    public void moveLeft()  { move(Direction.LEFT); }
	    public void moveRight() { move(Direction.RIGHT); }

	    private void move(Direction direction) {
	        this.pos = this.pos.goToDirection(direction);
	        checkState();
	    }

	    private void checkState() {
	        if (board.isFailurePosition(pos)) {
	            Game.lostGame();
	            return;
	        }

	        if (board.isWinPosition(pos)) {
	            Game.wonGame();
	            return;
	        }

	        UI.drawScreen(this, board);
	    }
}

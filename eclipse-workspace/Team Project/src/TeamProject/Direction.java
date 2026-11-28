package TeamProject;

public enum Direction {
	UP, DOWN, LEFT, RIGHT;
	
	private static final Direction[] flippedDirectionLookup = {
		DOWN,
		UP,
		RIGHT,
		LEFT
	};
	
	public Direction flip() {
		return flippedDirectionLookup[this.ordinal()];
	}
}

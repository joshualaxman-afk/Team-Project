package TeamProject;

import java.util.Objects;

public record Point(int x, int y) {

	@Override
	public boolean equals(Object otherObject) {
		if (this == otherObject) {
			return true;
		}

		if (otherObject.getClass() != Point.class) {
			return false;
		}

		Point other = (Point) (otherObject);
		return other.x() == this.x() && other.y() == this.y();
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.x, this.y);
	}
	
	public static Point positionToMinPoint(Position position) {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		for(Point point : position.points()) {
			minX = Math.min(minX, point.x());
			minY = Math.min(minY, point.y());
		}
		
		return new Point(minX, minY);
	}
}
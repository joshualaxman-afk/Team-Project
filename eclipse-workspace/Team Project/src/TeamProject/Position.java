package TeamProject;

// INVARIANT: !points[0].equals(points[1]) && points.length <= MAX_POINT_LENGTH && points.length >= 1
public record Position(Point[] points) {
	public static final int MAX_POINT_LENGTH = 2;

	public Position(Point point) {
		this(new Point[] { point });
	}

	public Position(int x, int y) {
		this(new Point(x, y));
	}

	public Position(Point firstPoint, Point secondPoint) {
		this(new Point[] { firstPoint, secondPoint });
	}

	public Position(int x0, int y0, int x1, int y1) {
		this(new Point(x0, y0), new Point(x1, y1));
	}

	public PositionType getPositionType() {
		if (this.points().length == 1) {
			return PositionType.ONE;
		}
		Point firstPoint = this.points()[0];
		Point secondPoint = this.points()[1];
		if (firstPoint.y() == secondPoint.y()) {
			return PositionType.HORIZ;
		}

		return PositionType.VERT;
	}

	// NOTE: If the offset needs only has a single point and the current position
	// has two positions, then the point closest to zero will be used for the
	// offset.
	public Position fromOffset(Position offset) {
		if (this.points().length == 1 && offset.points().length == 2) {
			Point point = this.points()[0];
			Point firstOffsetPoint = offset.points()[0];
			Point secondOffsetPoint = offset.points()[1];
			return new Position(point.x() + firstOffsetPoint.x(), point.y() + firstOffsetPoint.y(),
					point.x() + secondOffsetPoint.x(), point.y() + secondOffsetPoint.y());
		}

		if (this.points().length == 2 && offset.points().length == 1) {
			Point firstPoint = this.points()[0];
			Point secondPoint = this.points()[1];
			Point point = new Point(Math.min(firstPoint.x(), secondPoint.x()),
					Math.min(firstPoint.y(), secondPoint.y()));

			Point offsetPoint = offset.points()[0];
			return new Position(point.x() + offsetPoint.x(), point.y() + offsetPoint.y());
		}

		Point[] newPoints = new Point[MAX_POINT_LENGTH];
		for (int i = 0; i < this.points().length; i++) {
			Point thisPoint = this.points()[i];
			Point offsetPoint = offset.points()[i];
			newPoints[i] = new Point(thisPoint.x() + offsetPoint.x(), thisPoint.y() + offsetPoint.y());
		}

		return new Position(newPoints);
	}

	// INVARIANT: typeToPositionOffsets[PositionType][Direction] == pointOffset.
	private static Position[][] typeToPointOffsets = { { // ONE
			new Position(0, 1, 0, 2), // UP
			new Position(0, -1, 0, -2), // DOWN
			new Position(-1, 0, -2, 0), // LEFT
			new Position(1, 0, 2, 0) // RIGHT
			},

			{ // HORIZ
					new Position(0, 1, 0, 1), // UP
					new Position(0, -1, 0, -1), // DOWN
					new Position(-1, 0), // LEFT
					new Position(2, 0) // RIGHT
			},

			{ // VERT
					new Position(0, 2), // UP
					new Position(0, -1), // DOWN
					new Position(-1, 0, -1, 0), // LEFT
					new Position(1, 0, 1, 0) // RIGHT
			} };

	public Position goToDirection(Direction direction) {
		return this.fromOffset(Position.typeToPointOffsets[this.getPositionType().ordinal()][direction.ordinal()]);
	}
}

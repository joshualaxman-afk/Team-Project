package TeamProject;

import java.awt.Color;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SeparateChainingHashST;

public class UI {
	private static final int CELL_PIXEL_SIZE = 50;

	// Call once before the game starts
	public static void init(Board board) {
		int width = board.getWidth();
		int height = board.getHeight();
		StdDraw.setCanvasSize(width * CELL_PIXEL_SIZE, height * CELL_PIXEL_SIZE);

		// Cell centers land on integer coordinates
		StdDraw.setXscale(-0.5, width - 0.5);
		StdDraw.setYscale(-0.5, height - 0.5);
		StdDraw.enableDoubleBuffering(); // Avoids flicker
	}

	public static void drawBoard(Block block, Board board) {
		StdDraw.clear(Color.DARK_GRAY);

		// Floor tiles
		StdDraw.setPenColor(Color.LIGHT_GRAY);
		for (int y = 0; y < board.getHeight(); y++) {
			for (int x = 0; x < board.getWidth(); x++) {
				if (board.hasBlockAt(x, y)) {
					StdDraw.filledSquare(x, y, 0.48); // <0.5 leaves a grid gap
				}
			}
		}

		// Goal tile
		Point win = board.getWinPoint();
		if (win != null) {
			StdDraw.setPenColor(Color.RED);
			StdDraw.filledSquare(win.x(), win.y(), 0.48);
		}

		// The player's block (1 or 2 cells)
		StdDraw.setPenColor(Color.ORANGE);
		for (Point p : block.getPosition().points()) {
			StdDraw.filledSquare(p.x(), p.y(), 0.45);
		}

		StdDraw.show();
	}

	public static void drawWinScreen() {
		StdDraw.setPenColor(Color.GREEN);
		StdDraw.filledSquare(0, 0, 1);
	}

	public static void drawLoseScreen() {
		StdDraw.setPenColor(Color.RED);
		StdDraw.filledSquare(0, 0, 1);
	}
	
	// TODO: Put all the hash tables into this record and rename it to VertexDrawInfo.
	private static record VertexAndDoublePoint(int vertex, double x, double y) {}
	
	private static int findVertexInVertexAndDoublePoints(int targetVertex, ArrayList<VertexAndDoublePoint> vertexAndDoublePoints) {
		for(int i = 0; i < vertexAndDoublePoints.size(); i++) {
			if(vertexAndDoublePoints.get(i).vertex() == targetVertex) {
				return i;
			}
		}
		
		return -1;
	}

	// NOTE: This is separate from drawing the board (Or rest of the screen) in order to make this a toggleable option by the user.
	public static void drawGraph(Board board) {
		
		/// The movement logic of the game makes it so that, even in one place, multiple positions can exist, as such, they
		/// need space to fit in the board for the graph.
		SeparateChainingHashST<Point, Integer> pointToVertexOccupants = new SeparateChainingHashST<>();
		Graph positionGraph = board.getPositionGraph();
	 	for(int i = 0; i < positionGraph.V(); i++) {
	 		Point currentPoint = Point.positionToMinPoint(board.getVertexToPosition(i));
	 		Integer originalPoints = pointToVertexOccupants.get(currentPoint);
			if(originalPoints == null) {
	 			originalPoints = 0;
			}
    		
			pointToVertexOccupants.put(currentPoint, originalPoints + 1);
		}
    	
    	/// In order to be able to fit multiple vertices in the same board cell, we need to be able to know how many we started out with,
    	/// and how many are left. This way, we know how to give them all equal space. In order to do this, a copy is made.
    	SeparateChainingHashST<Point, Integer> pointToUndrawnVertexOccupants = new SeparateChainingHashST<>();
    	for(Point point : pointToVertexOccupants.keys()) {
    		pointToUndrawnVertexOccupants.put(point, pointToVertexOccupants.get(point));
    	}
    	
    	/// Figure out where the points are, so that lines can be drawn to them.
    	SeparateChainingHashST<Point, ArrayList<VertexAndDoublePoint>> pointToVertexAndDoublePoints = new SeparateChainingHashST<>();
    	for(int i = 0; i < positionGraph.V(); i++) {
    		Point currentPoint = Point.positionToMinPoint(board.getVertexToPosition(i));
    		int maxOccupancy = pointToVertexOccupants.get(currentPoint);
    		int currentOccupancy = pointToUndrawnVertexOccupants.get(currentPoint);
    		
    		if(!pointToVertexAndDoublePoints.contains(currentPoint)) {
    			pointToVertexAndDoublePoints.put(currentPoint, new ArrayList<>());
    		}
    		
    		ArrayList<VertexAndDoublePoint> currentVertexAndDoublePoints = pointToVertexAndDoublePoints.get(currentPoint);
    		if(findVertexInVertexAndDoublePoints(i, currentVertexAndDoublePoints) != -1) {
    			continue;
    		}
    		
    		int width = (int)(Math.sqrt(maxOccupancy));
    	
    		double startX = (double)(currentPoint.x()) * CELL_PIXEL_SIZE;
    		double startY = (double)(currentPoint.y()) * CELL_PIXEL_SIZE;
    		double sizeX = (double)(CELL_PIXEL_SIZE) / width;
    		double sizeY = (double)(CELL_PIXEL_SIZE) / (width + 1); // Add one for possible remainder.
    		double x = ((currentOccupancy % width) * sizeX) + startX;
    		double y = ((int)(currentOccupancy / width) * sizeY) + startY;
    		currentVertexAndDoublePoints.add(new VertexAndDoublePoint(i, x, y));
    		
    		pointToUndrawnVertexOccupants.put(currentPoint, currentOccupancy - 1);
    	}
    	
    	/// Draw dots and their corresponding lines.
    	for(Point point : pointToVertexAndDoublePoints.keys()) {
StdOut.printf("%f, %f\n", point.x(), point.y());
    		StdDraw.setPenColor(Color.MAGENTA);
    		StdDraw.filledCircle(point.x(), point.y(), 3);
    	}
    }
}

package TeamProject;

import java.util.ArrayList;
import java.util.function.Function;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.BreadthFirstPaths;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SeparateChainingHashST;

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
		
		this.buildGraph();
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
	
	private Graph positionGraph = null;
	public Graph getPositionGraph() {
		return this.positionGraph;
	}
	
	private ArrayList<Position> vertexToPosition = new ArrayList<>();
	public Position getVertexToPosition(int vertex) {
		return this.vertexToPosition.get(vertex);
	}
	
	private SeparateChainingHashST<Position, Integer> positionToVertex = new SeparateChainingHashST<>();
	
	private static record Pair<Type>(Type first, Type second) {}
	private SeparateChainingHashST<Pair<Position>, Direction> positionPairToDirection = new SeparateChainingHashST<>();
	private void buildGraph() {
		
		/// Build out the dynamic adjacency list.
		// NOTE: The algs4 {@code Graph} has a fixed adjacent array, which is fundamentally incompatible with buildGraph(). As such, an
		// adjacency list is built first and then converted into a Graph to BreadthFirstPaths can read it.
		ArrayList<ArrayList<Integer>> vertexToAdjacentVertices = new ArrayList<>();	
		Queue<Position> positionsToBuild = new Queue<>();
		positionsToBuild.enqueue(new Position(this.winPoint));
		for(Position position : positionsToBuild) {
			if(this.isFailurePosition(position) || this.isWinPosition(position)) {
				continue;
			}
			
			Integer vertex = this.positionToVertex.get(position);
			if(vertex != null && vertexToAdjacentVertices.get(vertex).size() > 0) {
				continue;
			}
			
			for(Direction direction : Direction.values()) {
				Position newPosition = position.goToDirection(direction);
				if(this.isFailurePosition(newPosition) || this.positionToVertex.contains(newPosition)) {
					continue;
				}
				
				this.positionPairToDirection.put(new Pair<>(position, newPosition), direction);
				this.positionPairToDirection.put(new Pair<>(newPosition, position), direction.flip());
				
				Integer newVertex = this.positionToVertex.get(position);
				if(newVertex == null) {
					newVertex = vertexToAdjacentVertices.size();
					vertexToAdjacentVertices.add(new ArrayList<Integer>());
					this.vertexToPosition.add(position);
					this.positionToVertex.put(position, newVertex);
				}
				
				vertexToAdjacentVertices.get(this.positionToVertex.get(position)).add(newVertex);
				positionsToBuild.enqueue(newPosition);
			}
		}
		
		/// Use that dynamic adjacency list and put it into a graph.
		this.positionGraph = new Graph(vertexToAdjacentVertices.size());
		for(int i = 0; i < vertexToAdjacentVertices.size(); i++) {
			ArrayList<Integer> adjacentVertices = vertexToAdjacentVertices.get(i);
			for(Integer adjacentVertex : adjacentVertices) {
				this.positionGraph.addEdge(i, adjacentVertex);
			}
		}
	}
	
	public Queue<Direction> solveBoard(Position startPosition) {
		Queue<Direction> result = new Queue<>();
		int startVertex = this.positionToVertex.get(startPosition);
		BreadthFirstPaths bfs = new BreadthFirstPaths(this.positionGraph, startVertex);

		int pastVertex = -1;
		for (Integer vertex : bfs.pathTo(startVertex)) {

			// INVARIANT: edges is in order starting from the start (or current) position
			// and will go to the end position in that order. I don't know if this can be
			// guaranteed.
			Position pastPosition = this.vertexToPosition.get(pastVertex);
			Position currentPosition = this.vertexToPosition.get(vertex);
			result.enqueue(this.positionPairToDirection.get(new Pair<>(pastPosition, currentPosition)));
			
			pastVertex = vertex;
		}
		
		return result;
	}
}

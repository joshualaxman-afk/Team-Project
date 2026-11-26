package TeamProject;

import java.awt.Color;

import edu.princeton.cs.algs4.StdDraw;

public class UI {

	private static final int CELL_PIXELS = 50;

    // Call once before the game starts
    public static void init(Board board) {
        int w = board.getWidth();
        int h = board.getHeight();
        StdDraw.setCanvasSize(w * CELL_PIXELS, h * CELL_PIXELS);
        // Cell centers land on integer coordinates
        StdDraw.setXscale(-0.5, w - 0.5);
        StdDraw.setYscale(-0.5, h - 0.5);
        StdDraw.enableDoubleBuffering(); // avoids flicker
    }

    public static void drawScreen(Block block, Board board) {
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
    
    public static void winScreen() {
    	StdDraw.setPenColor(Color.MAGENTA);
    	StdDraw.filledSquare(0, 0, 1);
    }
    
    public static void loseScreen() {
    	StdDraw.setPenColor(Color.red);
    	StdDraw.filledSquare(0, 0, 1);
    }
    
    public static void titleScreen() {
    	
    }
    
    public static void graphScreen() {
    	
    }
}

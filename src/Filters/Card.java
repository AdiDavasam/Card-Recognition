package Filters;

import java.util.ArrayList;

public class Card {
    private short[][] cardGrid;
    private int startRow, startCol, width, height;
    private int topLeft, bottomLeft, topRight, bottomRight;
    ArrayList<Point> allPoints;
    Point controlPoint;

    public Card(int startRow, int startCol, int height, int width) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.width = width;
        this.height = height;
        short white = 255;
        controlPoint = new Point(white,white,white,0,0);
    }

    public void copySubGrid(short[][] originalGrid) {
        cardGrid = new short[height+1][width+1];
        for (int row = startRow; row <= startRow + height; row++) {
            for (int col = startCol; col <= startCol + width; col++) {
                cardGrid[row - startRow][col-startCol] = originalGrid[row][col];
            }
        }
        makePointList();
    }

    public int getTopLeft() {
        for (Point p : allPoints) {
            if (p)
        }
        return topLeft;
    }

    public int getBottomLeft() {
        return bottomLeft;
    }

    public int getTopRight() {
        return topRight;
    }

    public int getBottomRight() {
        return bottomRight;
    }

    public void makePointList(){
        ArrayList<Point> points = new ArrayList<>();
        for (int row = 0; row <= height; row++) {
            for (int col = 0; col <= width; col++) {
                points.add(new Point(cardGrid[row][col], cardGrid[row][col],cardGrid[row][col],row,col));
            }
        }
    }
}

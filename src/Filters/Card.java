package Filters;

import java.util.ArrayList;

public class Card {
    private short[][] cardGrid;
    private int startRow, startCol, width, height;
    private Point topLeft, bottomLeft, topRight, bottomRight;
    ArrayList<Point> allPoints;
    Point controlPoint;

    public Card(int startRow, int startCol, int height, int width) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.width = width;
        this.height = height;
        short white = 255;
        short black = 0;
        controlPoint = new Point(white,white, white,0,0);
        topLeft = new Point(black,black,black,0,0);
        bottomLeft = new Point(black,black,black,0,0);
        topRight = new Point(black,black,black,0,0);
        bottomRight = new Point(black,black,black,0,0);
        cardGrid = new short[height][width];
        allPoints = new ArrayList<>();

    }

    public void copySubGrid(short[][] originalGrid) {
        cardGrid = new short[height][width];

        for (int row = startRow; row < startRow + height - 1; row++) {
            for (int col = startCol; col < startCol + width - 1; col++) {
                cardGrid[row - startRow][col-startCol] = originalGrid[row][col];
            }
//            System.out.println(row);
        }
//        makePointList();
    }

    public void assignTopLeft() {
        controlPoint.setRow(0);
        controlPoint.setCol(0);
        double closest = Double.MAX_VALUE;
        int closestIndex = -1;
        for (int i = 0; i < allPoints.size(); i++) {
            Point p = allPoints.get(i);
            double dist = p.distanceToOtherPoint(controlPoint);
            if (dist < closest && p.getR() > 230) {
                closest = dist;
                closestIndex = i;
            }
        }
//        System.out.println("Index: " + closestIndex);
        topLeft = allPoints.get(closestIndex);
//        System.out.println("Position: " + topLeft.getCol() + "," + topLeft.getRow());
    }

    public void assignBottomLeft() {
        controlPoint.setRow(height);
        controlPoint.setCol(0);
        double closest = Double.MAX_VALUE;
        int closestIndex = -1;
        for (int i = 0; i < allPoints.size(); i++) {
            Point p = allPoints.get(i);
            double dist = p.distanceToOtherPoint(controlPoint);
            if (dist < closest && p.getR() == 255) {
                closest = dist;
                closestIndex = i;
            }
        }
        bottomLeft = allPoints.get(closestIndex);
    }

    public void assignTopRight() {
        controlPoint.setRow(0);
        controlPoint.setCol(width);
        double closest = Double.MAX_VALUE;
        int closestIndex = -1;
        for (int i = 0; i < allPoints.size(); i++) {
            Point p = allPoints.get(i);
            double dist = p.distanceToOtherPoint(controlPoint);
            if (dist < closest && p.getR() > 230) {
                closest = dist;
                closestIndex = i;
            }
        }
        topRight = allPoints.get(closestIndex);
    }

    public void assignBottomRight() {
        controlPoint.setRow(height);
        controlPoint.setCol(width);
        double closest = Double.MAX_VALUE;
        int closestIndex = -1;
        for (int i = 0; i < allPoints.size(); i++) {
            Point p = allPoints.get(i);
            double dist = p.distanceToOtherPoint(controlPoint);
            if (dist < closest && p.getR() > 230) {
                closest = dist;
                closestIndex = i;
            }
        }
        bottomRight = allPoints.get(closestIndex);
    }
    public void makePointList(){
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                allPoints.add(new Point(cardGrid[row][col], cardGrid[row][col],cardGrid[row][col],row,col));
            }
        }
    }

    public Point getBottomRight() {
        return bottomRight;
    }

    public Point getTopRight() {
        return topRight;
    }

    public Point getBottomLeft() {
        return bottomLeft;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public int getStartRow() {
        return startRow;
    }

    public int getStartCol() {
        return startCol;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

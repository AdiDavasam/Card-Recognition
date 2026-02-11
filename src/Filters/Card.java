package Filters;

import java.util.ArrayList;

public class Card {
    private short[][] cardGridBW, colorGridR, colorGridG, colorGridB;
    private int startRow, startCol, width, height;
    private Point topLeft, bottomLeft, topRight, bottomRight;
    private String shapeColor;
    private final int WHITE = 255, BLACK = 0;
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
        cardGridBW = new short[height][width];
        colorGridR = new short[height][width];
        colorGridG = new short[height][width];
        colorGridB = new short[height][width];
        allPoints = new ArrayList<>();

    }

    public void copySubGridBW(short[][] originalGrid) {

        for (int row = startRow; row < startRow + height - 1; row++) {
            for (int col = startCol; col < startCol + width - 1; col++) {
                cardGridBW[row - startRow][col-startCol] = originalGrid[row][col];
            }
//            System.out.println(row);
        }
//        makePointList();
    }

    public void copyColorGrid(short[][] redGrid, short[][] greenGrid, short[][] blueGrid) {
        for (int row = startRow; row < startRow + height - 1; row++) {
            for (int col = startCol; col < startCol + width - 1; col++) {
                colorGridR[row - startRow][col-startCol] = redGrid[row][col];
                colorGridG[row - startRow][col-startCol] = greenGrid[row][col];
                colorGridB[row - startRow][col-startCol] = blueGrid[row][col];
            }
        }
    }

    public void getCardColor() {
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        for (int row = 0; row < colorGridR.length; row++) {
            for (int col = 0; col < colorGridR[0].length; col++) {
                if (colorGridR[row][col] > 185 && colorGridG[row][col] > 185 && colorGridB[row][col] > 185) continue;
                if (colorGridR[row][col] < 80 && colorGridG[row][col] < 80 && colorGridB[row][col] < 80) continue;

                redSum+= colorGridR[row][col];
                greenSum+= colorGridG[row][col];
                blueSum+= colorGridB[row][col];
            }
        }
        if (greenSum > redSum && greenSum > blueSum) shapeColor = "Green";
        if (blueSum > greenSum && blueSum > redSum) shapeColor = "Purple";
        if (redSum > greenSum && (double)blueSum/redSum >= 0.85) shapeColor = "Purple"; //bc purple sometimes has a bit mroe red than blue
        if (redSum > greenSum && (double)blueSum/redSum < 0.85) shapeColor = "Red";
    }

    public int findAmountOfShapes() {
        int counter = 0;
        int checkingColor = WHITE;
        boolean lookingForWhite = true;
        int checkingRow = height/2;
//        System.out.println("Row: " + (checkingRow + startRow));
        for (int col = width/6; col < width - width/6; col++) {
            if (lookingForWhite && cardGridBW[checkingRow][col] == WHITE) {
                counter++;
                lookingForWhite = false;
            }
            if (!lookingForWhite && cardGridBW[checkingRow][col] == BLACK) lookingForWhite = true;
        }
        return counter/2;
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
                allPoints.add(new Point(cardGridBW[row][col], cardGridBW[row][col], cardGridBW[row][col],row,col));
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

    public String getShapeColor() {
        return shapeColor;
    }
}

package Filters;

public class Point {
    private short r,g,b, color;
    private int row, col;
    public Point(short r, short g, short b, int row, int col) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.row = row;
        this.col = col;
    }

    public double colorDistanceTo(Point other) {
        double deltaRed = (this.r - other.r)*(this.r - other.r);
        double deltaGreen = (this.g - other.g)*(this.g - other.g);
        double deltaBlue = (this.b - other.b)*(this.b - other.b);
        return Math.sqrt(deltaRed + deltaGreen + deltaBlue);
    }

    public double distanceToOtherPoint(Point other) {
        double deltaR = (this.row - other.row)*(this.row - other.row);
        double deltaC = (this.col - other.col)*(this.col - other.col);
        return Math.sqrt(deltaR + deltaC);
    }

    public short getR() {
        return r;
    }

    public void setR(short r) {
        this.r = r;
    }

    public short getG() {
        return g;
    }

    public void setG(short g) {
        this.g = g;
    }

    public short getB() {
        return b;
    }

    public void setB(short b) {
        this.b = b;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public short getColor() {
        color = (short)((double)(r+g+b)/3);
        return color;
    }

    public void setColor(short color) {
        this.color = color;
    }
}

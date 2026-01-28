package Filters;

import java.util.ArrayList;

public class Cluster {
    private ArrayList<Point> points;
    private Point center;
    public Cluster(int row, int col) {
        center = new Point((short) 100, (short) 100, (short) 100,row,col);
        points = new ArrayList<>();
    }

    public void addPoint(Point p) {
        points.add(p);
    }

    public void reCalculateCenter() {
        if(points.isEmpty()) return;
        double red = 0;
        double green = 0;
        double blue = 0;
        for (Point p: points) {
            red += p.getR();
            green += p.getG();
            blue += p.getB();
        }
        red /= points.size();
        green /= points.size();
        blue /= points.size();

        center.setR((short) red);
        center.setG((short) green);
        center.setB((short) blue);
    }
    public void clear() {
        points.clear();
    }

    public Point getCenter(){
        return center;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public int getSize(){
        return points.size();
    }

}

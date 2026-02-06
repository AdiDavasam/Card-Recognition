package Filters;

import Interfaces.Drawable;
import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;
import processing.core.PApplet;

import java.util.ArrayList;

public class CardFilter implements PixelFilter, Interactive, Drawable {
    short threshold;
    short[][] red, green, blue, grid;
    ArrayList<Cluster> clusters;
    ArrayList<Point> points;
    int numberOfCardsHeight;
    int numberOfCardsWidth;
    Card a;
    ArrayList<Card> cards;

    public CardFilter() {
        threshold = 190;
        clusters = new ArrayList<>();
        points = new ArrayList<>();
        numberOfCardsHeight = 3;
        numberOfCardsWidth = 3;
        cards = new ArrayList<>();
//        initClusters();
//        a = new Card(46,26, 164, 153);
    }
    @Override
    public DImage processImage(DImage img) {
        red = img.getRedChannel();
        green = img.getGreenChannel();
        blue = img.getBlueChannel();
        grid = img.getBWPixelGrid();
        colorMaskAtThreshold(threshold);
        makeColorChannelsGray();
        initCards();


        //initClusters();
        makePointList();//all points in grid into points
//        a.copySubGrid(red);
//        a.makePointList();

        initCards();
        for (Card a : cards) {
            a.copySubGrid(red);
            a.makePointList();
        }

//        for (int i = 0; i < 10; i++) {
//            reCalculateClusterCenters();//moves all the clusters to center
//            reAssignPixelColors();
//            clearClusters();
//            System.out.println("Number " + i + ":");
//        }

//        printPointsInCluster(clusters.get(0));
        //printPointRGB(clusters.get(1).getCenter());


        for (Card a : cards) {
            a.assignTopLeft();
            a.assignBottomLeft();
            a.assignBottomRight();
            a.assignTopRight();
        }



//        img.setPixels(grid);
//        return img;
//
//        System.out.println("Cluster size: " + clusters.size());
//        reAssignPixelColors();
        img.setColorChannels(red, green, blue);
        return img;
    }

    public void initCards() {
        ArrayList<Integer> columnSplits = new ArrayList<>();
        ArrayList<Integer> rowSplits = new ArrayList<>();
        rowSplits.add(0);
        columnSplits.add(0);
        boolean white = true;
        boolean black = false;
        boolean color = true;

        for (int i = 0; i < red.length; i++) {
            if(color == white && addAllValuesInRow(red[i])/(double)(red[0].length) >= 0.2) {
                color = black;
                rowSplits.add(i);
            } else if(color == black && addAllValuesInRow(red[i])/(double)(red[0].length) <= 0.7) {
                color = white;
                rowSplits.add(i);
            }
        }

        for (int i = 0; i < red[0].length; i++) {
            if(color == white && addAllValuesInCol(red,i)/(double)(red.length) >= 0.2) {
                color = black;
                columnSplits.add(i);
            } else if(color == black && addAllValuesInCol(red,i)/(double)(red.length) <= 0.7) {
                color = white;
                columnSplits.add(i);
            }
        }
        rowSplits.add(red.length-1);
        columnSplits.add(red[0].length-1);
//        for (int i = 0; i < columnSplits.size(); i++) {
//            System.out.println(columnSplits.get(i));
//        }
//        System.out.println();
        for (int i = 0; i < rowSplits.size() / 2; i+=2) {
            int startRow = (rowSplits.get(i) + rowSplits.get(i+1))/2;
            int height = rowSplits.get(i+1) - rowSplits.get(i); // this is not the height of the card only between wrong this
            for (int j = 0; j < columnSplits.size() / 2; j+=2) {
                int startCol = (columnSplits.get(i) + columnSplits.get(i+1))/2;
                int width = columnSplits.get(i+1) - columnSplits.get(i);
                cards.add(new Card(startRow,startCol,height,width));
            }
        }
    }

    public int addAllValuesInRow(short[] arr) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum;
    }

    public int addAllValuesInCol(short[][] arr, int col) {
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i][col];
        }
        return sum;
    }

    public void makeColorChannelsGray() {
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col <grid[0].length; col++) {
                red[row][col] = grid[row][col];
                green[row][col] = grid[row][col];
                blue[row][col] = grid[row][col];
            }
        }
    }

    public void printPointsInCluster(Cluster cluster) {
        for (Point p : cluster.getPoints()) {
            System.out.println("Row: " + p.getRow() +"\tCol: " + p.getCol());
        }
    }

    public void colorMaskAtThreshold(int threshold) {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[r].length; c++) {
                if (grid[r][c] > threshold) {
                    grid[r][c] = 255;
                } else {
                    grid[r][c] = 0;
                }
            }
        }
    }

    public void printPointRGB(Point p) {
        System.out.print("Red: " + p.getR() + " ");
        System.out.print("Green: " + p.getG() + " ");
        System.out.print("Blue: " + p.getB() + " ");
        System.out.println();
    }

    public void printColorChannels() {
        for (int i = 0; i < red.length; i++) {
            for (int j = 0; j < red[0].length; j++) {
                System.out.print("Red: " + red[i][j] + " ");
                System.out.print("Green: " + green[i][j] + " ");
                System.out.print("Blue: " + blue[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void reAssignPixelColors() {
        for (Cluster c : clusters) {
            for (Point point : c.getPoints()) {
                red[point.getRow()][point.getCol()] = c.getCenter().getR();
                green[point.getRow()][point.getCol()] = c.getCenter().getG();
                blue[point.getRow()][point.getCol()] = c.getCenter().getB();
            }
        }
    }

    public void reCalculateClusterCenters() {
        for (int i = 0; i < points.size(); i++) {
            double max = points.get(i).distanceToOtherPoint(clusters.get(0).getCenter())+1;
            int indexOfHighest = 0;
            for (int j = 0; j < clusters.size(); j++) {
                if(points.get(i).distanceToOtherPoint(clusters.get(j).getCenter()) < max && points.get(i).getColor() > 200){
                    max = points.get(i).distanceToOtherPoint(clusters.get(j).getCenter());
                    indexOfHighest = j;
                }
            }
            clusters.get(indexOfHighest).addPoint(points.get(i));
        }
        for (int i = 0; i < clusters.size(); i++) {
            clusters.get(i).reCalculateCenter();
        }
    }

    public void initClusters() {
        int heightBetweenCards = (grid.length/numberOfCardsHeight);
        int lengthBetweenCards = (grid[0].length/numberOfCardsWidth);
        for (int i = 0; i < numberOfCardsHeight; i++) {
            for (int j = 1; j <= numberOfCardsWidth; j++) {
                clusters.add(new Cluster(heightBetweenCards * i, lengthBetweenCards * j));
            }
        }
    }

    public void initClusters(int amt) {
        for (int i = 0; i < amt; i++) {
            clusters.add(new Cluster((short)(Math.random() * grid.length),(short)(Math.random() * grid[0].length)));
        }
    }

    public void clearClusters() {
        for (int i = 0; i < clusters.size(); i++) {
            clusters.get(i).clear();
        }
    }

    public void makePointList(){
        for (int row = 0; row < red.length; row++) {
            for (int col = 0; col < red[0].length; col++) {
                points.add(new Point(red[row][col], green[row][col],blue[row][col],row,col));
            }

        }
    }
    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage original, DImage filtered) {

    }
    @Override
    public void keyPressed(char key) {
        if (key == '-' && threshold - 5 >=0) {
            threshold -= 5;
            System.out.println("Threshold: " + threshold);
        }
        if (key == '=' && threshold + 5 <=255) {
            threshold += 5;
            System.out.println("Threshold: " + threshold);
        }


    }
    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        window.fill(255, 0, 0);
//        window.ellipse(original.getWidth(), original.getHeight(), 10, 10);

        window.fill(255,0,0);
//        for (int i = 0; i < clusters.size(); i++) {
//            window.ellipse(clusters.get(i).getCenter().getRow(),clusters.get(i).getCenter().getCol(), 5,5);
//        }
//        System.out.println("Start Row: " + (a.getStartRow() + a.getBottomLeft().getRow()));
//        System.out.println("Start Col: " + (a.getStartCol() + a.getBottomLeft().getCol()));
        for (Card a : cards) {
            window.ellipse(a.getTopLeft().getCol() + a.getStartCol(), a.getTopLeft().getRow() + a.getStartRow(), 20, 20);
            window.ellipse(a.getBottomLeft().getCol() + a.getStartCol(), a.getBottomLeft().getRow() + a.getStartRow(), 20, 20);
            window.ellipse(a.getTopRight().getCol() + a.getStartCol(), a.getTopRight().getRow() + a.getStartRow(), 20, 20);
            window.ellipse(a.getBottomRight().getCol() + a.getStartCol(), a.getBottomRight().getRow() + a.getStartRow(), 20, 20);
        }
    }
}


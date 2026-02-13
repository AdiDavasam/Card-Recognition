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
    ArrayList<Point> points;
    int numberOfCardsHeight;
    int numberOfCardsWidth;
    ArrayList<Card> cards;
    boolean[] showCorners;

    public CardFilter() {
        threshold = 190;
        points = new ArrayList<>();
        numberOfCardsHeight = 3;
        numberOfCardsWidth = 3;
        cards = new ArrayList<>();
        showCorners = new boolean[9];
        for (int i = 0; i < showCorners.length; i++) {
            showCorners[i] = false;
        }
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
        makePointList();//all points in grid into points

        for (Card a : cards) {
            a.copySubGridBW(red);
            a.makePointList();
            a.copyColorGrid(img.getRedChannel(), img.getGreenChannel(), img.getBlueChannel());
        }

        for (Card a : cards) {
            a.assignTopLeft();
            a.assignBottomLeft();
            a.assignBottomRight();
            a.assignTopRight();
            a.getCardColor();
            System.out.println(a.getShapeColor() + "\t" + a.findAmountOfShapes());
        }



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
//        for (int i = 0; i < rowSplits.size(); i++) {
//            System.out.println("rows: " + rowSplits.get(i));
//        }
//        for (int i = 0; i < columnSplits.size(); i++) {
//            System.out.println("cols: " + columnSplits.get(i));
//        }

        for (int i = 0; i < rowSplits.size() - 2; i+=2) {
            int startRow = (rowSplits.get(i) + rowSplits.get(i+1))/2;
            int height = rowSplits.get(i+2) - rowSplits.get(i); // this is not the height of the card only between wrong this
            for (int j = 0; j < columnSplits.size() - 2; j+=2) {
                int startCol = (columnSplits.get(j) + columnSplits.get(j+1))/2;
                int width = columnSplits.get(j+2) - columnSplits.get(j);
//                System.out.println("Row: " + startRow + " Col: " + startCol + " h: " + height + " w: " + width );
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
        if (key == '1') showCorners[0] = !showCorners[0];
        if (key == '2') showCorners[1] = !showCorners[1];
        if (key == '3') showCorners[2] = !showCorners[2];
        if (key == '4') showCorners[3] = !showCorners[3];
        if (key == '5') showCorners[4] = !showCorners[4];
        if (key == '6') showCorners[5] = !showCorners[5];
        if (key == '7') showCorners[6] = !showCorners[6];
        if (key == '8') showCorners[7] = !showCorners[7];
        if (key == '9') showCorners[8] = !showCorners[8];


    }
    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
        window.fill(0);
        for (int i = 0; i < cards.size(); i ++) {
            Card a = cards.get(i);
            if(a.getShapeColor().equals("Purple")) window.fill(105,55,165);
            if(a.getShapeColor().equals("Green")) window.fill(35,160,75);
            if(a.getShapeColor().equals("Red")) window.fill(230,65,50);
            if (showCorners[i]) {
                int circleSize = 5 * (a.findAmountOfShapes() + 1);
                window.ellipse(a.getTopLeft().getCol() + a.getStartCol(), a.getTopLeft().getRow() + a.getStartRow(), circleSize, circleSize);
                window.ellipse(a.getBottomLeft().getCol() + a.getStartCol(), a.getBottomLeft().getRow() + a.getStartRow(), circleSize, circleSize);
                window.ellipse(a.getTopRight().getCol() + a.getStartCol(), a.getTopRight().getRow() + a.getStartRow(), circleSize, circleSize);
                window.ellipse(a.getBottomRight().getCol() + a.getStartCol(), a.getBottomRight().getRow() + a.getStartRow(), circleSize, circleSize);
            }
        }
    }
}


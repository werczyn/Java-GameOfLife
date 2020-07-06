package GameOfLife;

import javax.swing.*;
import java.awt.*;

public class Main {

    //frame size
    private static int width=700;
    private static int height=700;

    private static int[][] grid;
    private static int cols;
    private static int rows;
    private static int resolution = 30;
    private static JPanel center;



    public static void main(String[] args) {

        cols = width/resolution;
        rows = height/resolution;
        uruchomGUI();
    }

    private static void uruchomGUI(){
        JFrame frame = new JFrame("Game of life");
        center = new JPanel(new GridLayout(rows,cols));
        center.setPreferredSize(new Dimension(width,height));
        JPanel bottom = new JPanel(new FlowLayout());

        grid = make2DArray(cols,rows);
        //makeArrayRandom();
        draw();

        JTextField textField = new JTextField("1");
        textField.setPreferredSize(new Dimension(40,20));


        JButton buttonStart = new JButton("Start");


        buttonStart.addActionListener(e -> {
            boolean isNumeric = true;
            int iterator = 0;
            try {
                iterator = Integer.parseInt(textField.getText().trim());
            }catch (NumberFormatException ex){
                isNumeric=false;
                System.err.println("To nie jest liczba");
            }
            if (isNumeric){
                for (int i = 0; i < iterator; i++) {
                    center.removeAll();
                    checkNext();
                    draw();

                }
            }
        });
        bottom.add(buttonStart);
        bottom.add(textField);

        frame.add(center, BorderLayout.CENTER);
        frame.add(bottom,BorderLayout.PAGE_END);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static  int[][] make2DArray(int cols, int rows){
        int[][] arr = new int[cols][];
        for (int i=0; i<arr.length; i++){
            arr[i] = new int[rows];
        }
        return arr;
    }

    private static void makeArrayRandom(){
        for (int i=0; i<cols;i++){
            for (int j=0; j<rows;j++){
                grid[i][j] = (int)(Math.random()*2);
            }
        }
    }

    private static void draw() {

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                int x = i * resolution;
                int y = j * resolution;
                JButton button = new JButton();
                if (grid[i][j] == 1) {
                    button.setBackground(Color.BLACK);
                } else {
                    button.setBackground(Color.WHITE);
                }
                center.add(button);
                button.setName(""+i+"\t"+j);
                button.addActionListener(e -> {
                    String[] tab = button.getName().split("\t");
                    int a = Integer.parseInt(tab[0]);
                    int b = Integer.parseInt(tab[1]);
                    if (grid[a][b]==0){
                        grid[a][b]=1;
                        button.setBackground(Color.BLACK);
                    }else if (grid[a][b]==1){
                        grid[a][b]=0;
                        button.setBackground(Color.WHITE);
                    }
                });
            }
        }
        center.revalidate();
        center.repaint();


    }

    private static void checkNext(){
        int[][] next = make2DArray(cols,rows);
        for (int i=0; i<cols;i++){
            for (int j=0; j<rows;j++){
                int state = grid[i][j];

                if (i==0 || i==cols-1 || j==0 || j==rows-1){
                    next[i][j]=state;
                }else {
                    int neighbours = count(grid, i, j);
                    if (state == 0 && neighbours == 3) {
                        next[i][j] = 1;
                    } else if (state == 1 && (neighbours < 2 || neighbours > 3)) {
                        next[i][j] = 0;
                    } else {
                        next[i][j] = state;
                    }
                }
            }
        }
        grid = next;
    }

    private static int count(int[][] grid, int x, int y) {
        int sum=0;
        for(int i=-1; i<2;i++){
            for (int j = -1; j <2 ; j++) {
                sum+=grid[x+i][y+j];
            }
        }
        sum-=grid[x][y];
        return sum;
    }
}


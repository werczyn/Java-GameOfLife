import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import jdk.jfr.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Main extends Application {

    private Group root;
    private int[][] grid;
    private int cols;
    private int rows;
    private int resolution = 15;
    private Rectangle[][] rectangles;

    @Override
    public void start(Stage primaryStage) {
        root = new Group();
        HBox hBox = new HBox();
        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 500,500);
        cols = (int) (scene.getWidth()/resolution);
        rows = (int) (scene.getHeight()/resolution);
        rectangles = new Rectangle[cols][rows];

        grid = make2DArray(cols,rows);
        for (int i=0; i<cols;i++){
            for (int j=0; j<rows;j++){
                grid[i][j] = (int)(Math.random()*2);
            }
        }
        draw();
        Button button = new Button("START");
        TextArea textArea = new TextArea();
        textArea.setMaxSize(250,10);
        root.getChildren().add(button);

        //sprawdzam losowe rysowanie
        for (int[] rectangles : grid) {
            for (int rectangle : rectangles) {
                System.out.print(rectangle+" ");
            }
            System.out.println();
        }


        button.setOnAction(event -> {
            int iterator = Integer.parseInt(textArea.getText().trim());
            for (int i = 0; i < iterator; i++) {

                draw();
            }
        });

        primaryStage.setResizable(false);
        borderPane.setCenter(root);
        hBox.getChildren().addAll(button,textArea);
        borderPane.setBottom(hBox);
        primaryStage.setHeight(580);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Game of life");
        primaryStage.show();
    }

    private int[][] make2DArray(int cols, int rows){
        int[][] arr = new int[cols][];
        for (int i=0; i<arr.length; i++){
            arr[i] = new int[rows];
        }
        return arr;
    }

    private void draw(){

        for (int i=0; i<cols;i++){
            for (int j=0; j<rows;j++){
                int x = i*resolution;
                int y = j*resolution;
                Rectangle rect = new Rectangle(x,y,resolution,resolution);
                rect.setStroke(Color.GREY);
                if (grid[i][j] == 1){
                    rect.setFill(Color.BLACK);
                }else {
                    rect.setFill(Color.WHITESMOKE);
                }
                rectangles[i][j]=rect;
                rect.setId(""+i+"\t"+j);
                rect.setOnMouseClicked(event -> {
                    String  tab[]=rect.getId().split("\t");
                    int a = Integer.parseInt(tab[0]);
                    int b = Integer.parseInt(tab[1]);
                    if (grid[a][b]==0){
                        rect.setFill(Color.BLACK);
                        grid[a][b]=1;
                    }else {
                        rect.setFill(Color.WHITESMOKE);
                        grid[a][b]=0;
                    }
                });
                root.getChildren().add(rect);
            }
        }

        int[][] next = make2DArray(cols,rows);

        for (int i=0; i<cols;i++){
            for (int j=0; j<rows;j++){
                int state = grid[i][j];

                if (i==0 || i==cols-1 || j==0 || j==rows-1){
                    next[i][j]=state;
                }else {
                    int sum = 0;
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

    private int count(int[][] grid, int x, int y) {
        int sum=0;
        for(int i=-1; i<2;i++){
            for (int j = -1; j <2 ; j++) {
                sum+=grid[x+i][y+j];
            }
        }
        sum-=grid[x][y];
        return sum;
    }

    public static void main(String[] args) {
        launch(args);
    }

}

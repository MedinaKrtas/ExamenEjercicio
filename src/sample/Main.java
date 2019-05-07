package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        var ref = new Object() {
            Thread thread = null;
        };

        final int[] sumaDados = new int[1];
        final boolean[] gano = {false};
        final int[] tiempoRestante = new int[1];

        AnchorPane root = new AnchorPane();
        Label lbl1 = new Label("Juego de dados");
        lbl1.setLayoutX(230);

        Label lbl2 = new Label("Suma de puntos:");
        lbl2.setLayoutX(180);
        lbl2.setLayoutY(30);

        Label lbl3 = new Label();
        lbl3.setLayoutX(250);
        lbl3.setLayoutY(350);

        Label lbl4 = new Label();
        lbl4.setTextFill(Color.rgb(255,0,0));
        lbl4.setFont(Font.font("arial",20));
        lbl4.setLayoutX(80);
        lbl4.setLayoutY(400);

        TextField textField = new TextField();
        textField.setLayoutX(270);
        textField.setLayoutY(30);


        Button btn1 = new Button("Lanzar Dados");
        btn1.setLayoutY(80);
        btn1.setLayoutX(230);
        btn1.setDisable(true);
        btn1.setOnMousePressed((MouseEvent evt)->{
            int dado1 = (int)(Math.random()*6)+1;
            int dado2 = (int)(Math.random()*6)+1;

            sumaDados[0] = dado1+dado2;

            textField.setText(String.valueOf(sumaDados[0]));

            if(sumaDados[0] == 7){
                gano[0] = true;
            }
        });


        Button btn2 = new Button("Iniciar");
        btn2.setLayoutY(150);
        btn2.setLayoutX(230);
        btn2.setOnMousePressed((MouseEvent evt)->{
            btn1.setDisable(false);
            if(ref.thread == null){
                lbl4.setText("");
                Task task = new Task() {
                    @Override
                    protected Object call() throws Exception {
                        int tiempo = 60;
                        while(tiempo >= 0 && !gano[0]){
                            int finalTiempo = tiempo;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    lbl3.setText(String.valueOf(finalTiempo));
                                }
                            });

                            Thread.sleep(100);
                            tiempoRestante[0] = tiempo;
                            tiempo--;
                        }

                        if(!gano[0]){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    lbl4.setTextFill(Color.rgb(255,0,0));
                                    lbl4.setText("Se ha acabado tu tiempo");
                                }
                            });
                        } else {
                            int finalTiempo1 = tiempo;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    lbl4.setTextFill(Color.rgb(0,0,255));
                                    lbl4.setText("Haz ganado,Tiempo restante: "+(finalTiempo1 +1));
                                }
                            });

                        }

                        ref.thread = null;
                        btn1.setDisable(true);
                        gano[0] = false;
                        return null;
                    }
                };
                ref.thread = new Thread(task);
                ref.thread.setDaemon(true);
                ref.thread.start();
            }
        });

        root.getChildren().add(lbl1);
        root.getChildren().add(lbl2);
        root.getChildren().add(lbl3);
        root.getChildren().add(lbl4);
        root.getChildren().add(textField);
        root.getChildren().add(btn1);
        root.getChildren().add(btn2);


        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }


}

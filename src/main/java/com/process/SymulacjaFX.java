package com.process;

import com.domain.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SymulacjaFX extends Application {

    private final List<PunktTransportu> punktyTransportu = new ArrayList<>();
    private final List<Sygnalizacja> sygnalizacje = new ArrayList<>();
    private final List<Pojazd> pojazdy = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        // Tworzenie przystanków i węzłów komunikacyjnych
        Wezel w1 = new Wezel("1", "Dworzec Główny", 100, 200);
        Wezel w2 = new Wezel("2", "Rynek", 300, 200);
        Wezel w3 = new Wezel("3", "Lotnisko", 500, 200);
        Wezel w4 = new Wezel("4", "Skrzyżowanie", 300, 400);
        Wezel w5 = new Wezel("5", "Galeria", 500, 400);
        Wezel w6 = new Wezel("6", "Nowy Węzeł", 500, 300);
        Wezel w7 = new Wezel("7", "Nowy Węzeł 2", 300, 300);
        Przystanek przystanek1 = new Przystanek("P1", "Przystanek 1", 200, 200);
        Przystanek przystanek2 = new Przystanek("P2", "Przystanek 2", 500, 350);

        // Dodawanie połączeń
        w1.dodajPolaczenie(przystanek1);
        przystanek1.dodajPolaczenie(w2);
        w2.dodajPolaczenie(w3);
        w2.dodajPolaczenie(w7);
        w7.dodajPolaczenie(w6);
        w7.dodajPolaczenie(w4);
        w4.dodajPolaczenie(w5);
        w3.dodajPolaczenie(w6);
        w6.dodajPolaczenie(przystanek2);
        przystanek2.dodajPolaczenie(w5);

        punktyTransportu.addAll(Arrays.asList(w1, w2, w3, w4, w5, w6, w7, przystanek1, przystanek2));

        for (PunktTransportu punkt : punktyTransportu) {
            Circle punktGraficzny = new Circle(punkt.getX(), punkt.getY(), 10, punkt instanceof Przystanek ? Color.GREEN : Color.BLUE);
            pane.getChildren().add(punktGraficzny);
        }

        // Tworzenie linii
        pane.getChildren().addAll(
                new Line(w1.getX(), w1.getY(), przystanek1.getX(), przystanek1.getY()),
                new Line(przystanek1.getX(), przystanek1.getY(), w2.getX(), w2.getY()),
                new Line(w2.getX(), w2.getY(), w3.getX(), w3.getY()),
                new Line(w2.getX(), w2.getY(), w7.getX(), w7.getY()),
                new Line(w7.getX(), w7.getY(), w6.getX(), w6.getY()),
                new Line(w7.getX(), w7.getY(), w4.getX(), w4.getY()),
                new Line(w4.getX(), w4.getY(), w5.getX(), w5.getY()),
                new Line(w3.getX(), w3.getY(), w6.getX(), w6.getY()),
                new Line(w6.getX(), w6.getY(), przystanek2.getX(), przystanek2.getY()),
                new Line(przystanek2.getX(), przystanek2.getY(), w5.getX(), w5.getY())
        );

        // Tworzenie sygnalizacji
        sygnalizacje.add(new Sygnalizacja(300, 200, 50, 30, pane));
        sygnalizacje.add(new Sygnalizacja(500, 200, 40, 60, pane));
        sygnalizacje.add(new Sygnalizacja(500, 300, 70, 50, pane));

        // Tworzenie pojazdów
        pojazdy.add(new Pojazd(w1, Arrays.asList(przystanek1, przystanek2), pane));

        // Uruchomienie animacji
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> aktualizujSymulacje()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setTitle("Symulacja Transportu Publicznego");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void aktualizujSymulacje() {
        for (Sygnalizacja sygnalizacja : sygnalizacje) {
            sygnalizacja.aktualizuj();
        }
        for (Pojazd pojazd : pojazdy) {
            pojazd.przemieszczaj(sygnalizacje);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


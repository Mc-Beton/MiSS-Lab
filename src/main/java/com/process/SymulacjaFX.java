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

import java.util.*;

public class SymulacjaFX extends Application {

    private final List<PunktTransportu> punktyTransportu = new ArrayList<>();
    private final List<Sygnalizacja> sygnalizacje = new ArrayList<>();
    private final List<Pojazd> pojazdy = new ArrayList<>();
    private final Map<PunktTransportu, Map<PunktTransportu, Double>> obciazeniaTras = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();

        // Tworzenie przystanków i węzłów komunikacyjnych
        Wezel w1 = new Wezel("1", "Dworzec Główny", 100, 200);
        Wezel w2 = new Wezel("2", "Rynek", 300, 200);
        Wezel w3 = new Wezel("3", "Lotnisko", 500, 200);
        Wezel w4 = new Wezel("4", "Skrzyżowanie", 300, 600);
        Wezel w5 = new Wezel("5", "Galeria", 500, 600);
        Wezel w6 = new Wezel("6", "Nowy Węzeł", 500, 400);
        Wezel w7 = new Wezel("7", "Nowy Węzeł 2", 300, 400);
        Wezel w8 = new Wezel("8", "Lotnisko", 700, 600);
        Wezel w9 = new Wezel("9", "Skrzyżowanie", 700, 400);
        Wezel w10 = new Wezel("10", "Galeria", 900, 400);
        Wezel w11 = new Wezel("11", "Nowy Węzeł", 900, 200);
        Wezel w12 = new Wezel("12", "Nowy Węzeł", 1100, 200);

        Przystanek przystanek1 = new Przystanek("P1", "Przystanek 1", 200, 200, 1);
        Przystanek przystanek2 = new Przystanek("P2", "Przystanek 2", 500, 500, 2);
        Przystanek przystanek3 = new Przystanek("P3", "Przystanek 3", 1000, 200, 3);

        // Dodawanie połączeń
        dodajPolaczenie(w1, przystanek1,1);
        dodajPolaczenie(przystanek1, w2,2);
        dodajPolaczenie(w2, w3,2);
        dodajPolaczenie(w2, w7,3);
        dodajPolaczenie(w7, w6,3);
        dodajPolaczenie(w7, w4,2);
        dodajPolaczenie(w4, w5,1);
        dodajPolaczenie(w3, w6,1);
        dodajPolaczenie(w6, przystanek2,1);
        dodajPolaczenie(przystanek2, w5,1);
        dodajPolaczenie(w5, w8,1);
        dodajPolaczenie(w8, w9,1);
        dodajPolaczenie(w9, w3,1);
        dodajPolaczenie(w9, w10,1);
        dodajPolaczenie(w9, w11,5);
        dodajPolaczenie(w10, w11,1);
        dodajPolaczenie(w11, przystanek3,1);
        dodajPolaczenie(przystanek3, w12,1);

        punktyTransportu.addAll(Arrays.asList(w1, w2, w3, w4, w5, w6, w7, w8, w9, w10, w11, w12, przystanek1, przystanek2, przystanek3));

        for (PunktTransportu punkt : punktyTransportu) {
            Circle punktGraficzny = new Circle(punkt.getX(), punkt.getY(), 10, punkt instanceof Przystanek ? Color.GREEN : Color.BLUE);
            pane.getChildren().add(punktGraficzny);
        }

        // Tworzenie linii
        for (PunktTransportu p1 : punktyTransportu) {
            for (PunktTransportu p2 : p1.getPolaczenia()) {
                double obciazenie = p1.getObciazeniePolaczenia(p2);
                Line line = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                line.setStroke(getKolorDlaObciazenia(obciazenie));
                pane.getChildren().add(line);
            }
        }

        // Tworzenie sygnalizacji
        sygnalizacje.add(new Sygnalizacja(300, 200, 70, 30, pane));
        sygnalizacje.add(new Sygnalizacja(500, 200, 80, 20, pane));
        sygnalizacje.add(new Sygnalizacja(500, 300, 70, 30, pane));
        sygnalizacje.add(new Sygnalizacja(700, 400, 60, 20, pane));

        // Tworzenie pojazdów
        Pojazd autobus = new Pojazd(w1, Arrays.asList(przystanek1, przystanek2, przystanek3), pane, sygnalizacje);
        autobus.ustawPredkosc(5);
        pojazdy.add(autobus);

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
            pojazd.przemieszczaj();
        }
    }

    private Color getKolorDlaObciazenia(double obciazenie) {
        switch ((int) obciazenie) {
            case 1: return Color.BLUE;
            case 2: return Color.GREEN;
            case 3: return Color.YELLOW;
            case 4: return Color.ORANGE;
            case 5: return Color.RED;
            default: return Color.GRAY;
        }
    }

    private void dodajPolaczenie(PunktTransportu p1, PunktTransportu p2, double obciazenie) {
        p1.dodajPolaczenie(p2, obciazenie);
        p2.dodajPolaczenie(p1, obciazenie);
    }

    private double kosztTrasy(PunktTransportu p1, PunktTransportu p2) {
        double odleglosc = Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
        double obciazenie = p1.getObciazeniePolaczenia(p2);
        return odleglosc + (obciazenie * 10); // Obciążenie ma drugorzędne znaczenie po odległości
    }

    public static void main(String[] args) {
        launch(args);
    }
}


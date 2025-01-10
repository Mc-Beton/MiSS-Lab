package com.domain;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Sygnalizacja {
    private boolean zielone = true;
    private int timer = 0;
    private final double x, y;
    private final Circle sygnalizacja;
    private final int czasZielone;
    private final int czasCzerwone;

    public Sygnalizacja(double x, double y, int czasZielone, int czasCzerwone, Pane pane) {
        this.x = x;
        this.y = y;
        this.czasZielone = czasZielone;
        this.czasCzerwone = czasCzerwone;
        this.sygnalizacja = new Circle(x, y, 5, Color.GREEN);
        pane.getChildren().add(sygnalizacja);
    }

    public boolean jestZielone() {
        return zielone;
    }

    public void aktualizuj() {
        timer++;
        if (zielone && timer >= czasZielone) {
            zielone = false;
            timer = 0;
            sygnalizacja.setFill(Color.RED);
        } else if (!zielone && timer >= czasCzerwone) {
            zielone = true;
            timer = 0;
            sygnalizacja.setFill(Color.GREEN);
        }
    }

    public boolean znajdujeSieBlisko(double pojazdX, double pojazdY) {
        double dystans = Math.sqrt(Math.pow(pojazdX - x, 2) + Math.pow(pojazdY - y, 2));
        return dystans < 20; // Załóżmy, że sygnalizacja wpływa na pojazdy w odległości 20 pikseli
    }
}

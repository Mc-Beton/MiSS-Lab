package com.domain;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.*;

public class Pojazd {
    private final Circle pojazd;
    private PunktTransportu aktualnyPunkt;
    private PunktTransportu celPunkt;
    private int postojTimer = 0;
    private final List<Przystanek> doOdwiedzenia;
    private double predkosc = 1.0;
    private final List<Sygnalizacja> sygnalizacje;
    private final Set<Sygnalizacja> mijaneSygnalizacje = new HashSet<>();

    public Pojazd(PunktTransportu start, List<Przystanek> przystanki, Pane pane, List<Sygnalizacja> sygnalizacje) {
        this.aktualnyPunkt = start;
        this.doOdwiedzenia = new ArrayList<>(przystanki);
        this.sygnalizacje = sygnalizacje;
        this.doOdwiedzenia.sort(Comparator.comparingInt(Przystanek::getKategoria));
        this.celPunkt = doOdwiedzenia.isEmpty() ? null : znajdzNajlepszaTrase(doOdwiedzenia.get(0));
        this.pojazd = new Circle(start.getX(), start.getY(), 8, Color.RED);
        pane.getChildren().add(pojazd);
    }

    public void ustawPredkosc(double nowaPredkosc) {
        this.predkosc = Math.max(0.1, nowaPredkosc);
    }

    private PunktTransportu znajdzNajlepszaTrase(PunktTransportu cel) {
        Map<PunktTransportu, Double> koszty = new HashMap<>();
        PriorityQueue<PunktTransportu> kolejka = new PriorityQueue<>(Comparator.comparingDouble(koszty::get));
        koszty.put(aktualnyPunkt, 0.0);
        kolejka.add(aktualnyPunkt);
        Map<PunktTransportu, PunktTransportu> poprzednicy = new HashMap<>();

        while (!kolejka.isEmpty()) {
            PunktTransportu obecny = kolejka.poll();
            if (obecny.equals(cel)) break;
            for (PunktTransportu sasiad : obecny.getPolaczenia()) {
                double dystans = obliczDystans(obecny, sasiad);
                double obciazenie = obecny.getObciazeniePolaczenia(sasiad);
                double sygnalizacjaKoszt = liczbaSygnalizacjiNaTrasie(obecny, sasiad) * 150;
                double koszt = dystans + (obciazenie * 100) + sygnalizacjaKoszt;

                if (!koszty.containsKey(sasiad) || koszty.get(sasiad) > koszty.get(obecny) + koszt) {
                    koszty.put(sasiad, koszty.get(obecny) + koszt);
                    poprzednicy.put(sasiad, obecny);
                    kolejka.add(sasiad);
                }
            }
        }

        PunktTransportu punkt = cel;
        while (poprzednicy.containsKey(punkt) && !poprzednicy.get(punkt).equals(aktualnyPunkt)) {
            punkt = poprzednicy.get(punkt);
        }
        return punkt;
    }

    private int liczbaSygnalizacjiNaTrasie(PunktTransportu start, PunktTransportu cel) {
        int licznik = 0;
        for (Sygnalizacja sygnalizacja : sygnalizacje) {
            if (sygnalizacja.znajdujeSieBlisko((start.getX() + cel.getX()) / 2, (start.getY() + cel.getY()) / 2)) {
                licznik++;
            }
        }
        return licznik;
    }

    private double obliczDystans(PunktTransportu p1, PunktTransportu p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }

    public void przemieszczaj() {
        if (postojTimer > 0) {
            postojTimer--;
            return;
        }

        for (Sygnalizacja sygnalizacja : sygnalizacje) {
            if (sygnalizacja.znajdujeSieBlisko(pojazd.getCenterX(), pojazd.getCenterY())) {
                if (!mijaneSygnalizacje.contains(sygnalizacja)) {
                    if (!sygnalizacja.jestZielone()) {
                        return; // Zatrzymaj pojazd na czerwonym świetle
                    }
                    mijaneSygnalizacje.add(sygnalizacja);
                }
            }
        }

        double dx = celPunkt.getX() - pojazd.getCenterX();
        double dy = celPunkt.getY() - pojazd.getCenterY();
        double dystans = Math.sqrt(dx * dx + dy * dy);
        double aktualnaPredkosc = predkosc / aktualnyPunkt.getObciazeniePolaczenia(celPunkt);

        if (dystans < aktualnaPredkosc) {
            pojazd.setCenterX(celPunkt.getX());
            pojazd.setCenterY(celPunkt.getY());
            aktualnyPunkt = celPunkt;
            if (aktualnyPunkt instanceof Przystanek) {
                postojTimer = 20;
                doOdwiedzenia.remove(aktualnyPunkt);
            }
            if (!doOdwiedzenia.isEmpty()) {
                celPunkt = znajdzNajlepszaTrase(doOdwiedzenia.get(0));
            }
        } else {
            pojazd.setCenterX(pojazd.getCenterX() + (dx / dystans) * aktualnaPredkosc);
            pojazd.setCenterY(pojazd.getCenterY() + (dy / dystans) * aktualnaPredkosc);
        }
    }
}

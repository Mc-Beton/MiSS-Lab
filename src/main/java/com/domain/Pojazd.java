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

    public Pojazd(PunktTransportu start, List<Przystanek> przystanki, Pane pane) {
        this.aktualnyPunkt = start;
        this.doOdwiedzenia = new ArrayList<>(przystanki);
        this.celPunkt = znajdzNajkrotszaDroge(doOdwiedzenia.get(0), new ArrayList<>());
        this.pojazd = new Circle(start.getX(), start.getY(), 8, Color.RED);
        pane.getChildren().add(pojazd);
    }

    private PunktTransportu znajdzNajkrotszaDroge(PunktTransportu cel, List<Sygnalizacja> sygnalizacje) {
        Map<PunktTransportu, Double> odleglosci = new HashMap<>();
        Map<PunktTransportu, Integer> liczbaSygnalizacji = new HashMap<>();
        Map<PunktTransportu, PunktTransportu> poprzednicy = new HashMap<>();
        PriorityQueue<PunktTransportu> kolejka = new PriorityQueue<>((a, b) -> {
            int sygnA = liczbaSygnalizacji.getOrDefault(a, Integer.MAX_VALUE);
            int sygnB = liczbaSygnalizacji.getOrDefault(b, Integer.MAX_VALUE);
            if (sygnA != sygnB) {
                return Integer.compare(sygnA, sygnB);
            }
            return Double.compare(odleglosci.getOrDefault(a, Double.MAX_VALUE), odleglosci.getOrDefault(b, Double.MAX_VALUE));
        });

        odleglosci.put(aktualnyPunkt, 0.0);
        liczbaSygnalizacji.put(aktualnyPunkt, 0);
        kolejka.add(aktualnyPunkt);

        while (!kolejka.isEmpty()) {
            PunktTransportu obecny = kolejka.poll();
            if (obecny.equals(cel)) {
                break;
            }

            for (PunktTransportu sasiad : obecny.getPolaczenia()) {
                double dystans = obliczDystans(obecny, sasiad);
                int sygnalizacjeNaTrasie = liczbaSygnalizacjiNaTrasie(obecny, sasiad, sygnalizacje);

                double nowyKosztOdleglosci = odleglosci.get(obecny) + dystans;
                int nowaLiczbaSygnalizacji = liczbaSygnalizacji.get(obecny) + sygnalizacjeNaTrasie;

                boolean lepszaDroga = nowaLiczbaSygnalizacji < liczbaSygnalizacji.getOrDefault(sasiad, Integer.MAX_VALUE) ||
                        (nowaLiczbaSygnalizacji == liczbaSygnalizacji.getOrDefault(sasiad, Integer.MAX_VALUE) &&
                                nowyKosztOdleglosci < odleglosci.getOrDefault(sasiad, Double.MAX_VALUE));

                if (lepszaDroga) {
                    odleglosci.put(sasiad, nowyKosztOdleglosci);
                    liczbaSygnalizacji.put(sasiad, nowaLiczbaSygnalizacji);
                    poprzednicy.put(sasiad, obecny);
                    kolejka.add(sasiad);
                }
            }
        }

        PunktTransportu punkt = cel;
        while (poprzednicy.containsKey(punkt) && !poprzednicy.get(punkt).equals(aktualnyPunkt)) {
            punkt = poprzednicy.get(punkt);
        }

        return punkt; // Zwraca następny punkt na najlepszej trasie
    }

    private int liczbaSygnalizacjiNaTrasie(PunktTransportu start, PunktTransportu cel, List<Sygnalizacja> sygnalizacje) {
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

    public void przemieszczaj(List<Sygnalizacja> sygnalizacje) {
        if (postojTimer > 0) {
            postojTimer--;
            return;
        }

        double dx = celPunkt.getX() - pojazd.getCenterX();
        double dy = celPunkt.getY() - pojazd.getCenterY();
        double dystans = Math.sqrt(dx * dx + dy * dy);

        // Sprawdzenie sygnalizacji świetlnej
        for (Sygnalizacja sygnalizacja : sygnalizacje) {
            if (sygnalizacja.znajdujeSieBlisko(pojazd.getCenterX(), pojazd.getCenterY()) && !sygnalizacja.jestZielone()) {
                return; // Zatrzymaj pojazd, jeśli sygnalizacja jest czerwona i pojazd jest blisko
            }
        }

        if (dystans > 1) {
            pojazd.setCenterX(pojazd.getCenterX() + dx / dystans);
            pojazd.setCenterY(pojazd.getCenterY() + dy / dystans);
        } else {
            aktualnyPunkt = celPunkt;
            if (aktualnyPunkt instanceof Przystanek) {
                postojTimer = 20; // Postój na przystanku (2 sekundy przy 100ms interwału)
                doOdwiedzenia.remove(aktualnyPunkt);
            }
            if (!doOdwiedzenia.isEmpty()) {
                celPunkt = znajdzNajkrotszaDroge(doOdwiedzenia.get(0), sygnalizacje);
            } else {
                celPunkt = znajdzNajkrotszaDroge(aktualnyPunkt.getPolaczenia().get(0), sygnalizacje);
            }
        }
    }
}


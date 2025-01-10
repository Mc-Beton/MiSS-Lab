package com.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Symulacja {
    private List<Przystanek> przystanki;
    private List<Linia> linie;
    private List<Pojazd> pojazdy;
    private List<Pasazer> pasazerowie;

    public Symulacja() {
        this.przystanki = new ArrayList<>();
        this.linie = new ArrayList<>();
        this.pojazdy = new ArrayList<>();
        this.pasazerowie = new ArrayList<>();
    }

    public void dodajPrzystanek(Przystanek przystanek) {
        this.przystanki.add(przystanek);
    }

    public void dodajLinie(Linia linia) {
        this.linie.add(linia);
    }

    public void dodajPojazd(Pojazd pojazd) {
        this.pojazdy.add(pojazd);
    }

    public void dodajPasazera(Pasazer pasazer) {
        this.pasazerowie.add(pasazer);
    }

    public void uruchom() {
        int iteracja = 0;
        while (!pasazerowie.isEmpty()) {
            iteracja++;
            System.out.println("Iteracja: " + iteracja);

            aktualizujPasazerow();
        }
        System.out.println("Symulacja zakończona po " + iteracja + " iteracjach.");
    }



    private void aktualizujPasazerow() {
        Iterator<Pasazer> iterator = pasazerowie.iterator();
        while (iterator.hasNext()) {
            Pasazer pasazer = iterator.next();
            pasazer.zwiekszCzasOczekiwania();
            System.out.println("Pasażer " + pasazer.getId() + " czeka już: " + pasazer.getCzasOczekiwania() + " jednostek czasu.");
            if (pasazer.getMiejsceStartu() == pasazer.getMiejsceDocelowe()) {
                iterator.remove();
                System.out.println("Pasażer " + pasazer.getId() + " dotarł do celu i opuszcza system.");
            }
        }
    }
}


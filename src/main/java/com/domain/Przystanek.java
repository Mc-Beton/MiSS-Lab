package com.domain;

// Klasa reprezentująca przystanek
public class Przystanek extends PunktBazowy {
    private final int kategoria;

    public Przystanek(String id, String nazwa, double x, double y, int kategoria) {
        super(id, nazwa, x, y);
        this.kategoria = kategoria;
    }

    public int getKategoria() {
        return kategoria;
    }
}
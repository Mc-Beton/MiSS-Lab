package com.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class PunktBazowy implements PunktTransportu {
    private final String id;
    private final String nazwa;
    private final List<PunktTransportu> polaczenia;
    private final double x;
    private final double y;

    public PunktBazowy(String id, String nazwa, double x, double y) {
        this.id = id;
        this.nazwa = nazwa;
        this.polaczenia = new ArrayList<>();
        this.x = x;
        this.y = y;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getNazwa() {
        return nazwa;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public List<PunktTransportu> getPolaczenia() {
        return polaczenia;
    }

    @Override
    public void dodajPolaczenie(PunktTransportu punkt) {
        this.polaczenia.add(punkt);
    }
}

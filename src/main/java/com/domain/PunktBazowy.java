package com.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PunktBazowy implements PunktTransportu {
    private final String id;
    private final String nazwa;
    private final List<PunktTransportu> polaczenia;
    private final Map<PunktTransportu, Double> obciazeniePolaczen;
    private final double x;
    private final double y;

    public PunktBazowy(String id, String nazwa, double x, double y) {
        this.id = id;
        this.nazwa = nazwa;
        this.polaczenia = new ArrayList<>();
        this.obciazeniePolaczen = new HashMap<>();
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
    public void dodajPolaczenie(PunktTransportu punkt, double obciazenie) {
        this.polaczenia.add(punkt);
        this.obciazeniePolaczen.put(punkt, obciazenie);
    }

    @Override
    public double getObciazeniePolaczenia(PunktTransportu punkt) {
        return obciazeniePolaczen.getOrDefault(punkt, 1.0); // Domyślne obciążenie 1.0
    }
}

package com.domain;

import java.util.ArrayList;
import java.util.List;

public class Linia {
    private String id;
    private List<Przystanek> przystanki;

    public Linia(String id) {
        this.id = id;
        this.przystanki = new ArrayList<>();
    }

    public void dodajPrzystanek(Przystanek przystanek) {
        this.przystanki.add(przystanek);
    }

    public List<Przystanek> getPrzystanki() {
        return przystanki;
    }

    public String getId() {
        return id;
    }
}

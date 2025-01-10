package com.domain;


public class Pasazer {
    private String id;
    private Przystanek miejsceStartu;
    private Przystanek miejsceDocelowe;
    private int czasOczekiwania;

    public Pasazer(String id, Przystanek miejsceStartu, Przystanek miejsceDocelowe) {
        this.id = id;
        this.miejsceStartu = miejsceStartu;
        this.miejsceDocelowe = miejsceDocelowe;
        this.czasOczekiwania = 0;
    }

    public void zwiekszCzasOczekiwania() {
        czasOczekiwania++;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Przystanek getMiejsceStartu() {
        return miejsceStartu;
    }

    public void setMiejsceStartu(Przystanek miejsceStartu) {
        this.miejsceStartu = miejsceStartu;
    }

    public Przystanek getMiejsceDocelowe() {
        return miejsceDocelowe;
    }

    public void setMiejsceDocelowe(Przystanek miejsceDocelowe) {
        this.miejsceDocelowe = miejsceDocelowe;
    }

    public int getCzasOczekiwania() {
        return czasOczekiwania;
    }

    public void setCzasOczekiwania(int czasOczekiwania) {
        this.czasOczekiwania = czasOczekiwania;
    }
}


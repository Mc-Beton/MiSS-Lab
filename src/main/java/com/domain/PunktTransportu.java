package com.domain;

import java.util.ArrayList;
import java.util.List;

// Interfejs dla wspólnej funkcjonalności przystanków i węzłów
public interface PunktTransportu {
    String getId();
    String getNazwa();
    double getX();
    double getY();
    List<PunktTransportu> getPolaczenia();
    void dodajPolaczenie(PunktTransportu punkt);
}



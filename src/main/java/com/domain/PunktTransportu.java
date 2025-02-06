package com.domain;

import java.util.List;

public interface PunktTransportu {
    String getId();
    String getNazwa();
    double getX();
    double getY();
    List<PunktTransportu> getPolaczenia();
    void dodajPolaczenie(PunktTransportu punkt, double obciazenie);
    double getObciazeniePolaczenia(PunktTransportu punkt);
}

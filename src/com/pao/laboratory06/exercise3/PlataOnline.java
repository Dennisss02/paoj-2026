package com.pao.laboratory06.exercise3;

interface PlataOnline {
    void autentificare(String user, String parola);
    double consultareSold();
    boolean efectuarePlata(double suma);
}

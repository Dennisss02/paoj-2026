package com.pao.laboratory05.angajati;

import java.util.Arrays;

public class AngajatService {
    private Angajat[] angajati;

    private AngajatService() {
        angajati = new Angajat[0];
    }

    private static class AngajatServiceHolder {
        private static final AngajatService Instance = new AngajatService();
    }

    public static AngajatService getInstance() {
        return AngajatServiceHolder.Instance;
    }

    void addAngajat(Angajat a) {
        int n = angajati.length;
        Angajat[] newangajati = new Angajat[n + 1];
        System.arraycopy(angajati, 0, newangajati, 0, n);
        newangajati[n] = a;
        angajati = newangajati;
    }

    void printAll() {
        for(Angajat a: angajati) {
            System.out.println(a);
        }
    }
    void listBySalary() {
        Angajat[] clone = angajati.clone();
        Arrays.sort(clone);
        for(int i = 1; i <= clone.length; i++) {
            System.out.println(i + ". " + clone[i - 1]);
        }
    }
    void findMyDepartment(String numeDept) {
        boolean found = false;
        for(Angajat a: angajati) {
            if(a.getDepartament().nume().equalsIgnoreCase(numeDept)) {
                found = true;
                System.out.println(a);
            }
        }
        if(!found) {
            System.out.println("Niciun angajat in departamentul: " + numeDept);
        }
    }
}

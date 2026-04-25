package com.pao.project.service;

import com.pao.project.exception.EntitateNegasitaException;
import com.pao.project.exception.OperatieInvalidaException;
import com.pao.project.model.Restaurant;

import java.util.*;

public class RestaurantService {
    private List<Restaurant> restaurante;
    private static int idCounter = 1;

    private RestaurantService() { restaurante = new ArrayList<>(); }
    private static class RestaurantServiceHolder {
        private static final RestaurantService Instance = new RestaurantService();
    }
    public static RestaurantService getInstance() { return RestaurantServiceHolder.Instance; }

    public int getIdCounter() { return idCounter++; }

    public void adaugaRestaurant(Restaurant r) {
        if(r == null) {
            throw new OperatieInvalidaException("Restaurantul nu poate fi nul!");
        }
        restaurante.add(r);
    }

    public List<Restaurant> getRestauranteDupaMedie() {
        return restaurante.stream()
                .sorted()
                .toList();
    }

    public Restaurant getRestaurantDupaId(int id) {
        return restaurante.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntitateNegasitaException("Restaurant invalid!"));
    }

    public void stergeRestaurant(Restaurant r) {
        restaurante.remove(r);
    }
}

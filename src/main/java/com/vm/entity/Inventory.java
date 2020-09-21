package com.vm.entity;

import java.util.*;

public class Inventory<T> {
    private Map<T, Integer> inventory = new HashMap<>();

    public void add(T item){
        int quantity = Optional.ofNullable(inventory.get(item)).orElse(0);
        inventory.put(item, quantity + 1);
    }

    public int getQuantity(T item){
        return Optional.ofNullable(inventory.get(item)).orElse(0);
    }

    public boolean hasItem(T item){
        return getQuantity(item) > 0;
    }

    public boolean hasItem(T item, int quantity){
        return getQuantity(item) >= quantity;
    }

    public void deduct(T item){
        if(hasItem(item)){
            inventory.put(item, getQuantity(item) - 1);
        }
    }
    public void deduct(T item, int quantity){
        if(hasItem(item, quantity)){
            inventory.put(item, getQuantity(item) - quantity);
        }
    }

    public void put(T item, int quantity){
        inventory.put(item, quantity);
    }
    public void clear(){
        inventory.clear();
    }

    public List<T> getAll(){
        return new ArrayList<>(inventory.keySet());
    }



}

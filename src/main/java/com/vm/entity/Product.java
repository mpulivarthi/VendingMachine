package com.vm.entity;

public enum Product {
    Coke("Coke", 25),
    Pepsi("Pepsi", 35),
    Soda("Soda", 45);

    String name;
    int price;

    Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName(){
        return name;
    }
    public long getPrice(){
        return price;
    }
}

package com.vm.utils;

public class Basket<E1, E2> {
    private final E1 item;
    private final E2 change;

    public Basket(E1 item, E2 change){
        this.item = item;
        this.change = change;
    }
    public E1 getItem(){
        return item;
    }
    public E2 getChange(){
        return change;
    }
}

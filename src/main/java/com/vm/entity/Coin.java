package com.vm.entity;

public enum Coin {
    Penny(1),
    Nickel(5),
    Dime(10),
    Quarter(25);

    int denom;

    Coin(int denom){
        this.denom = denom;
    }

    public int getDenom(){
        return denom;
    }


}

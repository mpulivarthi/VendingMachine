package com.vm.api;

import com.vm.entity.Coin;
import com.vm.entity.Product;
import com.vm.exceptions.*;
import com.vm.utils.Basket;

import java.util.List;
import java.util.Map;

public interface VendingMachine {
    void insertCoin(List<Coin> coins);
    Map<Product, Long> selectProductAndGetPrice(Product product) throws ProductNotAvailableException;
    Map<Coin, Integer> refund() throws NoChangeException;
    Basket<Product, Map<Coin, Integer>> dispenseProductAndChange() throws NoPaymentException, NoChangeException;
    void reset();

}

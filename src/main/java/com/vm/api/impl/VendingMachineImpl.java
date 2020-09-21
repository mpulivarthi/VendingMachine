package com.vm.api.impl;

import com.vm.api.VendingMachine;
import com.vm.entity.Coin;
import com.vm.entity.Inventory;
import com.vm.entity.Product;
import com.vm.exceptions.*;
import com.vm.utils.Basket;
import com.vm.config.ConfigReader;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class VendingMachineImpl implements VendingMachine {
    private final Inventory<Product> itemInventory = new Inventory<>();
    private final Inventory<Coin> cashInventory = new Inventory<>();
    private BigDecimal cBalance ;
    private Product sProduct;
    private BigDecimal totalSales;

    public VendingMachineImpl(){
        totalSales = new BigDecimal(0);
        loadInventory();
    }

    private void loadInventory(){
        ConfigReader cr = new ConfigReader();
        for(Product product: Product.values())
            itemInventory.put(product, cr.getValue("product"));
        System.out.println("Initial Inventory Information :: \t\t");
        printItemInventoryStatus();
        for(Coin coin: Coin.values())
            cashInventory.put(coin, cr.getValue("coin"));
        printCashInventoryStatus();
    }
    @Override
    public void insertCoin(List<Coin> coins) {
        cBalance = new BigDecimal(0);
        for(Coin coin: coins){
            cBalance = cBalance.add(new BigDecimal(coin.getDenom()));
            cashInventory.add(coin);
        }
        printCashInventoryStatus();
    }

    @Override
    public Map<Product, Long> selectProductAndGetPrice(Product product) throws ProductNotAvailableException {
        printItemInventoryStatus();
        if(itemInventory.hasItem(product)){
            sProduct = product;
            Map<Product, Long> map = new HashMap<>();
            map.put(sProduct, sProduct.getPrice());
            System.out.println("Selected Product : "+ sProduct.getName());
            return map;
        }
        throw new ProductNotAvailableException(product.getName() + " is not Available. Please select another product.");

    }

    @Override
    public Map<Coin, Integer> refund() throws NoChangeException {
        Map<Coin, Integer> refund = getChange(cBalance.longValue());
        updateCashInventory(refund);
        sProduct = null;
        cBalance = null;
        return refund;
    }

    @Override
    public Basket<Product, Map<Coin, Integer>> dispenseProductAndChange() throws NoPaymentException, NoChangeException {
        if(!isFullPaid()){
            long balance = sProduct.getPrice() - cBalance.longValue();
            throw new NoPaymentException("Price not paid fully, amount to pay ",balance);
        }
        Product product = dispenseProduct();
        totalSales = totalSales.add(new BigDecimal(product.getPrice()));
        Map<Coin, Integer> change = collectChange();
        System.out.println("Inventory after product dispatched: ");
        printItemInventoryStatus();
        printCashInventoryStatus();
        printSalesInfo();
        return new Basket<>(product, change);
    }

    private Product dispenseProduct() throws NoChangeException {
        if (hasEnoughChange(cBalance.longValue() - sProduct.getPrice())) {
            itemInventory.deduct(sProduct);
            return sProduct;
        }
        throw new NoChangeException("Not enough change to return, try with exact price or buy another product.");
    }

    private boolean hasEnoughChange(long amount) throws NoChangeException {
        getChange(amount);
        return true;
    }

    private Map<Coin, Integer> collectChange() throws NoChangeException {
        long changeAmount = cBalance.longValue() - sProduct.getPrice();
        System.out.println("Money deposited : "+ cBalance.longValue());
        System.out.println("Product price : "+ sProduct.getPrice());
        System.out.println("Change to be Returned : "+ changeAmount);
        Map<Coin, Integer> change = getChange(changeAmount);
        updateCashInventory(change);
        cBalance = null;
        sProduct = null;
        return change;

    }

    private void updateCashInventory(Map<Coin, Integer> change){
        for(Coin coin: change.keySet()){
            cashInventory.deduct(coin, change.get(coin));
        }

    }
    private Map<Coin, Integer> getChange(long amount) throws NoChangeException {
        Map<Coin, Integer> change = new HashMap<>();
        Inventory<Coin> changeInventory = new Inventory<>();
        if(amount > 0){
            long balance = amount;
            List<Coin> cashIn = cashInventory.getAll();
            cashIn = cashIn.stream()
                    .sorted(Comparator.comparing(Coin::getDenom))
                    .collect(Collectors.toList());
            cashIn.sort(Collections.reverseOrder());
            while(balance > 0){
                boolean isContinue = false;
                for(Coin coin: cashIn){
                    //System.out.println("coin.getDenomination(): "+ coin.getDenom());
                    //System.out.println(coin.name() + " quantity : "+ changeInventory.getQuantity(coin));
                    if(balance >= coin.getDenom() &&
                    cashInventory.hasItem(coin,
                            changeInventory.getQuantity(coin)+1)){
                        balance -= coin.getDenom();
                        //System.out.println("Balance : "+ balance);
                        changeInventory.add(coin);
                        isContinue = true;
                        break;
                    }
                }
                if(!isContinue)
                    break;
            }

            if(balance != 0){
                //System.out.println("After loop: "+balance);
                throw new NoChangeException("No change to return, try with exact price or buy another product.");
            }

        }
        for(Coin coin: changeInventory.getAll()){
            change.put(coin, changeInventory.getQuantity(coin));
        }
        return change;
    }

    private boolean isFullPaid(){
        return (cBalance.longValue() - sProduct.getPrice()) >= 0;
    }
    @Override
    public void reset() {
        System.out.println("***Reset Vending Machine***");
        cashInventory.clear();
        itemInventory.clear();
        totalSales = null;
        sProduct = null;
        cBalance = null;
        for(Product product: Product.values())
            itemInventory.put(product, 0);
        for(Coin coin: Coin.values())
            cashInventory.put(coin, 0);
        printItemInventoryStatus();
        printCashInventoryStatus();
    }
    public long getTotalSales(){
        return totalSales.longValue();
    }

    public void printItemInventoryStatus(){
        System.out.println("Product Inventory Status: ");
        itemInventory.getAll().forEach(item -> System.out.println("\t"+item.name() + " : "+ itemInventory.getQuantity(item)));
    }
    public void printCashInventoryStatus(){
        System.out.println("Cash Inventory Status: ");
        cashInventory.getAll().forEach(coin -> System.out.println("\t"+coin.name() + " : "+ cashInventory.getQuantity(coin)));
    }
    public void printSalesInfo(){
        System.out.println("Total Sales : "+ getTotalSales());
    }
}

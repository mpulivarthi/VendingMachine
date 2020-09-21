package com.vm.tests;
import com.vm.api.VendingMachine;
import com.vm.api.impl.VendingMachineImpl;
import com.vm.entity.Coin;
import com.vm.entity.Product;
import com.vm.exceptions.*;
import com.vm.utils.Basket;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;

public class VendingMachineTest {
    private static VendingMachine vm ;

    @BeforeClass
    public void setup(){
        vm = new VendingMachineImpl();
    }

    @AfterClass
    public void tearDown(){
        vm = null;

    }

    @Test(enabled = true, priority = 0, description = "User can select the product Soda")
    public void testUserCanSelectProductSoda() throws ProductNotAvailableException {
        Map<Product, Long> map = vm.selectProductAndGetPrice(Product.Soda);
        map.keySet().forEach(
            product -> {
                Assert.assertEquals(Product.Soda, product);
                Assert.assertEquals(Product.Soda.getPrice(), product.getPrice());
            });
    }

    @Test(enabled = true, priority = 1, description = "User buy the product with exact price paid")
    public void testProductWithExactPrice() throws NoChangeException, NoPaymentException, ProductNotAvailableException {
        Map<Product, Long> map = vm.selectProductAndGetPrice(Product.Pepsi);
        map.keySet().forEach(
                product -> {
                    Assert.assertEquals(product, Product.Pepsi);
                    Assert.assertEquals(product.getPrice(), Product.Pepsi.getPrice());
                });
        List<Coin> coins = new ArrayList<>();
        coins.add(Coin.Quarter);
        coins.add(Coin.Dime);
        vm.insertCoin(coins);
        Basket<Product, Map<Coin, Integer>> basket = vm.dispenseProductAndChange();
        Product product = basket.getItem();
        Assert.assertEquals(product, Product.Pepsi);
        Map<Coin, Integer> change = basket.getChange();
        Assert.assertTrue(change.isEmpty());
        printChangeReturned(change);
    }
    @Test(enabled = true, priority = 1, description = "User buy the product with excess price paid so change returned")
    public void testProductWithExcessPricePaidAndChangeReturned() throws NoChangeException, NoPaymentException, ProductNotAvailableException {
        Map<Product, Long> map = vm.selectProductAndGetPrice(Product.Coke);
        map.keySet().forEach(
                product -> {
                    Assert.assertEquals(product, Product.Coke, "Different Product returned");
                    Assert.assertEquals(product.getPrice(), Product.Coke.getPrice(), "Product price not matched");
                });
        List<Coin> coins = new ArrayList<>();
        //coins.add(Coin.Nickel);
        coins.add(Coin.Quarter);
        coins.add(Coin.Dime);
        coins.add(Coin.Penny);
        vm.insertCoin(coins);
        Basket<Product, Map<Coin, Integer>> basket = vm.dispenseProductAndChange();
        Assert.assertEquals(Product.Coke, basket.getItem());
        Map<Coin, Integer> change = basket.getChange();
        Assert.assertFalse(change.isEmpty());
        printChangeReturned(change);
    }

    @Test(enabled = true, priority = 1, description = "User cancel the request and takes refund")
    public void testUserCancelRequestAndTakeRefund() throws ProductNotAvailableException, NoChangeException {
        Map<Product, Long> map = vm.selectProductAndGetPrice(Product.Coke);
        map.keySet().forEach(
                product -> {
                    Assert.assertEquals(Product.Coke, product);
                    Assert.assertEquals(Product.Coke.getPrice(), product.getPrice());
                });
        List<Coin> coins = new ArrayList<>();
        coins.add(Coin.Quarter);
        coins.add(Coin.Dime);
        coins.add(Coin.Penny);
        coins.add(Coin.Penny);
        vm.insertCoin(coins);
        Map<Coin, Integer> change = vm.refund();
        System.out.println("Refund amount: " + getChangeTotal(change));
        Assert.assertEquals(getChangeTotal(change), 37, "Refund not Matched with Paid Amount");


    }
    @Test(enabled = true, description = "User can select the product Pepsi")
    public void testUserCanSelectProductPepsi() throws ProductNotAvailableException {
        Map<Product, Long> map = vm.selectProductAndGetPrice(Product.Pepsi);
        Assert.assertEquals(map.size(), 1);
        map.keySet().forEach(
                product -> {
                    Assert.assertEquals(Product.Pepsi, product);
                    Assert.assertEquals(Product.Pepsi.getPrice(), product.getPrice());
                });
    }

    @Test(enabled = true, description = "User can select the product Coke")
    public void testUserCanSelectProductCoke() throws ProductNotAvailableException {
        Map<Product, Long> map = vm.selectProductAndGetPrice(Product.Coke);
        map.keySet().forEach(
                product -> {
                    Assert.assertEquals(Product.Coke, product);
                    Assert.assertEquals(Product.Coke.getPrice(), product.getPrice());
                });
    }

    @Test(enabled = true,priority = 2, expectedExceptions = {ProductNotAvailableException.class})
    public void testProductNotAvailableScenario() throws ProductNotAvailableException, NoChangeException, NoPaymentException {
        for (int i = 0; i < 6; i++) {
            vm.selectProductAndGetPrice(Product.Pepsi);
            List<Coin> coins = new ArrayList<>();
            coins.add(Coin.Dime);
            coins.add(Coin.Dime);
            coins.add(Coin.Quarter);
            vm.insertCoin(coins);
            vm.dispenseProductAndChange();
        }
    }
    @Test(enabled = true, priority = 1,
            expectedExceptions = NoChangeException.class)
    public void testNoChangeExceptionScenario() throws ProductNotAvailableException, NoChangeException, NoPaymentException {
        for (int i = 0; i < 5; i++) {
            vm.selectProductAndGetPrice(Product.Soda);
            List<Coin> coins = new ArrayList<>();
            coins.add(Coin.Quarter);
            coins.add(Coin.Quarter);
            coins.add(Coin.Quarter);
            vm.insertCoin(coins);
            Basket<Product, Map<Coin, Integer>> basket = vm.dispenseProductAndChange();
            assert basket != null;
            Product product = basket.getItem();
            Assert.assertEquals(product, Product.Soda);
            Map<Coin, Integer> change = basket.getChange();
            //Assert.assertTrue(change.isEmpty());
            printChangeReturned(change);
        }

    }
    @Test(priority = 3, expectedExceptions = ProductNotAvailableException.class)
    public void testReset() throws ProductNotAvailableException {
        vm = new VendingMachineImpl();
        vm.reset();
        vm.selectProductAndGetPrice(Product.Coke);

    }
    private void printChangeReturned(Map<Coin, Integer> change){
        if(change.isEmpty()){
            System.out.println("Exact price paid! so No change to return.");
        } else{
            System.out.println("Change Returned: ");
            for(Coin coin: change.keySet())
                System.out.println("\tCoin: "+ coin.name() + "\n\tQuantity : " + change.get(coin));
        }
    }
    private int getChangeTotal(Map<Coin, Integer> change) {
        int total = 0;
        for (Coin coin : change.keySet())
            total += (coin.getDenom() * change.get(coin));
        return total;
    }
}

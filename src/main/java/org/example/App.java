package org.example;

import org.example.entities.Product;
import org.example.entities.categories.Category;
import org.example.service.Warehouse;

import java.util.Date;

public class App {
    public static void main(String[] args) {
        Warehouse testingWarehouse = new Warehouse();
        Product testProduct = new Product(testingWarehouse.totalProductsInWarehouse(), "Btove", Category.KITCHEN, (byte) 10, new Date());
        testingWarehouse.addProduct(testProduct);
        Product otherTestProduct = new Product(testingWarehouse.totalProductsInWarehouse(), "Aicrowave", Category.KITCHEN, (byte) 6, new Date());
        testingWarehouse.addProduct(otherTestProduct);

        testingWarehouse.getAllProducts().forEach(p -> System.out.println(p.getProductName()));

        System.out.println("=============================================");
        testingWarehouse.findByCategory(Category.KITCHEN).forEach(product -> System.out.println(product.getProductName()));

        System.out.println("=============================================");
        testingWarehouse.findProduct(1).changeName("New name");
        testingWarehouse.findModifiedProducts().forEach(product -> System.out.println(product.getProductName()));

        System.out.println("=============================================");
        testingWarehouse.getMapWithFirstLetterOfProductsAndTheirSum().forEach((character, integer) -> System.out.println(character + " " + integer));

        System.out.println("=============================================");
        testingWarehouse.getProductsWithMaxRatingFromThisMonthAndNewestFirst().forEach(product -> System.out.println(product.getProductName()));
    }
}

package org.example.service;

import org.example.entities.Product;
import org.example.entities.categories.Category;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Warehouse {
    private final List<Product> products;

    public Warehouse() {
        products = new ArrayList<>();
    }

    public boolean isEmpty() {
        return products.isEmpty();
    }

    public int totalProductsInWarehouse() {
        return products.size();
    }

    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(products);
    }


    public void addProduct(Product testProduct) {
        if (testProduct.getProductName().isEmpty()) {
            throw new RuntimeException("The name of the product cant be empty");
        }
        products.add(testProduct);
    }

    public Product findProduct(int id) {
        return products.stream()
                .filter(product -> product.getProductId() == id)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No product with this ID!"));
    }

    public List<Product> findByCategory(Category category) {
        return products.stream()
                .filter(product -> product.getProductCategory() == category)
                .sorted((product1, product2) -> product1.getProductName().compareToIgnoreCase(product2.getProductName()))
                .toList();
    }

    public List<Product> findModifiedProducts() {
        return products.stream()
                .filter(product -> product.getCreatedDate() != product.getModifiedDate())
                .toList();
    }

    public List<Product> findProductsFromDate(Date fromDate) {
        return products.stream()
                .filter(product -> product.getCreatedDate().after(fromDate))
                .toList();
    }

    public List<Category> getCategoriesWithAtLeastOneProduct() {
        return products.stream()
                .map(Product::getProductCategory)
                .distinct()
                .toList();
    }

    public long amountOfProductsInCategory(Category category) {
        return products.stream()
                .filter(product -> product.getProductCategory() == category)
                .count();
    }


    public Map<Character, Integer> getMapWithFirstLetterOfProductsAndTheirSum() {
        return products.stream()
                .map(product -> product.getProductName().toUpperCase().charAt(0))
                .collect(Collectors.toMap(Function.identity(),
                        character -> 1,
                        Integer::sum));
    }

    public List<Product> getProductsWithMaxRatingFromThisMonthAndNewestFirst() {
        return products.stream()
                .filter(product -> product.getProductRating() == (byte) 10)
                .filter(product -> convertDate(product.getCreatedDate()).getMonth().equals(getTodaysMonth()))
                .filter(product -> convertDate(product.getCreatedDate()).getYear() == getCurrentYear())
                .sorted((p1,p2) -> p2.getCreatedDate().compareTo(p1.getCreatedDate()))
                .toList();
    }

    private Month getTodaysMonth() {
        return convertDate(new Date()).getMonth();
    }

    private int getCurrentYear() {
        return convertDate(new Date()).getYear();
    }

    private LocalDate convertDate(Date dateToConvert) {
        return LocalDate.ofInstant(dateToConvert.toInstant(), ZoneId.systemDefault());
    }
}

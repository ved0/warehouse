package org.example.entities;

import org.example.entities.categories.Category;

import java.util.Date;

public class Product {
    private final int productId;
    private String productName;
    private Category productCategory;
    private byte productRating;
    private final Date productCreated;
    private Date productModified;

    public Product(int id, String name, Category category, byte rating, Date created) {
        this.productId = id;
        this.productName = name;
        this.productCategory = category;
        this.productRating = rating;
        this.productCreated = created;
        this.productModified = created;
    }

    public Date getCreatedDate() {
        return productCreated;
    }

    public Date getModifiedDate() {
        return productModified;
    }

    public String getProductName() {
        return productName;
    }

    public int getProductId() {
        return productId;
    }

    public void changeName(String newProductName) {
        this.productName = newProductName;
        setProductModified();
    }

    public void changeCategory(Category otherCategory) {
        this.productCategory = otherCategory;
        setProductModified();
    }

    public Category getProductCategory() {
        return productCategory;
    }

    public void changeRating(byte newRating) {
        this.productRating = newRating;
        setProductModified();
    }

    public byte getProductRating() {
        return productRating;
    }

    private void setProductModified() {
        this.productModified = new Date();
    }
}

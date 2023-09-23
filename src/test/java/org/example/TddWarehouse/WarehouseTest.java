package org.example.TddWarehouse;

import org.example.entities.Product;
import org.example.entities.categories.Category;
import org.example.service.Warehouse;
import org.junit.jupiter.api.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test of warehouse and products")
public class WarehouseTest {
    Warehouse warehouseTest;

    @Test
    @DisplayName("Create an empty warehouse object with new Warehouse()")
    void warehouseIsInstantiatedWithNew() {
        new Warehouse();
    }

    @Nested
    @DisplayName("When new")
    class whenNew {
        @BeforeEach
        void createNewWarehouse() {
            warehouseTest = new Warehouse();
        }

        @Test
        @DisplayName("No products in the warehouse")
        void warehouseIsEmpty() {
            assertTrue(warehouseTest.isEmpty());
        }

        @Nested
        @DisplayName("Creating a new product")
        class newProduct {
            int productId = 0;
            Product testProduct = new Product(productId, "Stove", Category.KITCHEN, (byte) 10, new Date());

            @Test
            @DisplayName("Try to add a product with an empty name")
            void addProductWithEmptyName() {
                assertThrows(RuntimeException.class, () -> warehouseTest.addProduct(new Product(0, "", Category.KITCHEN, (byte) 10, new Date())));
            }

            @Test
            @DisplayName("Search for a product with a non existent productID")
            void searchWithWrongProductID() {
                assertThrows(RuntimeException.class, () -> warehouseTest.findProduct(16));
            }

            @Test
            @DisplayName("Add a product to the warehouse")
            void addAProductToTheWarehouse() {
                warehouseTest.addProduct(testProduct);
                assertFalse(warehouseTest.isEmpty());
            }

            @Nested
            @DisplayName("Modifying a product in the warehouse")
            class modifyingExistingProduct {
                @BeforeEach
                void createAndAddProduct() {
                    warehouseTest.addProduct(testProduct);
                }

                @Test
                @DisplayName("Find a product using the productID")
                void findProductWithID() {
                    assertEquals(testProduct, warehouseTest.findProduct(productId));
                }

                @Test
                @DisplayName("Change name of a product")
                void changeName() {
                    String newName = "Dishwasher";
                    warehouseTest.findProduct(productId).changeName(newName);
                    assertEquals(newName, warehouseTest.findProduct(productId).getProductName());
                }

                @Test
                @DisplayName("Change category of a product")
                void changeCategory() {
                    Category newCategory = Category.BATHROOM;
                    warehouseTest.findProduct(productId).changeCategory(newCategory);
                    assertEquals(newCategory, warehouseTest.findProduct(productId).getProductCategory());
                }

                @Test
                @DisplayName("Change rating of a product")
                void changeRating() {
                    byte newRating = 5;
                    warehouseTest.findProduct(productId).changeRating(newRating);
                    assertEquals(newRating, warehouseTest.findProduct(productId).getProductRating());
                }

                @Test
                @DisplayName("Change modified date when editing a product")
                void checkModifiedDate() {
                    warehouseTest.findProduct(productId).changeRating((byte) 1);
                    assertNotSame(warehouseTest.findProduct(productId).getCreatedDate(), warehouseTest.findProduct(productId).getModifiedDate());
                }
            }

            @Nested
            @DisplayName("Creating multiple products")
            class multipleProducts {
                Product testProduct = new Product(0, "Stove", Category.KITCHEN, (byte) 10, new Date());
                Product otherTestProduct = new Product(1, "Microwave", Category.KITCHEN, (byte) 6, new Date());
                Product thirdTestProduct = new Product(2, "Lamp", Category.BEDROOM, (byte) 8, new SimpleDateFormat("dd-MM-yyyy").parse("26-09-2018"));
                Product fourthTestProduct = new Product(3, "Bed", Category.BEDROOM, (byte) 8, new SimpleDateFormat("dd-MM-yyyy").parse("26-09-2018"));

                multipleProducts() throws ParseException {
                }

                @BeforeEach
                void createAndAddMultipleProducts() {
                    warehouseTest.addProduct(testProduct);
                    warehouseTest.addProduct(otherTestProduct);
                    warehouseTest.addProduct(thirdTestProduct);
                    warehouseTest.addProduct(fourthTestProduct);
                }

                @Test
                @DisplayName("Add multiple products to the warehouse")
                void addMultipleProductsToTheWarehouse() {
                    assertEquals(List.of(testProduct, otherTestProduct, thirdTestProduct, fourthTestProduct), warehouseTest.getAllProducts());
                }

                @Test
                @DisplayName("Check all products in the warehouse")
                void checkAllProducts() {
                    assertEquals(4, warehouseTest.totalProductsInWarehouse());
                }

                @Test
                @DisplayName("Find products by category 'Kitchen' and display in alphabetical order")
                void findKitchenProducts() {
                    assertEquals(List.of(otherTestProduct, testProduct), warehouseTest.findByCategory(Category.KITCHEN));
                }

                @Test
                @DisplayName("Find modified products in the warehouse")
                void findAndDisplayModifiedProducts() {
                    String newName = "testing";
                    warehouseTest.findProduct(0).changeName(newName);
                    warehouseTest.findProduct(1).changeName(newName);
                    assertEquals(List.of(testProduct, otherTestProduct), warehouseTest.findModifiedProducts());
                }

                @Test
                @DisplayName("Find all new products from a specific date")
                void findProductsFromDate() throws ParseException {
                    Date fromDate = new SimpleDateFormat("dd-MM-yyyy").parse("26-09-2019");
                    assertEquals(List.of(testProduct, otherTestProduct), warehouseTest.findProductsFromDate(fromDate));
                }

                @Test
                @DisplayName("Find all categories that have at least ONE product inside")
                void findCategoriesWithAtLeastOneProduct() {
                    List<Category> withProducts = warehouseTest.getCategoriesWithAtLeastOneProduct();
                    assertEquals(List.of(Category.KITCHEN, Category.BEDROOM), withProducts);
                }

                @Test
                @DisplayName("How many products are in a specific category")
                void findProductsInCategory() {
                    long amountOfProducts = warehouseTest.amountOfProductsInCategory(Category.KITCHEN);
                    assertEquals(2, amountOfProducts);
                }

                @Test
                @DisplayName("Create a map with first letters as keys and the total sum of these products as values")
                void createMapContainingFirstLetterOfProductsAsKeyAndTheAmountAsValue() {
                    Product withDoubleLetterM = new Product(4, "monitor", Category.BEDROOM, (byte) 10, new Date());
                    warehouseTest.addProduct(withDoubleLetterM);
                    Map<Character, Integer> expected = Map.of('S', 1, 'M', 2, 'L', 1, 'B', 1);
                    Map<Character, Integer> test = warehouseTest.getMapWithFirstLetterOfProductsAndTheirSum();
                    assertEquals(expected, test);
                }

                @Test
                @DisplayName("Get all products created this month, with a maximum rating (10) and sorted by date (newest first) ")
                void getProductsWithMaxRatingThatAreCreatedThisMonthAndDisplayNewestFirst() throws ParseException {
                    Product shouldBeInListAndFirst = new Product(4, "Toaster", Category.KITCHEN, (byte) 10, new Date());
                    Product shouldNotBeInList = new Product(5, "Mixer", Category.KITCHEN, (byte) 10, new SimpleDateFormat("dd-MM-yyyy").parse("26-09-2020"));
                    warehouseTest.addProduct(shouldBeInListAndFirst);
                    warehouseTest.addProduct(shouldNotBeInList);
                    List<Product> productsThisMonth = warehouseTest.getProductsWithMaxRatingFromThisMonthAndNewestFirst();
                    assertEquals(List.of(shouldBeInListAndFirst, testProduct), productsThisMonth);
                }
            }
        }
    }
}

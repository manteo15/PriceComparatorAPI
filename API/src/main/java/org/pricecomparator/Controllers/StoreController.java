package org.pricecomparator.Controllers;

import org.pricecomparator.DTOs.*;
import org.pricecomparator.Models.Product;
import org.pricecomparator.Models.ProductDiscount;
import org.pricecomparator.Models.ProductPrice;
import org.pricecomparator.Interfaces.IStoreService;
import org.pricecomparator.Models.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private IStoreService storeService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Store>> getStores(){
        return new ResponseEntity<>(storeService.getAllStores(), HttpStatusCode.valueOf(200));
    }

    @PostMapping("/add")
    public ResponseEntity<Store> saveStore(@RequestBody String storeName){
        return new ResponseEntity<>(storeService.createStore(storeName), HttpStatusCode.valueOf(201));
    }

    @PostMapping("/uploadAll")
    public HttpStatusCode uploadAllPricesAndDiscounts() {
        storeService.uploadStorePrices(new StoreCSVModel("kaufland_2025-05-01.csv", "kaufland", "2025-05-01"));
        storeService.uploadStorePrices(new StoreCSVModel("kaufland_2025-05-08.csv", "kaufland", "2025-05-08"));
        storeService.uploadStorePrices(new StoreCSVModel("lidl_2025-05-01.csv", "lidl", "2025-05-01"));
        storeService.uploadStorePrices(new StoreCSVModel("lidl_2025-05-08.csv", "lidl", "2025-05-08"));
        storeService.uploadStorePrices(new StoreCSVModel("profi_2025-05-01.csv", "profi", "2025-05-01"));
        storeService.uploadStorePrices(new StoreCSVModel("profi_2025-05-08.csv", "profi", "2025-05-08"));
        storeService.uploadStoreDiscounts(new StoreCSVModel("kaufland_discounts_2025-05-01.csv", "kaufland", "2025-05-01"));
        storeService.uploadStoreDiscounts(new StoreCSVModel("kaufland_discounts_2025-05-08.csv", "kaufland", "2025-05-08"));
        storeService.uploadStoreDiscounts(new StoreCSVModel("lidl_discounts_2025-05-01.csv", "lidl", "2025-05-01"));
        storeService.uploadStoreDiscounts(new StoreCSVModel("lidl_discounts_2025-05-08.csv", "lidl", "2025-05-08"));
        storeService.uploadStoreDiscounts(new StoreCSVModel("profi_discounts_2025-05-01.csv", "profi", "2025-05-01"));
        storeService.uploadStoreDiscounts(new StoreCSVModel("profi_discounts_2025-05-08.csv", "profi", "2025-05-08"));
        return HttpStatusCode.valueOf(200);
    }

    /*
    The model contains the name of the file, the store name and the date when the prices are added
     */
    @PostMapping("/uploadPrices")
    public HttpStatusCode uploadStorePrice(@RequestBody StoreCSVModel model) {
        storeService.uploadStorePrices(model);
        return HttpStatusCode.valueOf(200);
    }

    /*
    The model contains the name of the file, the store name and the date when the prices are added
     */
    @PostMapping("/uploadDiscounts")
    public HttpStatusCode uploadStoreDiscounts(@RequestBody StoreCSVModel model) {
        storeService.uploadStoreDiscounts(model);
        return HttpStatusCode.valueOf(200);
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(){
        return new ResponseEntity<>(storeService.getAllProducts(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/productPrices/{storeId}")
    public ResponseEntity<List<ProductPrice>> getProductPrices(@PathVariable int storeId){
        return new ResponseEntity<>(storeService.getAllProductPricesFromStore(storeId), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/productPrices")
    public ResponseEntity<List<ProductPrice>> getAllProductPrices(){
        return new ResponseEntity<>(storeService.getAllProductPrices(), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/productDiscounts/{storeId}")
    public ResponseEntity<List<ProductDiscount>> getProductDiscounts(@PathVariable int storeId){
        return new ResponseEntity<>(storeService.getAllProductDiscountsFromStore(storeId), HttpStatusCode.valueOf(200));
    }

    // The model contains the current date
    @GetMapping("/bestDiscounts")
    public ResponseEntity<BestDiscountsModel> getBestDiscounts(@RequestBody CurrentDateModel model){
        return new ResponseEntity<>(storeService.getBestDiscounts(model), HttpStatusCode.valueOf(200));
    }

    // The model contains the current date
    @GetMapping("/newDiscounts")
    public ResponseEntity<List<NewlyAddedDiscountsModel>> getNewlyAddedDiscounts(@RequestBody CurrentDateModel model){
        return new ResponseEntity<>(storeService.getNewlyAddedDiscounts(model), HttpStatusCode.valueOf(200));
    }

    /*
    The model contains the current date and a list of product ids
    Returns a model containing the stores and their best prices per product
     */
    @GetMapping("/shoppingBasket")
    public ResponseEntity<ShoppingBasketMonitoringModel> getShoppingBasket(@RequestBody ShoppingBasketProductsModel model){
        return new ResponseEntity<>(storeService.getShoppingBasket(model), HttpStatusCode.valueOf(200));
    }

    /*
    The model contains current date, category, store id and brand
    The api returns price history until the current date
    If the store id is 0, the api returns data from all stores
    If the category or brand is empty, it returns data from all categories and brands
    If store id, category and brand have values, it filters based on its values
     */
    @GetMapping("/priceHistory")
    public ResponseEntity<List<DynamicPriceHistoryModel>> getPriceHistory(@RequestBody PriceHistoryFilterModel model){
        return new ResponseEntity<>(storeService.getDynamicPriceHistory(model), HttpStatusCode.valueOf(200));
    }

    /*
    The model contains current date, product id and set target price
    Return a list of periods, the price and store when the price drops below set target price
     */
    @GetMapping("/priceAlert")
    public ResponseEntity<List<PriceTrendModel>> getPriceAlert(@RequestBody PriceAlertModel model){
        return new ResponseEntity<>(storeService.getPriceAlert(model), HttpStatusCode.valueOf(200));
    }
}

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

    @PostMapping("/uploadPrices")
    public HttpStatusCode uploadStorePrice(@RequestBody StoreCSVModel model) {
        storeService.uploadStorePrices(model);
        return HttpStatusCode.valueOf(200);
    }

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

    @GetMapping("/bestDiscounts")
    public ResponseEntity<BestDiscountsModel> getBestDiscounts(@RequestBody CurrentDateModel model){
        return new ResponseEntity<>(storeService.getBestDiscounts(model), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/newDiscounts")
    public ResponseEntity<List<NewlyAddedDiscountsModel>> getNewlyAddedDiscounts(@RequestBody CurrentDateModel model){
        return new ResponseEntity<>(storeService.getNewlyAddedDiscounts(model), HttpStatusCode.valueOf(200));
    }

    @GetMapping("/shoppingBasket")
    public ResponseEntity<ShoppingBasketMonitoringModel> getShoppingBasket(@RequestBody ShoppingBasketProductsModel model){
        return new ResponseEntity<>(storeService.getShoppingBasket(model), HttpStatusCode.valueOf(200));
    }
}

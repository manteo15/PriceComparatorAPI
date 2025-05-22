package org.pricecomparator.Controllers;

import org.pricecomparator.DTOs.StoreCSVModel;
import org.pricecomparator.Models.Product;
import org.pricecomparator.Models.ProductDiscount;
import org.pricecomparator.Models.ProductPrice;
import org.pricecomparator.Interfaces.IStoreService;
import org.pricecomparator.Models.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private IStoreService storeService;

    @GetMapping("/getAll")
    public List<Store> getStores(){
        return storeService.getAllStores();
    }

    @PostMapping("/add")
    public Store saveStore(@RequestBody String storeName){
        return storeService.createStore(storeName);
    }

    @PostMapping("/uploadPrices")
    public HttpStatusCode uploadStorePrice(@RequestBody StoreCSVModel model) throws IOException {
        storeService.uploadStorePrices(model);
        return HttpStatusCode.valueOf(200);
    }

    @PostMapping("/uploadDiscounts")
    public HttpStatusCode uploadStoreDiscounts(@RequestBody StoreCSVModel model) throws IOException{
        storeService.uploadStoreDiscounts(model);
        return HttpStatusCode.valueOf(200);
    }

    @GetMapping("/products")
    public List<Product> getProducts(){
        return storeService.getAllProducts();
    }

    @GetMapping("/productPrices/{storeId}")
    public List<ProductPrice> getProductPrices(@PathVariable int storeId){
        return storeService.getAllProductPricesFromStore(storeId);
    }

    @GetMapping("/productPrices")
    public List<ProductPrice> getAllProductPrices(){
        return storeService.getAllProductPrices();
    }

    @GetMapping("/productDiscounts/{storeId}")
    public List<ProductDiscount> getProductDiscounts(@PathVariable int storeId){
        return storeService.getAllProductDiscountsFromStore(storeId);
    }
}

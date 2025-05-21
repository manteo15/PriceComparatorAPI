package org.pricecomparator.Controllers;

import org.pricecomparator.Interfaces.IStoreService;
import org.pricecomparator.Models.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class StoreController {

    @Autowired
    private IStoreService storeService;

    @GetMapping("/stores")
    public List<Store> getStores(){
        return storeService.getAllStores();
    }

    @PostMapping("/store")
    public Store saveStore(@RequestBody String storeName){
        return storeService.createStore(storeName);
    }
}

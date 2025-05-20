package org.pricecomparator.Services;

import org.pricecomparator.Interfaces.IStoreService;
import org.pricecomparator.Models.Store;
import org.pricecomparator.Repositories.IStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreService implements IStoreService {

    @Autowired
    private IStoreRepository storeRepository;

    @Override
    public Store createStore(String storeName){
        Store store = new Store(storeName);
        return storeRepository.save(store);
    }

}

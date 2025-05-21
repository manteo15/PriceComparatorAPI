package org.pricecomparator.Interfaces;

import org.pricecomparator.Models.Store;

import java.util.List;

public interface IStoreService {

    public Store createStore(String storeName);

    public List<Store> getAllStores();

}

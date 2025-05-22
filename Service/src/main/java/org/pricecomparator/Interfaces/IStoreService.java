package org.pricecomparator.Interfaces;

import org.pricecomparator.DTOs.StoreCSVModel;
import org.pricecomparator.Models.Product;
import org.pricecomparator.Models.ProductDiscount;
import org.pricecomparator.Models.ProductPrice;
import org.pricecomparator.Models.Store;

import java.io.IOException;
import java.util.List;

public interface IStoreService {

    public Store createStore(String storeName);

    public List<Store> getAllStores();

    public List<Product> getAllProducts();

    public List<ProductPrice> getAllProductPricesFromStore(int storeId);

    public List<ProductPrice> getAllProductPrices();

    public List<ProductDiscount> getAllProductDiscountsFromStore(int storeId);

    public void uploadStorePrices(StoreCSVModel model) throws IOException;

    public void uploadStoreDiscounts(StoreCSVModel model) throws IOException;

}

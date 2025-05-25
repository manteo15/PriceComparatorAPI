package org.pricecomparator.Interfaces;

import org.pricecomparator.DTOs.*;
import org.pricecomparator.Models.Product;
import org.pricecomparator.Models.ProductDiscount;
import org.pricecomparator.Models.ProductPrice;
import org.pricecomparator.Models.Store;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface IStoreService {

    public Store createStore(String storeName);

    public List<Store> getAllStores();

    public List<Product> getAllProducts();

    public List<ProductPrice> getAllProductPricesFromStore(int storeId);

    public List<ProductPrice> getAllProductPrices();

    public BestDiscountsModel getBestDiscounts(CurrentDateModel currentDateModel);

    public List<NewlyAddedDiscountsModel> getNewlyAddedDiscounts(CurrentDateModel currentDateModel);

    public List<ProductDiscount> getAllProductDiscountsFromStore(int storeId);

    public ShoppingBasketMonitoringModel getShoppingBasket(ShoppingBasketProductsModel model);

    public List<DynamicPriceHistoryModel> getDynamicPriceHistory(PriceHistoryFilterModel model);

    public void uploadStorePrices(StoreCSVModel model);

    public void uploadStoreDiscounts(StoreCSVModel model);

}

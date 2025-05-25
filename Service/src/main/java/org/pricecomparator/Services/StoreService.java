package org.pricecomparator.Services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.pricecomparator.DTOs.*;
import org.pricecomparator.Exceptions.InternalServerErrorException;
import org.pricecomparator.Exceptions.StoreAlreadyExistsException;
import org.pricecomparator.Exceptions.StoreNotFoundException;
import org.pricecomparator.Interfaces.*;
import org.pricecomparator.Models.Product;
import org.pricecomparator.Models.ProductDiscount;
import org.pricecomparator.Models.ProductPrice;
import org.pricecomparator.Models.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Service
public class StoreService implements IStoreService {

    private static final Logger log = LoggerFactory.getLogger(StoreService.class);
    @Autowired
    private IStoreRepository storeRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IProductPriceRepository productPriceRepository;
    @Autowired
    private IProductDiscountRepository productDiscountRepository;

    @Override
    public Store createStore(String storeName){
        Store store = new Store();
        store.setName(storeName);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id");
        Example<Store> example = Example.of(store, matcher);
        List<Store> stores = storeRepository.findAll(example);
        if(!stores.isEmpty()){
            throw new StoreAlreadyExistsException("Store already exists!");
        }

        return storeRepository.save(store);
    }

    @Override
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductPrice> getAllProductPricesFromStore(int storeId) {
        if(!storeRepository.existsById(storeId)){
            throw new StoreNotFoundException("Store id not found!");
        }

        ProductPrice productPrice = new ProductPrice();
        productPrice.setStoreId(storeId);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("price")
                .withIgnorePaths("id");

        Example<ProductPrice> example = Example.of(productPrice, matcher);

        return productPriceRepository.findAll(example);
    }

    @Override
    public List<ProductPrice> getAllProductPrices() {
        return productPriceRepository.findAll();
    }

    // returns best 3 discounts from every store
    @Override
    public BestDiscountsModel getBestDiscounts(CurrentDateModel currentDateModel) {
        List<Store> stores = getAllStores();
        BestDiscountsModel bestDiscountsModel = new BestDiscountsModel();
        Map<String, List<ProductDiscountModel>> bestStoreDiscounts = new HashMap<>();
        for(Store s:stores){
            List<ProductDiscount> productDiscounts = productDiscountRepository.getAllCurrentFromStoreOrderedByDiscount(s.getId(),
                    LocalDate.parse(currentDateModel.getCurrentDate()));
            List<ProductDiscountModel> productDiscountModels = new ArrayList<>();
            if(productDiscounts.size() > 3){
                productDiscountModels = getProductDiscountModels(productDiscounts.subList(0, 3), currentDateModel.getCurrentDate());
            } else{
                productDiscountModels = getProductDiscountModels(productDiscounts, currentDateModel.getCurrentDate());
            }
            bestStoreDiscounts.put(s.getName(), productDiscountModels);
        }
        bestDiscountsModel.setBestStoreDiscounts(bestStoreDiscounts);
        return  bestDiscountsModel;
    }

    @Override
    public List<NewlyAddedDiscountsModel> getNewlyAddedDiscounts(CurrentDateModel currentDateModel) {
        List<ProductDiscount> productDiscounts = productDiscountRepository.getNewlyAddedProductDiscounts(LocalDate.parse(currentDateModel.getCurrentDate()));
        List<NewlyAddedDiscountsModel> newlyAddedDiscountsModels = new ArrayList<>();
        for(ProductDiscount pd:productDiscounts){
            Optional<Product> product = productRepository.findById(pd.getProductId());
            List<ProductPrice> productPrices = productPriceRepository
                    .findAll(getProductPriceExampleByProductIdAndStoreId(pd.getProductId(), pd.getStoreId()));
            ProductPrice productPrice = getCurrentProductPrice(productPrices, LocalDate.parse(currentDateModel.getCurrentDate()));
            Optional<Store> store = storeRepository.findById(pd.getStoreId());

            String valuePerUnit = getValuePerUnit(product.get().getQuantity(), product.get().getPackageUnit(), productPrice.getPrice(), productPrice.getCurrency(),
                    pd.getPercentDiscount());

            NewlyAddedDiscountsModel newlyAddedDiscountsModel = new NewlyAddedDiscountsModel(store.get().getName(), pd.getProductId(),
                    product.get().getName(), product.get().getCategory(), product.get().getBrand(), product.get().getQuantity(),
                    product.get().getPackageUnit(), valuePerUnit, productPrice.getPrice(), productPrice.getCurrency(), pd.getPercentDiscount(), pd.getStoreId());

            newlyAddedDiscountsModels.add(newlyAddedDiscountsModel);
        }

        return newlyAddedDiscountsModels;
    }

    // returns a model containing data about the product, current price, store, discount and value per unit
    private List<ProductDiscountModel> getProductDiscountModels(List<ProductDiscount> productDiscounts, String currentDate){
        List<ProductDiscountModel> productDiscountModels = new ArrayList<>();
        for(ProductDiscount pd:productDiscounts){
            Optional<Product> product = productRepository.findById(pd.getProductId());
            List<ProductPrice> productPrices = productPriceRepository
                    .findAll(getProductPriceExampleByProductIdAndStoreId(pd.getProductId(), pd.getStoreId()));
            ProductPrice productPrice = getCurrentProductPrice(productPrices, LocalDate.parse(currentDate));

            String valuePerUnit = getValuePerUnit(product.get().getQuantity(), product.get().getPackageUnit(), productPrice.getPrice(), productPrice.getCurrency(),
                    pd.getPercentDiscount());

            ProductDiscountModel productDiscountModel = new ProductDiscountModel(pd.getProductId(), product.get().getName(),
                        product.get().getCategory(), product.get().getBrand(), product.get().getQuantity(), product.get().getPackageUnit(), valuePerUnit,
                        pd.getStoreId(), productPrice.getPrice(), productPrice.getCurrency(), pd.getPercentDiscount());

            productDiscountModels.add(productDiscountModel);
        }

        return productDiscountModels;
    }

    /*
    It receives a list of productPrices and the current date and returns the productPrice that is currently active in the store
     */
    private ProductPrice getCurrentProductPrice(List<ProductPrice> productPrices, LocalDate currentDate){
        ProductPrice currentProductPrice = null;
        LocalDate mostRecentDate = null;
        for(ProductPrice pd:productPrices){
            if(pd.getDate().isBefore(currentDate) || pd.getDate().isEqual(currentDate) ) {
                if(mostRecentDate == null){
                    mostRecentDate = pd.getDate();
                    currentProductPrice = pd;
                }else{
                    if(mostRecentDate.isBefore(pd.getDate())){
                        mostRecentDate = pd.getDate();
                        currentProductPrice = pd;
                    }
                }
            }
        }
        return currentProductPrice;
    }

    private Example<ProductPrice> getProductPriceExampleByProductIdAndStoreId(String productId, int storeId){
        ProductPrice productPrice = new ProductPrice();
        productPrice.setStoreId(storeId);
        productPrice.setProductId(productId);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("price")
                .withIgnorePaths("id");

        return Example.of(productPrice, matcher);
    }

    @Override
    public List<ProductDiscount> getAllProductDiscountsFromStore(int storeId) {
        if(!storeRepository.existsById(storeId)){
            throw new StoreNotFoundException("Store id not found!");
        }
        Example<ProductDiscount> example = getProductDiscountExampleByStoreId(storeId);
        return productDiscountRepository.findAll(example);
    }

    /*
    For every product, the method calculates which store has the best price and creates a model with data from product, price, discount and store
    It returns a map with the key being the store name and the value list of products with the best price from that store
     */
    @Override
    public ShoppingBasketMonitoringModel getShoppingBasket(ShoppingBasketProductsModel model) {
        ShoppingBasketMonitoringModel shoppingBasketMonitoringModel = new ShoppingBasketMonitoringModel();
        Map<String, List<BestProductPriceModel>> shoppingBasket = new HashMap<>();
        for(String productId: model.getProductList()){
            BestProductPriceModel bestProductPriceModel = getBestProductPriceModel(productId, model.getCurrentDate());
            Optional<Store> store = storeRepository.findById(bestProductPriceModel.getStoreId());
            String storeName = store.get().getName();
            if(!shoppingBasket.containsKey(storeName)){
                List<BestProductPriceModel> bestProductPriceModels = new ArrayList<>();
                bestProductPriceModels.add(bestProductPriceModel);
                shoppingBasket.put(storeName, bestProductPriceModels);
            }else{
                shoppingBasket.get(storeName).add(bestProductPriceModel);
            }
        }
        shoppingBasketMonitoringModel.setShoppingModel(shoppingBasket);
        return shoppingBasketMonitoringModel;
    }

    //For every product, a model containing price trend models is returned
    @Override
    public List<DynamicPriceHistoryModel> getDynamicPriceHistory(PriceHistoryFilterModel model) {
        List<Product> products = getProductsByCategoryOrBrand(model);
        List<DynamicPriceHistoryModel> dynamicPriceHistoryModels = new ArrayList<>();
        for(Product p:products){
            DynamicPriceHistoryModel dynamicPriceHistoryModel = getDynamicPriceHistoryModelOfProduct(p, model);
            dynamicPriceHistoryModels.add(dynamicPriceHistoryModel);
        }
        return dynamicPriceHistoryModels;
    }

    // A list of price trend models are created and returned
    @Override
    public List<PriceTrendModel> getPriceAlert(PriceAlertModel model) {
        List<Integer> storeIds = productPriceRepository.getStoreIdsWithProduct(model.getProductId());
        List<PriceTrendModel> priceAlerts = new ArrayList<>();

        for(Integer storeId:storeIds){
            priceAlerts.addAll(getPriceAlertOfProductByStore(model, storeId));
        }

        return priceAlerts;
    }

    private List<Product> getProductsByCategoryOrBrand(PriceHistoryFilterModel model){
        Product product = new Product();
        if(!model.getCategory().isEmpty()){
            if(!model.getBrand().isEmpty()){
                product.setCategory(model.getCategory());
                product.setBrand(model.getBrand());
            }else{
                product.setCategory(model.getCategory());
            }
        }else{
            if(!model.getBrand().isEmpty()){
                product.setBrand(model.getBrand());
            }else{
                return productRepository.findAll();
            }
        }

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withIgnorePaths("quantity");
        Example<Product> example = Example.of(product, matcher);
        return productRepository.findAll(example);
    }

    //For every store a list of price trend models is returned
    private DynamicPriceHistoryModel getDynamicPriceHistoryModelOfProduct(Product product, PriceHistoryFilterModel model){
        DynamicPriceHistoryModel dynamicPriceHistoryModel = new DynamicPriceHistoryModel(product.getId(), product.getName(), product.getCategory(), product.getBrand(),
                product.getQuantity(), product.getPackageUnit());
        Map<Integer, List<PriceTrendModel>> storePriceTrendModels = new HashMap<>();
        List<Integer> storeIds = new ArrayList<>();
        if(model.getStoreId() == 0){
            storeIds = productPriceRepository.getStoreIdsUntilDate(product.getId(),LocalDate.parse(model.getCurrentDate()));
        }else{
            storeIds.add(model.getStoreId());
        }

        for(Integer storeId:storeIds){
            List<PriceTrendModel> priceTrendModels = getDynamicPriceHistoryModelOfProductByStore(product.getId(), model, storeId);
            if(!priceTrendModels.isEmpty()){
                storePriceTrendModels.put(storeId, priceTrendModels);
            }
        }

        dynamicPriceHistoryModel.setPriceTrends(storePriceTrendModels);
        return  dynamicPriceHistoryModel;
    }

    /*
    lists of product prices and discounts are requested for product by storeId
    A list of price trend models are created and returned
    Example: P034;cafea măcinată;Davidoff;0.25;kg;cafea; store = lidl
    2025-05-01 - 2025-05-07 - 22.40 RON
    2025-05-08 - 2025-05-14 - 22.60 RON
    2025-05-04 - 2025-05-10; discount - 12
    2025-05-10 - 2025-05-16; discount - 15

    2025-05-01 - 2025-05-04 - 22.40 RON price
    2025-05-05 - 2025-05-07 - 22.40 RON with 12 discount
    2025-05-08 - 2025-05-10 - 22.60 RON with 12 discount
    2025-05-11 - 2025-05-14 - 22.60 RON with 15 discount
     */
    private List<PriceTrendModel> getDynamicPriceHistoryModelOfProductByStore(String productId, PriceHistoryFilterModel model, int storeId){
        List<ProductPrice> productPrices = productPriceRepository.getAllByStoreIdUntilDateOrderedByDate(productId,storeId, LocalDate.parse(model.getCurrentDate()));
        List<ProductDiscount> productDiscounts = productDiscountRepository.getAllByStoreIdUntilDateOrderedByDate(productId, storeId, LocalDate.parse(model.getCurrentDate()));
        ProductPrice currentProductPrice = new ProductPrice();
        Optional<Store> store = storeRepository.findById(storeId);
        LocalDate currentPriceTrendDate = productPrices.getFirst().getDate();
        LocalDate currentDate = LocalDate.parse(model.getCurrentDate());
        ProductDiscount currentProductDiscount = new ProductDiscount();
        List<PriceTrendModel> priceTrendModels = new ArrayList<>();
        while(!productPrices.isEmpty()){
            currentProductPrice = productPrices.getFirst();
            PriceTrendModel priceTrendModel = new PriceTrendModel();
            priceTrendModel.setCurrency(currentProductPrice.getCurrency());
            priceTrendModel.setFromDate(currentPriceTrendDate);
            priceTrendModel.setStoreName(store.get().getName());
            priceTrendModel.setPrice(currentProductPrice.getPrice());
            if(!productDiscounts.isEmpty()){
                currentProductDiscount = productDiscounts.getFirst();
                if(currentProductPrice.getDate().isBefore(currentProductDiscount.getFromDate())){
                    if(currentDate.isBefore(currentProductDiscount.getFromDate()) || currentDate.isEqual(currentProductDiscount.getFromDate())){
                        priceTrendModel.setToDate(currentDate);
                        priceTrendModel.setDiscountPercentage(0);
                        currentPriceTrendDate = currentDate;
                        productPrices.clear();
                    }else{
                        priceTrendModel.setToDate(currentProductDiscount.getFromDate());
                        priceTrendModel.setDiscountPercentage(0);
                        currentPriceTrendDate = currentProductDiscount.getFromDate().plusDays(1);
                    }
                }else {
                    if(currentProductPrice.getDate().plusDays(6).isBefore(currentProductDiscount.getToDate())){
                        if(currentDate.isBefore(currentProductPrice.getDate().plusDays(6)) ||
                                currentDate.isEqual(currentProductPrice.getDate().plusDays(6))){
                            priceTrendModel.setToDate(currentDate);
                            priceTrendModel.setDiscountPercentage(currentProductDiscount.getPercentDiscount());
                            currentPriceTrendDate = currentDate;
                            productPrices.clear();
                        }else{
                            priceTrendModel.setToDate(currentProductPrice.getDate().plusDays(6));
                            priceTrendModel.setDiscountPercentage(currentProductDiscount.getPercentDiscount());
                            currentPriceTrendDate = currentProductPrice.getDate().plusDays(7);
                            productPrices.removeFirst();
                        }
                    } else if (currentProductPrice.getDate().plusDays(6).isEqual(currentProductDiscount.getToDate())) {
                        if(currentDate.isBefore(currentProductPrice.getDate().plusDays(6)) ||
                                currentDate.isEqual(currentProductPrice.getDate().plusDays(6))){
                            priceTrendModel.setToDate(currentDate);
                            priceTrendModel.setDiscountPercentage(currentProductDiscount.getPercentDiscount());
                            currentPriceTrendDate = currentDate;
                            productPrices.clear();
                        } else{
                            priceTrendModel.setToDate(currentProductPrice.getDate().plusDays(6));
                            priceTrendModel.setDiscountPercentage(currentProductDiscount.getPercentDiscount());
                            currentPriceTrendDate = currentProductPrice.getDate().plusDays(7);
                            productPrices.removeFirst();
                            productDiscounts.removeFirst();
                        }
                    } else{
                        if(currentDate.isBefore(currentProductDiscount.getToDate()) ||
                                currentDate.isEqual(currentProductDiscount.getToDate())){
                            priceTrendModel.setToDate(currentDate);
                            priceTrendModel.setDiscountPercentage(currentProductDiscount.getPercentDiscount());
                            currentPriceTrendDate = currentDate;
                            productPrices.clear();
                        } else{
                            priceTrendModel.setToDate(currentProductDiscount.getToDate());
                            priceTrendModel.setDiscountPercentage(currentProductDiscount.getPercentDiscount());
                            currentPriceTrendDate = currentProductDiscount.getToDate().plusDays(1);
                            productDiscounts.removeFirst();
                        }
                    }
                }
            } else{
                if(currentDate.isBefore(currentProductPrice.getDate().plusDays(6)) ||
                        currentDate.isEqual(currentProductPrice.getDate().plusDays(6))){
                    priceTrendModel.setToDate(currentDate);
                    priceTrendModel.setDiscountPercentage(0);
                    currentPriceTrendDate = currentDate;
                    productPrices.clear();
                } else{
                    priceTrendModel.setDiscountPercentage(0);
                    priceTrendModel.setToDate(currentProductPrice.getDate().plusDays(6));
                    currentPriceTrendDate = currentProductPrice.getDate().plusDays(7);
                    productPrices.removeFirst();
                }
            }
            priceTrendModels.add(priceTrendModel);
        }
        return priceTrendModels;
    }

    /*
    It receives a list of current and future product prices and discounts by store
    It is almost the same as in the getDynamicPriceHistoryModelOfProductByStore method, the only difference being that the price trend models are added
    to the list only if the price calculated with discount is smaller than target price set by the user
     */
    private List<PriceTrendModel> getPriceAlertOfProductByStore(PriceAlertModel model, int storeId){
        List<ProductPrice> productPrices = productPriceRepository.getCurrentAndFutureOfProductFromStore(model.getProductId(), storeId, LocalDate.parse(model.getCurrentDate()).minusDays(6));
        List<ProductDiscount> productDiscounts = productDiscountRepository.getAllCurrentOfProductFromStore(model.getProductId(), storeId, LocalDate.parse(model.getCurrentDate()));
        productDiscounts.addAll(productDiscountRepository.getAllFutureOfProductFromStore(model.getProductId(), storeId, LocalDate.parse(model.getCurrentDate())));
        ProductPrice currentProductPrice = new ProductPrice();
        Optional<Store> store = storeRepository.findById(storeId);
        LocalDate currentPriceTrendDate = LocalDate.parse(model.getCurrentDate());
        LocalDate currentDate = LocalDate.parse(model.getCurrentDate());
        ProductDiscount currentProductDiscount = new ProductDiscount();
        List<PriceTrendModel> priceTrendModels = new ArrayList<>();
        while(!productPrices.isEmpty()){
            currentProductPrice = productPrices.getFirst();
            PriceTrendModel priceTrendModel = new PriceTrendModel();
            priceTrendModel.setCurrency(currentProductPrice.getCurrency());
            priceTrendModel.setFromDate(currentPriceTrendDate);
            priceTrendModel.setStoreName(store.get().getName());
            priceTrendModel.setPrice(currentProductPrice.getPrice());
            if(!productDiscounts.isEmpty()){
                currentProductDiscount = productDiscounts.getFirst();
                if(currentProductPrice.getDate().isBefore(currentProductDiscount.getFromDate())){
                    priceTrendModel.setToDate(currentProductDiscount.getFromDate());
                    priceTrendModel.setDiscountPercentage(0);
                    currentPriceTrendDate = currentProductDiscount.getFromDate().plusDays(1);
                }else {
                    if(currentProductPrice.getDate().plusDays(6).isBefore(currentProductDiscount.getToDate())){
                        priceTrendModel.setToDate(currentProductPrice.getDate().plusDays(6));
                        priceTrendModel.setDiscountPercentage(currentProductDiscount.getPercentDiscount());
                        currentPriceTrendDate = currentProductPrice.getDate().plusDays(7);
                        productPrices.removeFirst();
                    } else if (currentProductPrice.getDate().plusDays(6).isEqual(currentProductDiscount.getToDate())) {
                        priceTrendModel.setToDate(currentProductPrice.getDate().plusDays(6));
                        priceTrendModel.setDiscountPercentage(currentProductDiscount.getPercentDiscount());
                        currentPriceTrendDate = currentProductPrice.getDate().plusDays(7);
                        productPrices.removeFirst();
                        productDiscounts.removeFirst();
                    } else{
                        priceTrendModel.setToDate(currentProductDiscount.getToDate());
                        priceTrendModel.setDiscountPercentage(currentProductDiscount.getPercentDiscount());
                        currentPriceTrendDate = currentProductDiscount.getToDate().plusDays(1);
                        productDiscounts.removeFirst();
                    }
                }
            } else{
                priceTrendModel.setDiscountPercentage(0);
                priceTrendModel.setToDate(currentProductPrice.getDate().plusDays(6));
                currentPriceTrendDate = currentProductPrice.getDate().plusDays(7);
                productPrices.removeFirst();
            }
            float priceWithDiscount = priceTrendModel.getPrice() * (100-priceTrendModel.getDiscountPercentage()) / 100;
            if(priceWithDiscount <= model.getTargetPrice()){
                priceTrendModels.add(priceTrendModel);
            }
        }
        return priceTrendModels;
    }


    /*
    The product, its prices and discounts from every store is requested from db
    It is calculated which store has the best price calculated with the discount
    The model with data relating to the product, price, store, discount and valuePerUnit is created and returned
     */
    private BestProductPriceModel getBestProductPriceModel(String productId, String currentDate){
        float bestPrice = 0;
        int storeWithBestPrice = 0;
        Optional<Product> product = productRepository.findById(productId);
        List<ProductPrice> productPrices = productPriceRepository.findAll(getProductPriceExampleByProductId(productId));
        Map<Integer, ProductPrice> currentProductPricesByStore = getCurrentProductPricesByStore(productPrices, LocalDate.parse(currentDate));
        List<ProductDiscount> productDiscounts = productDiscountRepository.getCurrentProductDiscounts(productId, LocalDate.parse(currentDate));
        for(Map.Entry<Integer, ProductPrice> entry:currentProductPricesByStore.entrySet()){
            float price = entry.getValue().getPrice();
            int discount = 0;
            for(ProductDiscount pd: productDiscounts){
                if(pd.getStoreId() == entry.getKey()){
                    discount = pd.getPercentDiscount();
                }
            }
            if(discount != 0){
                price = (price * (100-discount)) / 100;
            }
            if(bestPrice == 0){
                bestPrice = price;
                storeWithBestPrice = entry.getKey();
            }else{
                if(bestPrice > price){
                    bestPrice = price;
                    storeWithBestPrice = entry.getKey();
                }
            }
        }
        int discountPercentage = 0;
        for(ProductDiscount pd: productDiscounts){
            if(pd.getStoreId() == storeWithBestPrice){
                discountPercentage = pd.getPercentDiscount();
            }
        }

        String valuePerUnit = getValuePerUnit(product.get().getQuantity(), product.get().getPackageUnit(), currentProductPricesByStore.get(storeWithBestPrice).getPrice(),
                currentProductPricesByStore.get(storeWithBestPrice).getCurrency(), discountPercentage);

        return new BestProductPriceModel(productId, product.get().getName(), product.get().getCategory(), product.get().getBrand(), product.get().getQuantity(),
                product.get().getPackageUnit(), valuePerUnit, storeWithBestPrice, currentProductPricesByStore.get(storeWithBestPrice).getPrice(),
                currentProductPricesByStore.get(storeWithBestPrice).getCurrency(), discountPercentage);
    }

    private String getValuePerUnit(float packageQuantity, String packageUnit, float price, String currency, int discountPercentage){
        String valuePerUnit = null;
        float priceWithDiscount = (price * (100-discountPercentage))/100;
        valuePerUnit = switch (packageUnit) {
            case "buc" -> priceWithDiscount / packageQuantity + " " + currency + "  per buc";
            case "role" -> priceWithDiscount / packageQuantity + " " + currency + "  per rola";
            case "kg" -> priceWithDiscount / packageQuantity + " " + currency + " per kg";
            case "g" -> (priceWithDiscount / packageQuantity) / 1000 + " " + currency + " per kg";
            case "l" -> priceWithDiscount / packageQuantity + " " + currency + " per l";
            case "ml" -> (priceWithDiscount / packageQuantity) / 1000 + " " + currency + " per l";
            default -> valuePerUnit;
        };
        return valuePerUnit;
    }


    /*
    It receives a list of ProductPrices from different stores and a current date
    The list is separated in lists with product prices from every store
    Then a map is created and returned with the key being the store id and the value being the product price from every store that is active
     */
    private Map<Integer, ProductPrice> getCurrentProductPricesByStore(List<ProductPrice> productPrices, LocalDate currentDate){
        Map<Integer, ProductPrice> currentProductPricesByStore = new HashMap<>();
        Map<Integer,List<ProductPrice>> productPricesByStore = new HashMap<>();
        Set<Integer> storeIds = new TreeSet<>();
        for(ProductPrice productPrice:productPrices){
            int storeId = productPrice.getStoreId();
            storeIds.add(storeId);
            if(!productPricesByStore.containsKey(storeId)){
                List<ProductPrice> storeProductPrices = new ArrayList<>();
                storeProductPrices.add(productPrice);
                productPricesByStore.put(storeId,storeProductPrices);
            }else {
                productPricesByStore.get(storeId).add(productPrice);
            }
        }
        for(int id:storeIds){
            ProductPrice productPrice = getCurrentProductPrice(productPricesByStore.get(id), currentDate);
            currentProductPricesByStore.put(id, productPrice);
        }
        return currentProductPricesByStore;
    }

    private Example<ProductPrice> getProductPriceExampleByProductId(String productId){
        ProductPrice productPrice = new ProductPrice();
        productPrice.setProductId(productId);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("price")
                .withIgnorePaths("id")
                .withIgnorePaths("storeId");

        return Example.of(productPrice, matcher);
    }

    private Example<ProductDiscount> getProductDiscountExampleByStoreId(int storeId){
        ProductDiscount productDiscount = new ProductDiscount();
        productDiscount.setStoreId(storeId);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("percentDiscount")
                .withIgnorePaths("id");

        return Example.of(productDiscount, matcher);
    }


    /*
        Creates the path of the file by combining the absolute path of the folder where the CSVs are stored + the filename
        It creates the store if it does not exist or gets the store from db
        It parses the CSV and puts the data in a list of model objects that matches the CSV
        From the list of model objects, it maps to a list of products and productPrices and saves them to db
     */
    @Override
    public void uploadStorePrices(StoreCSVModel model)  {
        Path pathToFile = Paths.get("C:\\Users\\allee\\IdeaProjects\\PriceComparatorAPI\\Utils\\src\\main\\java\\org\\pricecomparator\\CSVs\\" +
                model.getFileName());
        Store store = createOrGetExistingStore(model.getStoreName());
        List<StorePricesCSVModel> storeProductsFromCSV = parseStorePricesCSV(pathToFile);
        List<Product> products = mapProductsFromStorePricesCSVModel(storeProductsFromCSV);
        productRepository.saveAll(products);
        LocalDate date = LocalDate.parse(model.getDate());
        List<ProductPrice> productPrices = mapProductPricesFromStorePricesCSVModel(storeProductsFromCSV, store.getId(), date);
        productPriceRepository.saveAll(productPrices);
    }

    /*
        Creates the path of the file by combining the absolute path of the folder where the CSVs are stored + the filename
        It creates the store if it does not exist or gets the store from db
        It parses the CSV and puts the data in a list of model objects that matches the CSV
        From the list of model objects, it maps to a list of productDiscounts and saves them to db
     */
    @Override
    public void uploadStoreDiscounts(StoreCSVModel model)  {
        Path pathToFile = Paths.get("C:\\Users\\allee\\IdeaProjects\\PriceComparatorAPI\\Utils\\src\\main\\java\\org\\pricecomparator\\CSVs\\" +
                model.getFileName());
        Store store = createOrGetExistingStore(model.getStoreName());
        List<StoreDiscountsCSVModel> storeDiscountsFromCSV = parseStoreDiscountsCSV(pathToFile);
        List<ProductDiscount> productDiscounts = mapProductDiscountsFromStoreDiscountCSVModel(storeDiscountsFromCSV, store.getId());
        productDiscountRepository.saveAll(productDiscounts);
    }


    // Parses the CSV and returns a list of model objects matching the CSV
    private List<StorePricesCSVModel> parseStorePricesCSV(Path pathToFile) {
        try(Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(pathToFile)))) {
            HeaderColumnNameMappingStrategy<StorePricesCSVModel> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(StorePricesCSVModel.class);
            CsvToBean<StorePricesCSVModel> csvToBean = new CsvToBeanBuilder<StorePricesCSVModel>(reader)
                    .withMappingStrategy(strategy)
                    .withSeparator(';')
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return  csvToBean.parse();
        } catch (IOException e) {
            throw new InternalServerErrorException("An internal server error occurred!");
        }
    }

    // Parses the CSV and returns a list of model objects matching the CSV
    private List<StoreDiscountsCSVModel> parseStoreDiscountsCSV(Path pathToFile){
        try(Reader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(pathToFile)))) {
            HeaderColumnNameMappingStrategy<StoreDiscountsCSVModel> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(StoreDiscountsCSVModel.class);
            CsvToBean<StoreDiscountsCSVModel> csvToBean = new CsvToBeanBuilder<StoreDiscountsCSVModel>(reader)
                    .withMappingStrategy(strategy)
                    .withSeparator(';')
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return  csvToBean.parse();
        } catch (IOException e) {
            throw new InternalServerErrorException("An internal server error occurred!");
        }
    }

    private List<Product> mapProductsFromStorePricesCSVModel(List<StorePricesCSVModel> storePrices){
        List<Product> products = new ArrayList<>();
        for(StorePricesCSVModel storePrice:storePrices){
            products.add(new Product(storePrice.getProduct_id(), storePrice.getProduct_name(), storePrice.getProduct_category(),
                    storePrice.getBrand(), storePrice.getPackage_quantity(), storePrice.getPackage_unit()));
        }
        return products;
    }

    private List<ProductPrice> mapProductPricesFromStorePricesCSVModel(List<StorePricesCSVModel> storePrices, int storeId, LocalDate date){
        List<ProductPrice> productPrices = new ArrayList<>();
        for(StorePricesCSVModel storePrice: storePrices){
            productPrices.add(new ProductPrice(storePrice.getProduct_id(), storeId, storePrice.getPrice(), storePrice.getCurrency(),
                    date));
        }
        return productPrices;
    }

    private List<ProductDiscount> mapProductDiscountsFromStoreDiscountCSVModel(List<StoreDiscountsCSVModel> storeDiscounts, int storeId){
        List<ProductDiscount> productDiscounts = new ArrayList<>();
        for(StoreDiscountsCSVModel storeDiscount: storeDiscounts){
            LocalDate fromDate = LocalDate.parse(storeDiscount.getFrom_date());
            LocalDate toDate = LocalDate.parse(storeDiscount.getTo_date());
            productDiscounts.add(new ProductDiscount(storeDiscount.getProduct_id(), fromDate, toDate, storeDiscount.getPercentage_of_discount(),
                    storeId));
        }
        return productDiscounts;
    }

    private Store createOrGetExistingStore(String storeName){
        Store store = new Store();
        store.setName(storeName);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id");

        Example<Store> example = Example.of(store, matcher);
        List<Store> stores = storeRepository.findAll(example);
        if(stores.isEmpty()){
            store = createStore(storeName);
        } else{
            store = stores.getFirst();
        }
        return store;
    }

}

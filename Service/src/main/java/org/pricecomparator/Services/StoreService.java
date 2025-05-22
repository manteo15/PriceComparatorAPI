package org.pricecomparator.Services;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.pricecomparator.DTOs.StoreDiscountsCSVModel;
import org.pricecomparator.DTOs.StorePricesCSVModel;
import org.pricecomparator.DTOs.StoreCSVModel;
import org.pricecomparator.Interfaces.*;
import org.pricecomparator.Models.Product;
import org.pricecomparator.Models.ProductDiscount;
import org.pricecomparator.Models.ProductPrice;
import org.pricecomparator.Models.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        Store store = new Store(storeName);
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

    @Override
    public List<ProductDiscount> getAllProductDiscountsFromStore(int storeId) {
        ProductDiscount productDiscount = new ProductDiscount();
        productDiscount.setStoreId(storeId);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("percentDiscount")
                .withIgnorePaths("id");

        Example<ProductDiscount> example = Example.of(productDiscount, matcher);

        return productDiscountRepository.findAll(example);
    }

    @Override
    public void uploadStorePrices(StoreCSVModel model) throws IOException {
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

    @Override
    public void uploadStoreDiscounts(StoreCSVModel model) throws IOException {
        Path pathToFile = Paths.get("C:\\Users\\allee\\IdeaProjects\\PriceComparatorAPI\\Utils\\src\\main\\java\\org\\pricecomparator\\CSVs\\" +
                model.getFileName());
        Store store = createOrGetExistingStore(model.getStoreName());
        List<StoreDiscountsCSVModel> storeDiscountsFromCSV = parseStoreDiscountsCSV(pathToFile);
        List<ProductDiscount> productDiscounts = mapProductDiscountsFromStoreDiscountCSVModel(storeDiscountsFromCSV, store.getId());
        productDiscountRepository.saveAll(productDiscounts);
    }

    private List<StorePricesCSVModel> parseStorePricesCSV(Path pathToFile) throws IOException{
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
        }
    }

    private List<StoreDiscountsCSVModel> parseStoreDiscountsCSV(Path pathToFile) throws IOException{
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

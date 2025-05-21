package org.pricecomparator.Database;

import org.pricecomparator.Models.Store;
import org.pricecomparator.Repositories.IProductDiscountRepository;
import org.pricecomparator.Repositories.IProductPriceRepository;
import org.pricecomparator.Repositories.IProductRepository;
import org.pricecomparator.Repositories.IStoreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(IProductRepository productRepository, IProductDiscountRepository productDiscountRepository,
                                   IProductPriceRepository productPriceRepository, IStoreRepository storeRepository){
        return args -> {
            storeRepository.save(new Store("Profi"));
        };
    };

}

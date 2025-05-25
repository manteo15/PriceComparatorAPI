package org.pricecomparator.Interfaces;

import org.pricecomparator.Models.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IProductPriceRepository extends JpaRepository<ProductPrice, Integer> {

    @Query("Select pp from ProductPrice pp where pp.storeId = :storeId and pp.productId = :productId and pp.date <= :date order by pp.date")
    public List<ProductPrice> getAllByStoreIdUntilDateOrderedByDate(String productId, int storeId, LocalDate date);

    @Query("Select pp from ProductPrice pp where pp.storeId = :storeId and pp.productId = :productId and pp.date >= :date order by pp.date")
    public List<ProductPrice> getCurrentAndFutureOfProductFromStore(String productId, int storeId, LocalDate date);

    @Query("Select Distinct storeId from ProductPrice pp where pp.productId = :productId and pp.date <= :date")
    public List<Integer> getStoreIdsUntilDate(String productId, LocalDate date);

    @Query("Select Distinct storeId from ProductPrice pp where pp.productId = :productId")
    public List<Integer> getStoreIdsWithProduct(String productId);
}

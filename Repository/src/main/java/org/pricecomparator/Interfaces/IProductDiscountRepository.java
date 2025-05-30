package org.pricecomparator.Interfaces;

import org.pricecomparator.Models.ProductDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IProductDiscountRepository extends JpaRepository<ProductDiscount, Integer> {

    @Query("Select pd from ProductDiscount pd where pd.storeId = :storeId and pd.fromDate <= :currentDate and pd.toDate > :currentDate order by percentDiscount desc")
    public List<ProductDiscount> getAllCurrentFromStoreOrderedByDiscount(int storeId, LocalDate currentDate);

    @Query("Select pd from ProductDiscount pd where pd.storeId = :storeId and pd.productId = :productId and pd.fromDate <= :date and pd.toDate > :date order by pd.fromDate")
    public List<ProductDiscount> getAllCurrentOfProductFromStore(String productId, int storeId, LocalDate date);

    @Query("Select pd from ProductDiscount pd where pd.storeId = :storeId and pd.productId = :productId and pd.toDate < :date order by pd.fromDate")
    public List<ProductDiscount> getAllFutureOfProductFromStore(String productId, int storeId, LocalDate date);

    @Query("Select pd from ProductDiscount pd where pd.fromDate = :currentDate")
    public List<ProductDiscount> getNewlyAddedProductDiscounts(LocalDate currentDate);

    @Query("Select pd from ProductDiscount pd where pd.productId = :productId and pd.fromDate <= :currentDate and pd.toDate > :currentDate")
    public List<ProductDiscount> getCurrentProductDiscounts(String productId, LocalDate currentDate);

    @Query("Select pd from ProductDiscount pd where pd.storeId = :storeId and pd.productId = :productId and pd.fromDate <= :date order by pd.fromDate")
    public List<ProductDiscount> getAllByStoreIdUntilDateOrderedByDate(String productId, int storeId, LocalDate date);
}

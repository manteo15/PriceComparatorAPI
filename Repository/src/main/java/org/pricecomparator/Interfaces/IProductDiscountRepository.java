package org.pricecomparator.Interfaces;

import org.pricecomparator.Models.ProductDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductDiscountRepository extends JpaRepository<ProductDiscount, Integer> {
}

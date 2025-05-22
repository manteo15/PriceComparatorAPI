package org.pricecomparator.Interfaces;

import org.pricecomparator.Models.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductPriceRepository extends JpaRepository<ProductPrice, Integer> {
}

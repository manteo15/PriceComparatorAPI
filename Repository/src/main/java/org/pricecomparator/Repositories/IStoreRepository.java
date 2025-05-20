package org.pricecomparator.Repositories;

import org.pricecomparator.Models.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStoreRepository extends JpaRepository<Store, Integer> {
}

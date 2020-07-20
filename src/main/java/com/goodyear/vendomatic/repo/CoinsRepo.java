package com.goodyear.vendomatic.repo;

import java.util.Optional;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.goodyear.vendomatic.model.Coins;

@Transactional
public interface CoinsRepo extends CrudRepository<Coins,Integer>{
	
	@Query("select c from Coins c")
	@ReadOnlyProperty
	public Optional<Coins> findCoins();
	
	@Query("update Coins c set c.count = c.count - :coinsForBeverage")
	@Modifying(clearAutomatically = true)
	public void updateCoins(@Param("coinsForBeverage")Integer coinsForBeverage);
	
}

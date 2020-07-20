package com.goodyear.vendomatic.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.goodyear.vendomatic.model.Inventory;

@Transactional
public interface InventoryRepo extends CrudRepository<Inventory,Integer>{
	
	@Query("select i.count from Inventory i")
	@ReadOnlyProperty
	public Optional<List<Integer>> getInventory();
		
	@Query("update Inventory i set i.count = i.count - 1 where i.id = :beverageId")
	@Modifying(clearAutomatically = true)
	public void updateInventory(@Param("beverageId")Integer beverageId);
	
	
}

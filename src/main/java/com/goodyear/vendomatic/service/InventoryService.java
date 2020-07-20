package com.goodyear.vendomatic.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodyear.vendomatic.model.Inventory;
import com.goodyear.vendomatic.properties.VendOMaticProperties;
import com.goodyear.vendomatic.repo.InventoryRepo;

@Service
public class InventoryService {
	
	@Autowired
	private InventoryRepo repo;
	
	@Autowired
	private VendOMaticProperties properties;
	
	public void refill() {
		final List<Inventory> inventoryList = new LinkedList<>();
		for(int i=0;i<properties.getBeverageBrands();i++) {
			inventoryList.add(initBeverage());
		}
		repo.saveAll(inventoryList);
	}
	
	public Inventory initBeverage() {
		final Inventory  beverage = new Inventory();
		beverage.setCount(properties.getBeverageBrandSize());
		return beverage;
	}
	
	public Integer[] getInventory() {
		final List<Integer> items = repo.getInventory().map(x->x).orElse(null);
		final Integer inventory[] = items.toArray(new Integer[0]);
		return inventory;
	}
	
	public Inventory getInventoryById(Integer beverageId) {
		return repo.findById(beverageId).map(x->x).orElse(null);
	}
	
	public void updateInventory(int beverageId) {
		repo.updateInventory(beverageId);
	}
	
}

package com.goodyear.vendomatic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodyear.vendomatic.model.Coins;
import com.goodyear.vendomatic.model.CoinsJson;
import com.goodyear.vendomatic.properties.VendOMaticProperties;
import com.goodyear.vendomatic.repo.CoinsRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CoinsService {

	@Autowired
	private CoinsRepo coinsRepo;
	
	@Autowired
	private VendOMaticProperties properties;
	
	public Coins putCoins(CoinsJson coinsJson) {
		LOG.debug("Putting coins into vend-o-matic");
		final long coinsCount = coinsRepo.count();
		if(coinsCount>0) {
			final Coins c = getCoins();
			c.setCount(c.getCount()+coinsJson.getCoin());
			return coinsRepo.save(c);
		}else {
			final Coins coins = new Coins();
			coins.setCount(coinsJson.getCoin());
			return coinsRepo.save(coins);
		}
	}
	
	public void deleteCoins() {
		LOG.debug("Deleting coins from vend-o-matic");
		coinsRepo.deleteAll();
	}
	
	
	public Coins getCoins() {
		LOG.debug("Get all inserted coins");
		final Coins def = new Coins();
		def.setCount(0);
		return coinsRepo.findCoins().map(c1->c1).orElse(def);
	}
	
	public Integer getCoinsValue() {
		return getCoins().getCount();
	}
	
	public void updateCoins() {
		LOG.debug("Update coins count");
		coinsRepo.updateCoins(properties.getBeverageCoins());
	}
	
}

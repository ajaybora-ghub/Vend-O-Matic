package com.goodyear.vendomatic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.goodyear.vendomatic.model.Coins;
import com.goodyear.vendomatic.model.CoinsJson;
import com.goodyear.vendomatic.repo.CoinsRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CoinsService {

	@Autowired
	private CoinsRepo coinsRepo;
	
	public Coins putCoins(CoinsJson coinsJson) {
		LOG.info("Putting coins into vend-o-matic");
		final long coinsCount = coinsRepo.count();
		LOG.info("coinsCount vend-o-matic {}",coinsCount);
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
		coinsRepo.deleteAll();
	}
	
	
	public Coins getCoins() {
		final Coins def = new Coins();
		def.setCount(0);
		return coinsRepo.findCoins().map(c1->c1).orElse(def);
	}
	
	public Integer getCoinsValue() {
		return getCoins().getCount();
	}
	
	public void updateCoins() {
		coinsRepo.updateCoins();
	}
	
}

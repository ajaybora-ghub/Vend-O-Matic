package com.goodyear.vendomatic.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.goodyear.vendomatic.model.Coins;
import com.goodyear.vendomatic.model.CoinsJson;
import com.goodyear.vendomatic.model.Inventory;
import com.goodyear.vendomatic.model.InventoryJson;
import com.goodyear.vendomatic.properties.VendOMaticProperties;
import com.goodyear.vendomatic.service.CoinsService;
import com.goodyear.vendomatic.service.InventoryService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class VendOMaticController {
	
	@Autowired
	private CoinsService coinsService;
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private VendOMaticProperties properties;
		
	@GetMapping(value = "/inventory", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Integer[]> getInventory() {
		LOG.debug("In getInventory()");
		try {
			final Integer[] inventory = inventoryService.getInventory();
			return new ResponseEntity<Integer[]>(inventory, OK);
		}catch(Exception e) {
			LOG.error("Exception in getInventory {}",e);
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/inventory/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Integer> getInventoryByBeverage(@PathVariable Integer id) {
		LOG.debug("In getInventoryByBeverage() with beverageid {}",id);
		try {
			final Inventory inventory = inventoryService.getInventoryById(id);
			if(inventory==null) {
				return new ResponseEntity<>(BAD_REQUEST);
			}else {
				return new ResponseEntity<Integer>(inventory.getCount(), OK);
			}
		}catch(Exception e) {
			LOG.error("Exception in getInventoryByBeverage {} beverage {}",e,id);
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(value = "/")
	public ResponseEntity<String> refundCoins(HttpServletResponse response) { 
		try {
			if(coinsService.getCoinsValue()==0) {
				return new ResponseEntity<>(BAD_REQUEST);
			}
			response.addHeader(properties.getCoinLabel(), String.valueOf(coinsService.getCoinsValue()));
			coinsService.deleteCoins();
			return new ResponseEntity<>(NO_CONTENT);
		}catch(Exception e) {
			LOG.error("Exception in refund coins {}",e);
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> putCoins(@RequestBody CoinsJson coinsJson,HttpServletResponse response) {
		try {
			if(coinsJson.getCoin()>properties.getVendingCoinLimit()||coinsJson.getCoin()==0) {
				return new ResponseEntity<>(BAD_REQUEST);
			}
			coinsService.putCoins(coinsJson);
			response.addHeader(properties.getCoinLabel(), String.valueOf(coinsService.getCoinsValue()));
			return new ResponseEntity<>(NO_CONTENT);
		}catch(Exception e) {
			LOG.error("Exception in put coins {}",e);
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(value = "/inventory/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<InventoryJson> getBeverage(@PathVariable Integer id,HttpServletResponse response) {
		try {
			Coins coins = coinsService.getCoins();
			if(coins.getCount()<2) {
				LOG.warn("Insufficient coins {} to vend beverage {}",coins.getCount(),id);
				response.addHeader(properties.getCoinLabel(), String.valueOf(coins.getCount()));
				return new ResponseEntity<>(FORBIDDEN);
			}
			Inventory inventory = inventoryService.getInventoryById(id);
			if(inventory.getCount()==0) {
				LOG.warn("Beverage {} is out of stock",coins.getCount(),id);
				response.addHeader(properties.getCoinLabel(), String.valueOf(coins.getCount()));
				return new ResponseEntity<>(NOT_FOUND);
			}
			inventoryService.updateInventory(id);
			inventory = inventoryService.getInventoryById(id);
			InventoryJson inventoryJson = new InventoryJson();
			inventoryJson.setQuantity(properties.getVendingBeverageLimit());
			response.addHeader(properties.getBeverageLabel(), String.valueOf(inventory.getCount()));
			response.addHeader(properties.getCoinLabel(), String.valueOf(coinsService.getCoinsValue()-properties.getBeverageCoins()));
			coinsService.deleteCoins();
			return new ResponseEntity<InventoryJson>(inventoryJson,NO_CONTENT);
		}catch(Exception e) {
			LOG.error("Exception in getBeverage {}",e);
			return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
		}
	}
	
}

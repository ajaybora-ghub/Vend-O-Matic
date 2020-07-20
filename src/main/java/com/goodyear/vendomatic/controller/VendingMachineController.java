package com.goodyear.vendomatic.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
import com.goodyear.vendomatic.service.CoinsService;
import com.goodyear.vendomatic.service.InventoryService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class VendingMachineController {
	
	private static final String X_COINS = "X-Coins";
	
	private static final String X_INVENTORY_REMAINING = "X-Inventory-Remaining";
	
	@Autowired
	private CoinsService coinsService;
	
	@Autowired
	private InventoryService inventoryService;
	
	private static final int VENDING_LIMIT = 1;
		
	@GetMapping(value = "/inventory", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Integer[]> getInventory() {
		LOG.info("In getInventory()");
		final Integer[] inventory = inventoryService.getInventory();
		return new ResponseEntity<Integer[]>(inventory, OK);
	}
	
	@GetMapping(value = "/inventory/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Integer> getInventoryByBeverage(@PathVariable Integer id) {
		LOG.info("In getInventoryByBeverage() with beverageid {}",id);
		final Inventory inventory = inventoryService.getInventoryById(id);
		if(inventory==null) {
			return new ResponseEntity<>(BAD_REQUEST);
		}else {
			return new ResponseEntity<Integer>(inventory.getCount(), OK);
		}
	}
	
	@DeleteMapping(value = "/")
	public ResponseEntity<String> refundCoins(HttpServletResponse response) { 
		if(coinsService.getCoinsValue()==0) {
			return new ResponseEntity<>(BAD_REQUEST);
		}
		response.addHeader(X_COINS, String.valueOf(coinsService.getCoinsValue()));
		coinsService.deleteCoins();
		return new ResponseEntity<>(NO_CONTENT);
	}
	
	
	@PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> putCoins(@RequestBody CoinsJson coinsJson,HttpServletResponse response) {
		if(coinsJson.getCoin()>1||coinsJson.getCoin()==0) {
			return new ResponseEntity<>(BAD_REQUEST);
		}
		coinsService.putCoins(coinsJson);
		response.addHeader(X_COINS, String.valueOf(coinsService.getCoinsValue()));
		return new ResponseEntity<>(NO_CONTENT);
	}
	
	@PutMapping(value = "/inventory/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<InventoryJson> getBeverage(@PathVariable Integer id,HttpServletResponse response) {
		Coins coins = coinsService.getCoins();
		if(coins.getCount()<2) {
			LOG.warn("Insufficient coins {} to vend beverage {}",coins.getCount(),id);
			response.addHeader(X_COINS, String.valueOf(coins.getCount()));
			return new ResponseEntity<>(FORBIDDEN);
		}
		Inventory inventory = inventoryService.getInventoryById(id);
		if(inventory.getCount()==0) {
			LOG.warn("Beverage {} is out of stock",coins.getCount(),id);
			response.addHeader(X_COINS, String.valueOf(coins.getCount()));
			return new ResponseEntity<>(NOT_FOUND);
		}
		inventoryService.updateInventory(id);
		inventory = inventoryService.getInventoryById(id);
		coinsService.updateCoins();
		InventoryJson inventoryJson = new InventoryJson();
		inventoryJson.setQuantity(VENDING_LIMIT);
		response.addHeader(X_INVENTORY_REMAINING, String.valueOf(inventory.getCount()));
		response.addHeader(X_COINS, String.valueOf(coinsService.getCoinsValue()));
		return new ResponseEntity<InventoryJson>(inventoryJson,NO_CONTENT);
	}
	
}

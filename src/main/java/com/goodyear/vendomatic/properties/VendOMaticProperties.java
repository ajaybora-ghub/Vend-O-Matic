package com.goodyear.vendomatic.properties;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Setter
@Getter
@ConfigurationProperties(prefix = "vendomatic")
public class VendOMaticProperties implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String VENDOMATIC = "vendomatic";
	
	@Value("${"+ VENDOMATIC  +".beverage.brandsize}")
	private Integer beverageBrandSize;
	
	@Value("${"+ VENDOMATIC  +".beverage.brands}")
	private Integer beverageBrands;
	
	@Value("${"+ VENDOMATIC  +".beverage.coins}")
	private Integer beverageCoins;
	
	@Value("${"+ VENDOMATIC  +".vending.limit.coin}")
	private Integer vendingCoinLimit;
	
	@Value("${"+ VENDOMATIC  +".vending.limit.beverage}")
	private Integer vendingBeverageLimit;
	
	@Value("${"+ VENDOMATIC  +".label.coin}")
	private String coinLabel;
	
	@Value("${"+ VENDOMATIC  +".label.items}")
	private String beverageLabel;
	
}

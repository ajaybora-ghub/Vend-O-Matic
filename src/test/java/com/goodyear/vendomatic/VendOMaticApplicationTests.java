package com.goodyear.vendomatic;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.goodyear.vendomatic.model.CoinsJson;
import com.goodyear.vendomatic.service.CoinsService;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "spring.config.location = classpath:application-test.yml" })
@TestMethodOrder(Alphanumeric.class)
class VendOMaticApplicationTests {

	
	@Value("${local.server.port}")
    private int port;
	
	@Autowired
	private CoinsService coinsService;
	

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }
    
    @Test
    public void can_get_inventory_info() {
    	 get(String.format("/inventory", "")).then().statusCode(200).contentType("")
    	 		.body("[0]", equalTo(5)).body("[1]", equalTo(5)).body("[2]", equalTo(5));
    }

    @Test
    public void can_get_inventory_info_by_beverage_1() {
    	 get(String.format("/inventory/1", "")).then().statusCode(200).extract().body().toString().equals("5");
    }
    
    @Test
    public void can_get_inventory_info_by_beverage_2() {
    	 get(String.format("/inventory/2", "")).then().statusCode(200).extract().body().toString().equals("5");
    }
	
    @Test
    public void can_get_inventory_info_by_beverage_3() {
    	 get(String.format("/inventory/3", "")).then().statusCode(200).extract().body().toString().equals("5");
    }
    
    @Test
    public void can_get_inventory_info_by_beverage_4() {
    	 get(String.format("/inventory/4", "")).then().statusCode(400);
    }
    
    @Test
    public void can_put_1_coin() {
    	given().given().contentType(ContentType.JSON).body("{\"coin\" : 1}").when().put("/").then().statusCode(204).header("x-coins", "1");
    }
    
    @Test
    public void cannot_put_more_then_1_coin() {
    	given().given().contentType(ContentType.JSON).body("{\"coin\" : 2}").when().put("/").then().statusCode(400);
    }
    
    @Test
    public void cannot_put_zero_coin() {
    	given().given().contentType(ContentType.JSON).body("{\"coin\" : 0}").when().put("/").then().statusCode(400);
    }
      
    @Test
    public void cannot_vend_with_zero_coin_for_beverage_1() {
    	coinsService.deleteCoins();
     	given().given().when().put(String.format("/inventory/1", "")).then().statusCode(403).header("x-coins", "0");
    }
    
    @Test
    public void cannot_vend_with_zero_coin_for_beverage_2() {
     	given().given().when().put(String.format("/inventory/2", "")).then().statusCode(403).header("x-coins", "0");
    }
    
    @Test
    public void cannot_vend_with_zero_coin_for_beverage_3() {
     	given().given().when().put(String.format("/inventory/3", "")).then().statusCode(403).header("x-coins", "0");
    }
    
    @Test
    public void can_vend_1_beverage() {
    	CoinsJson json = new CoinsJson();
    	json.setCoin(2);
    	coinsService.putCoins(json);
    	given().given().when().put(String.format("/inventory/1", "")).then().statusCode(204).header("x-coins", "1").header("x-inventory-remaining", "4");
    }
    
}

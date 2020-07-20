package com.goodyear.vendomatic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.goodyear.vendomatic.model.Inventory;
import com.goodyear.vendomatic.service.InventoryService;
import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class VendingMachineConfig {
	
	@Autowired
	private InventoryService inventoryService;

	@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot"))).build().pathMapping("/");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Vend-O-Matic API")
        		.description("Vend-O-Matic API")
                .version("1.0").build();
    }
	
    @Bean
    public Inventory initInventory() {
    	inventoryService.refill();
    	return inventoryService.getInventoryById(1);
    }
	
}

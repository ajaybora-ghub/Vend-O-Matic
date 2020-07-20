package com.goodyear.vendomatic.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InventoryJson{
	
	private Integer quantity;

}

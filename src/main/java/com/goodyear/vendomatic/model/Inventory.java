package com.goodyear.vendomatic.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "Inventory")
public class Inventory {

	@javax.persistence.Id
	@GeneratedValue(strategy = IDENTITY)
    private Integer id;
	
    private Integer count;
	
}

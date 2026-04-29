package com.project.radix.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IsotopeResponse {
    private Integer id;
    private String name;
    private String symbol;
    private String type;
    private Double halfLife;
    private String halfLifeUnit;
}
package com.github.not.n0w.weblab3Bot.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class Hit implements Serializable {
    private Long id;
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal r;
    private Boolean hit;
    private Double executionTime;
    private String currentDatetime;
    private String sessionId;
}

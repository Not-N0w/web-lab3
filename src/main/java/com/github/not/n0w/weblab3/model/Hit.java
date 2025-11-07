package com.github.not.n0w.weblab3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


@NoArgsConstructor
@Entity
@Table(name = "hits")
public class Hit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private BigDecimal x;

    @Column
    private BigDecimal y;

    @Column
    private BigDecimal r;

    @Column
    private boolean hit;

    @Column
    private Double executionTime;

    @Column
    private Date currentDatetime;

    public void setId(Long id) {
        this.id = id;
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    public void setR(BigDecimal r) {
        this.r = r;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public void setExecutionTime(Double executionTime) {
        this.executionTime = executionTime;
    }

    public void setCurrentDatetime(Date currentDatetime) {
        this.currentDatetime = currentDatetime;
    }

    public Date getCurrentDatetime() {
        return currentDatetime;
    }

    public boolean isHit() {
        return hit;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getX() {
        return x;
    }

    public BigDecimal getY() {
        return y;
    }

    public BigDecimal getR() {
        return r;
    }

    public boolean getHit() {
        return hit;
    }

    public Double getExecutionTime() {
        return executionTime;
    }

    public String toJson() {
        return "{" +
                "\"x\":" + x.toPlainString() +
                ",\"y\":" + y.toPlainString() +
                ",\"r\":" + r.toPlainString() +
                ",\"isHit\":" + (hit ? "true" : "false") +
                "}";
    }

}

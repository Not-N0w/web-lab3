package com.github.not.n0w.weblab3;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AreaChecker {
    public static boolean hit(BigDecimal x, BigDecimal y, BigDecimal r) {
        BigDecimal zero = BigDecimal.ZERO;
        boolean isHit = false;

        if (x.compareTo(zero) >= 0 && y.compareTo(zero) >= 0) {
            BigDecimal radius = r.divide(BigDecimal.valueOf(2));
            BigDecimal sum = x.multiply(x).add(y.multiply(y));
            if (sum.compareTo(radius.multiply(radius)) <= 0) {
                isHit = true;
            }
        }

        if (x.compareTo(zero) <= 0 && y.compareTo(zero) <= 0) {
            if (x.compareTo(r.negate()) >= 0 && y.compareTo(r.divide(BigDecimal.valueOf(2).negate())) >= 0) {
                isHit = true;
            }
        }

        if (x.compareTo(zero) >= 0 && y.compareTo(zero) <= 0) {
            BigDecimal border = x.multiply(BigDecimal.valueOf(2)).subtract(r);
            if (y.compareTo(border) >= 0) {
                isHit = true;
            }
        }

        return isHit;
    }

}


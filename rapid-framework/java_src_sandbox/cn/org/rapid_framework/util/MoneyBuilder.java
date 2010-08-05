
package cn.org.rapid_framework.util;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Currency;

/**
 * 
 * 用于构建Money
 * @author badqiu
 *
 */
public class MoneyBuilder {

    private Money money = new Money();

    private MoneyBuilder(Money money) {
        super();
        this.money = money;
    }
    
    public MoneyBuilder add(Money m) {
        money = money.add(m);
        return this;
    }
    
    public MoneyBuilder multiply(long m) {
        money = money.multiply(m);
        return this;
    }

    public MoneyBuilder divide(long m) {
        money = money.divide(m);
        return this;
    }
    
    public MoneyBuilder subtract(Money m) {
        money = money.subtract(m);
        return this;
    }    
    
    public Money toMoney() {
        return money;
    }
    
    public String toString() {
        return money.toString();
    }
}

package com.example.pi.stockquotes;

/**
 * Created by pi on 08-Mar-15.
 */
public class StockInfo {
    private String daysLow = "";
    private String daysHigh = "";
    private String yearLow = "";
    private String yearHigh = "";
    private String name = "";
    private String lastTradePriceOnly = "";
    private String change = "";
    private String daysRange = "";

    public String getDaysRange() {
        return daysRange;
    }

    public void setDaysRange(String daysRange) {
        this.daysRange = daysRange;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getLastTradePriceOnly() {
        return lastTradePriceOnly;
    }

    public void setLastTradePriceOnly(String lastTradePriceOnly) {
        this.lastTradePriceOnly = lastTradePriceOnly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYearHigh() {
        return yearHigh;
    }

    public void setYearHigh(String yearHigh) {
        this.yearHigh = yearHigh;
    }

    public String getYearLow() {
        return yearLow;
    }

    public void setYearLow(String yearLow) {
        this.yearLow = yearLow;
    }

    public String getDaysHigh() {
        return daysHigh;
    }

    public void setDaysHigh(String daysHigh) {
        this.daysHigh = daysHigh;
    }

    public StockInfo(String daysLow, String daysHigh, String yearLow, String yearHigh, String name, String lastTradePriceOnly, String change, String daysRange) {
        this.daysLow = daysLow;
        this.daysHigh = daysHigh;
        this.yearLow = yearLow;
        this.yearHigh = yearHigh;
        this.name = name;
        this.lastTradePriceOnly = lastTradePriceOnly;
        this.change = change;
        this.daysRange = daysRange;
    }

    public String getDaysLow() {
        return daysLow;
    }

    public void setDaysLow(String daysLow) {
        this.daysLow = daysLow;
    }


}

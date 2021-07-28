package com.ringstechnology.subscribed;

public class ItemMySubscription { String  name, cycle, logo,currency; double fee;

    public ItemMySubscription(){

    }

    public ItemMySubscription(long fee, String name, String logo, String cycle,String currency){
        this.fee = fee;
        this.name = name;
        this.logo = logo;
        this.cycle = cycle;
        this.currency =currency;
    }

    public double getFee() {
        return fee;
    }

    public String getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }

    public String getCycle() {
        return cycle;
    }

    public String getCurrency() {
        return currency;
    }
}
package com.ringstechnology.subscribed;

public class ItemUpcoming {String name, date, logo; long due;

   public ItemUpcoming(){

   }

    public ItemUpcoming(long due, String name, String logo, String date){
        this.due = due;
        this.name = name;
        this.logo = logo;
        this.date = date;
    }

    public long getDue() {
        return due;
    }

    public String getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}

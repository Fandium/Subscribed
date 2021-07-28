package com.ringstechnology.subscribed;

public class ItemNotifications {String message, date,logo;

   public ItemNotifications(){

   }

    public ItemNotifications(String message, String date, String logo){
        this.message = message;
        this.date = date;
        this.logo = logo;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getLogo() {
        return logo;
    }
}

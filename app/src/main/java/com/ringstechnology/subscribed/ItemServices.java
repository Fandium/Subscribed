package com.ringstechnology.subscribed;

public class ItemServices {
    String category, name, logo;

    public ItemServices(){

    }

    public ItemServices(String category, String name, String logo){
        this.category = category;
        this.name = name;
        this.logo = logo;
    }

    public String getCategory() {
        return category;
    }

    public String getLogo() {
        return logo;
    }

    public String getName() {
        return name;
    }
}

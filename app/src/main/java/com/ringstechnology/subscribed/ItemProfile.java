package com.ringstechnology.subscribed;

public class ItemProfile {
    private int image;
    private String text;

    public ItemProfile(int mimage, String mtext){
        image = mimage;
        text = mtext;
    }

    public int getImage(){
        return image;
    }

    public String getText(){
        return text;
    }
}

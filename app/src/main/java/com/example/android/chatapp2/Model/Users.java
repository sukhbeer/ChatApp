package com.example.android.chatapp2.Model;

public class Users {

    public String name;
    public String image;
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Users(){ }

    public Users(String name, String image,String id) {
        this.name = name;
        this.image = image;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

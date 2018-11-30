package com.example.bamaproject.sar.model;

public class UsersModel extends userID {

    String name,image;

    public UsersModel(){

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UsersModel(String name, String image){
        this.name = name ;
        this.image = image ;
    }

}

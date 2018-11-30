package com.example.bamaproject.sar.model;

import android.support.annotation.NonNull;

public class userID {

    public String user_id;


    public <T extends userID> T withId(@NonNull final String id){

        this.user_id = id ;
        return (T) this ;

    }



}

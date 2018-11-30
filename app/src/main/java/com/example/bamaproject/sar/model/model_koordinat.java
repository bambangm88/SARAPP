package com.example.bamaproject.sar.model;

public class model_koordinat{

    //Deklarasi Variable
    private String lat;
    private String longi;
    private String key;
    private String nama;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    private String imageUrl;

    public String getTim() {
        return tim;
    }

    public void setTim(String tim) {
        this.tim = tim;
    }

    private String tim;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }


    public model_koordinat(){
    }

    public model_koordinat(String lat, String longi,String nama, String imageUrl ,String tim) {
        this.lat = lat;
        this.longi = longi;
        this.nama = nama;
        this.tim = tim ;
        this.imageUrl = imageUrl ;



}
}
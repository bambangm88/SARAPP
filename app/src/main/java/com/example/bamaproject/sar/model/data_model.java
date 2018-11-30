package com.example.bamaproject.sar.model;

public class data_model{

    //Deklarasi Variable
    private String lat;
    private String longi;
    private String nama;
    private String imageUrl;
    private String key;
    private String pesan;

    public String getTim() {
        return tim;
    }

    public void setTim(String tim) {
        this.tim = tim;
    }

    private String tim;

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    private String tanggal;

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

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    //Membuat Konstuktor kosong untuk membaca data snapshot
    public data_model(){
    }

    //Konstruktor dengan beberapa parameter, untuk mendapatkan Input Data dari User
    public data_model(String lat, String longi, String nama, String tanggal, String imageUrl, String pesan, String tim) {
        this.lat = lat;
        this.longi = longi;
        this.nama = nama;
        this.tanggal = tanggal;
        this.imageUrl = imageUrl;
        this.pesan = pesan;
        this.tim = tim;
    }
}
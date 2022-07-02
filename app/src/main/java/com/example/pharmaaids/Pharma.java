package com.example.pharmaaids;

public class Pharma {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String imgUrl;
    private String location;

   private Double lan;
   private Double lon;


    public Pharma() {
    }

//    public Pharma(String id, String name, String email, String phone, String imgUrl) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.phone = phone;
//        this.imgUrl = imgUrl;
//    }


//////////////////////////////////////

    public Pharma(String id, String name, String email, String phone,String imgUrl,String location, Double lan, Double lon) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.imgUrl = imgUrl;
        this.location = location;
        this.lan = lan;
        this.lon = lon;
    }


    ///////////////////////////////////////



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /////////////////////////////////////////////////////////

    public Double getLan() {
        return lan;
    }

    public void setLan(Double lan) {
        this.lan = lan;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }



}

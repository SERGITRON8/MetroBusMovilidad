package com.example.metrobusmovilidad;

public class Model_usuario {
    private String name;
    private  String phone;
    private String type;
    private String arrived;
    private String email;

    public Model_usuario(){}

    public Model_usuario(String name, String phone, String type, String arrived, String email) {
        this.name = name;
        this.phone = phone;
        this.type = type;
        this.arrived = arrived;
        this.email = email;
    }

    public String getName(){
        return name;
    }
    public String getPhone(){
        return phone;
    }
    public String getType(){
        return type;
    }
    public String getArrived(){
        return arrived;
    }
}

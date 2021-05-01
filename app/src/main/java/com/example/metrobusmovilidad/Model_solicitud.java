package com.example.metrobusmovilidad;

public class Model_solicitud {
    private String user_name;
    private String user_phone;
    private String id_origin_line;
    private String id_origin_estacion;
    private String origin_line;
    private String origin_estacion;
    private String id_destiny_line;
    private String id_destiny_estacion;
    private String destiny_line;
    private String destiny_estacion;

    public Model_solicitud(){}

    public Model_solicitud(String destination_linea, String destination_estacion, String origen_linea, String origen_estacion, String user_name, String user_phone, String id_origin_line, String id_origin_estacion, String origin_line, String origin_estacion, String id_destiny_line, String id_destiny_estacion, String destiny_line, String destiny_estacion) {
        this.user_name = user_name;
        this.user_phone = user_phone;
        this.id_origin_line = id_origin_line;
        this.id_origin_estacion = id_origin_estacion;
        this.origin_line = origin_line;
        this.origin_estacion = origin_estacion;
        this.id_destiny_line = id_destiny_line;
        this.id_destiny_estacion = id_destiny_estacion;
        this.destiny_line = destiny_line;
        this.destiny_estacion = destiny_estacion;
    }

    public String getDestination(){
        return destiny_line + " - " + destiny_estacion;
    }

    public String getOrigin(){
        return origin_line + " - " + origin_estacion;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_phone() {
        return String.valueOf(user_phone);
    }

    public String getId_origin_line() {
        return String.valueOf(id_origin_line);
    }

    public String getId_origin_estacion() {
        return String.valueOf(id_origin_estacion);
    }

    public String getOrigin_line() {
        return origin_line;
    }

    public String getOrigin_estacion() {
        return origin_estacion;
    }

    public String getId_destiny_line() {
        return String.valueOf(id_destiny_line);
    }

    public String getId_destiny_estacion() {
        return String.valueOf(id_destiny_estacion);
    }

    public String getDestiny_line() {
        return destiny_line;
    }

    public String getDestiny_estacion() {
        return destiny_estacion;
    }
}

package com.example.dillus.stockapp.AppData;

public class ClsUserTienda {

    private String id_user;
    private String id_tienda;

    public ClsUserTienda() {
    }

    public ClsUserTienda(String id_user, String id_tienda) {
        this.id_user = id_user;
        this.id_tienda = id_tienda;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_tienda() {
        return id_tienda;
    }

    public void setId_tienda(String id_tienda) {
        this.id_tienda = id_tienda;
    }
}

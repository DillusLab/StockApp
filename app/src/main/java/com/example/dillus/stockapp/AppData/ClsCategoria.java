package com.example.dillus.stockapp.AppData;

public class ClsCategoria {

    private String id_tienda;
    private String tipo;

    public ClsCategoria() {
    }

    public ClsCategoria(String id_tienda, String tipo) {
        this.id_tienda = id_tienda;
        this.tipo = tipo;
    }

    public String getId_tienda() {
        return id_tienda;
    }

    public void setId_tienda(String id_tienda) {
        this.id_tienda = id_tienda;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}

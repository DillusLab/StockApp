package com.example.dillus.stockapp.AppData;

import android.net.Uri;

import java.util.List;

public class ClsProducto {

    private String codigo;
    private String id_tienda;
    private String nombre;
    private String descripcion;
    private String categoria;
    private String unidad;
    private String marca;
    private String uri;
    private List<String> colores;
    private List<String> compras;

    public ClsProducto() {
    }

    public ClsProducto(String codigo, String id_tienda, String nombre, String descripcion, String categoria, String unidad, String marca, String uri, List<String> colores) {
        this.codigo = codigo;
        this.id_tienda = id_tienda;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.unidad = unidad;
        this.marca = marca;
        this.uri = uri;
        this.colores = colores;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getId_tienda() {
        return id_tienda;
    }

    public void setId_tienda(String id_tienda) {
        this.id_tienda = id_tienda;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<String> getColores() {
        return colores;
    }

    public void setColores(List<String> colores) {
        this.colores = colores;
    }
}

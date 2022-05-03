package com.sellba.app.sellbatery.Class;

import java.io.Serializable;
public class Articulo implements Serializable {
    private String id;
    private String nombre;
    private String precio;
    private String descripcion;
    private String vendedor;
    private String imagen;
    public Articulo() {
    }

    public Articulo(String id, String nombre, String precio, String descripcion, String imagen,String vendedor) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.vendedor = vendedor;
        this.imagen = imagen;
    }

    public Articulo(String nombre, String precio, String descripcion, String vendedor ) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.vendedor = vendedor;

    }public Articulo(String nombre, String precio, String descripcion, String imagen,String vendedor) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.vendedor = vendedor;
        this.imagen=imagen;

    }
    public Articulo(String nombre, String precio, String descripcion ) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
    }



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getId() {
        return id;
    }
}

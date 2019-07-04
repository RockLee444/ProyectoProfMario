/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moduloventa;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Luis Matuz
 */
public class Producto {
    private int codigo;
    private String nombre;
    private int cantidad;
    private double precio;
    private ImageView imagen;
    private int totalAComprar=0;
    
    public Producto(int codigo, String nombre, int cantidad, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public ImageView getImagen() {
        return imagen;
    }

    public void setImagen(String url) {
        this.imagen = new ImageView(new Image(url));
    }
    
    public int getTotalAComprar() {
        return totalAComprar;
    }

    public void setTotalAComprar(int totalAComprar) {
        this.totalAComprar = totalAComprar;
    }
    
    public String getInformacion(){
        return ("Codigo: " + codigo + "\nNombre: " + nombre + "\nCantidad: " + cantidad + "\nPrecio: $" + precio + "\n");
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}

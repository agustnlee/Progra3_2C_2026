public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private double calificacion;
 
    public Producto(int id, String nombre, double precio, double calificacion) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.calificacion = calificacion;
    }
 
    public int getId() {
        return id;
    }
 
    public String getNombre() {
        return nombre;
    }
 
    public double getPrecio() {
        return precio;
    }
 
    public double getCalificacion() {
        return calificacion;
    }
 
    @Override
    public String toString() {
        return "{id=" + id + ", nombre='" + nombre + "', precio=" + precio
                + ", calificacion=" + calificacion + "}";
    }
}

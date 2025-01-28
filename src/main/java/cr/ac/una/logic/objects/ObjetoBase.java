package cr.ac.una.logic.objects;

public abstract class ObjetoBase {
    protected String nombre;
    protected String descripcion;
    protected String id;
    public ObjetoBase(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }
    public ObjetoBase() {}
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + "[" + id + "] " + nombre + ": " + descripcion;
    }
}

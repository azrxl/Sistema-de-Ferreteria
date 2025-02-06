package cr.ac.una.proxy.objects;

public class Subcategoria extends ObjetoBase {
    private String categoriaID;
    public Subcategoria(String id, String name, String description, String categoriaID) {
        super(id, name, description);
        this.categoriaID = categoriaID;
    }
    public void setCategoriaID(String id) {
        this.categoriaID = id;
    }
    public String getCategoriaID() {
        return categoriaID;
    }
}

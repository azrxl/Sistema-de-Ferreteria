package cr.ac.una.data;

import cr.ac.una.logic.objects.Articulo;
import cr.ac.una.logic.objects.Categoria;
import cr.ac.una.logic.objects.Presentacion;
import cr.ac.una.logic.objects.Subcategoria;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLHandler {
    private static final String fileSource = "data.xml";
    public XMLHandler() {}

    private Document getDoc() throws ParserConfigurationException, IOException, SAXException {
        File file = new File(fileSource);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();
        return document;
    }

    public List<Categoria> cargarCategorias() throws ParserConfigurationException, IOException, SAXException {
        List<Categoria> categorias = new ArrayList<>();
        NodeList categoriaNodes = getDoc().getElementsByTagName("categoria");

        for (int i = 0; i < categoriaNodes.getLength(); i++) {
            Element categoriaElement = (Element) categoriaNodes.item(i);
            String id = categoriaElement.getAttribute("ID");
            String nombre = getTextContent(categoriaElement, "nombre");
            String descripcion = getTextContent(categoriaElement, "descripcion");
            categorias.add(new Categoria(id, nombre, descripcion));
        }
        return categorias;
    }

    public List<Subcategoria> cargarSubcategorias() throws ParserConfigurationException, IOException, SAXException {
        List<Subcategoria> subcategorias = new ArrayList<>();
        NodeList categoriaNodes = getDoc().getElementsByTagName("categoria");

        for (int i = 0; i < categoriaNodes.getLength(); i++) {
            Element categoriaElement = (Element) categoriaNodes.item(i);
            String categoriaID = categoriaElement.getAttribute("ID");
            NodeList subcategoriaNodes = categoriaElement.getElementsByTagName("subcategoria");

            for (int j = 0; j < subcategoriaNodes.getLength(); j++) {
                Element subcategoriaElement = (Element) subcategoriaNodes.item(j);
                String id = subcategoriaElement.getAttribute("ID");
                String nombre = getTextContent(subcategoriaElement, "nombre");
                String descripcion = getTextContent(subcategoriaElement, "descripcion");
                Subcategoria subcategoria = new Subcategoria(id, nombre, descripcion, categoriaID);
                subcategorias.add(subcategoria);
            }
        }
        return subcategorias;
    }

    public List<Articulo> cargarArticulos() throws ParserConfigurationException, IOException, SAXException {
        List<Articulo> articulos = new ArrayList<>();
        NodeList subcategoriaNodes = getDoc().getElementsByTagName("subcategoria");

        for (int i = 0; i < subcategoriaNodes.getLength(); i++) {
            Element subcategoriaElement = (Element) subcategoriaNodes.item(i);
            String subcategoriaID = subcategoriaElement.getAttribute("ID");
            NodeList articuloNodes = subcategoriaElement.getElementsByTagName("articulo");

            for (int j = 0; j < articuloNodes.getLength(); j++) {
                Element articuloElement = (Element) articuloNodes.item(j);
                String id = articuloElement.getAttribute("ID");
                String nombre = getTextContent(articuloElement, "nombre");
                String marca = getTextContent(articuloElement, "marca");
                String descripcion = getTextContent(articuloElement, "descripcion");
                Articulo articulo = new Articulo(id, nombre, marca, descripcion, subcategoriaID);
                articulos.add(articulo);
            }
        }
        return articulos;
    }

    public List<Presentacion> cargarPresentaciones() throws ParserConfigurationException, IOException, SAXException {
        List<Presentacion> presentaciones = new ArrayList<>();
        NodeList articuloNodes = getDoc().getElementsByTagName("articulo");

        for (int i = 0; i < articuloNodes.getLength(); i++) {
            Element articuloElement = (Element) articuloNodes.item(i);
            String articuloID = articuloElement.getAttribute("ID");
            NodeList presentacionNodes = articuloElement.getElementsByTagName("presentacion");

            for (int j = 0; j < presentacionNodes.getLength(); j++) {
                Element presentacionElement = (Element) presentacionNodes.item(j);
                String unidad = getTextContent(presentacionElement, "unidad");
                String capacidad = getTextContent(presentacionElement, "capacidad");
                String precioCompra = getTextContent(presentacionElement, "precioCompra");
                String precioVenta = getTextContent(presentacionElement, "precioVenta");
                String cantidad = getTextContent(presentacionElement, "cantidad");
                Presentacion presentacion = new Presentacion(unidad, capacidad, articuloID,Double.parseDouble(precioCompra),Double.parseDouble(precioVenta),Integer.parseInt(cantidad));
                presentaciones.add(presentacion);
            }
        }
        return presentaciones;
    }

    public List<String> cargarUnidades() throws ParserConfigurationException, IOException, SAXException {
        List<String> unidades = new ArrayList<>();
        NodeList unidadNodes = getDoc().getElementsByTagName("unidadValida");

        for (int i = 0; i < unidadNodes.getLength(); i++) {
            Element unidadElement = (Element) unidadNodes.item(i);
            String unidad = unidadElement.getTextContent();
            unidades.add(unidad);
        }
        return unidades;
    }


    public void guardar(Data data) throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        // Crear el root 'data'
        Element root = document.createElement("data");
        document.appendChild(root);

        // Crear el nodo 'categorias' y agregarlo al root
        Element categoriasElement = document.createElement("categorias");
        root.appendChild(categoriasElement);

        List<Categoria> categorias = data.getCategorias();
        List<Subcategoria> subcategorias = data.getSubcategorias();
        List<Articulo> articulos = data.getArticulos();
        List<Presentacion> presentaciones = data.getPresentaciones();

        // Recorrer categorías
        for (Categoria categoria : categorias) {
            Element categoriaElement = document.createElement("categoria");
            categoriaElement.setAttribute("ID", categoria.getId());

            Element nombreCategoria = document.createElement("nombre");
            nombreCategoria.setTextContent(categoria.getNombre());
            categoriaElement.appendChild(nombreCategoria);

            Element descripcionCategoria = document.createElement("descripcion");
            descripcionCategoria.setTextContent(categoria.getDescripcion());
            categoriaElement.appendChild(descripcionCategoria);

            // Recorrer subcategorías y filtrar las que pertenecen a esta categoría
            for (Subcategoria subcategoria : subcategorias) {
                if (subcategoria.getCategoriaID().equals(categoria.getId())) {
                    Element subcategoriaElement = document.createElement("subcategoria");
                    subcategoriaElement.setAttribute("ID", subcategoria.getId());

                    Element nombreSubcategoria = document.createElement("nombre");
                    nombreSubcategoria.setTextContent(subcategoria.getNombre());
                    subcategoriaElement.appendChild(nombreSubcategoria);

                    Element descripcionSubcategoria = document.createElement("descripcion");
                    descripcionSubcategoria.setTextContent(subcategoria.getDescripcion());
                    subcategoriaElement.appendChild(descripcionSubcategoria);

                    // Recorrer artículos y filtrar los que pertenecen a esta subcategoría
                    for (Articulo articulo : articulos) {
                        if (articulo.getSubcategoriaID().equals(subcategoria.getId())) {
                            Element articuloElement = document.createElement("articulo");
                            articuloElement.setAttribute("ID", articulo.getId());

                            Element nombreArticulo = document.createElement("nombre");
                            nombreArticulo.setTextContent(articulo.getNombre());
                            articuloElement.appendChild(nombreArticulo);

                            Element descripcionArticulo = document.createElement("descripcion");
                            descripcionArticulo.setTextContent(articulo.getDescripcion());
                            articuloElement.appendChild(descripcionArticulo);

                            Element marcaArticulo = document.createElement("marca");
                            marcaArticulo.setTextContent(articulo.getMarca());
                            articuloElement.appendChild(marcaArticulo);

                            // Recorrer presentaciones y filtrar las que pertenecen a este artículo
                            for (Presentacion presentacion : presentaciones) {
                                if (presentacion.getArticuloID().equals(articulo.getId())) {
                                    Element presentacionElement = document.createElement("presentacion");

                                    Element unidad = document.createElement("unidad");
                                    unidad.setTextContent(presentacion.getUnidad());
                                    presentacionElement.appendChild(unidad);

                                    Element capacidad = document.createElement("capacidad");
                                    capacidad.setTextContent(presentacion.getCapacidad());
                                    presentacionElement.appendChild(capacidad);

                                    Element precioCompra = document.createElement("precioCompra");
                                    precioCompra.setTextContent(String.valueOf(presentacion.getPrecioCompra()));
                                    presentacionElement.appendChild(precioCompra);

                                    Element precioVenta = document.createElement("precioVenta");
                                    precioVenta.setTextContent(String.valueOf(presentacion.getPrecioVenta()));
                                    presentacionElement.appendChild(precioVenta);

                                    Element cantidad = document.createElement("cantidad");
                                    cantidad.setTextContent(String.valueOf(presentacion.getCantidad()));
                                    presentacionElement.appendChild(cantidad);

                                    articuloElement.appendChild(presentacionElement);
                                }
                            }
                            subcategoriaElement.appendChild(articuloElement);
                        }
                    }
                    categoriaElement.appendChild(subcategoriaElement);
                }
            }
            categoriasElement.appendChild(categoriaElement);
        }

        Element unidadesElement = document.createElement("unidades");
        root.appendChild(unidadesElement);

        List<String> unidadesValidas = data.getUnidades();

        for (String unidad : unidadesValidas) {
            Element unidadElement = document.createElement("unidadValida");
            unidadElement.setTextContent(unidad);
            unidadesElement.appendChild(unidadElement);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File("data.xml"));
        transformer.transform(source, result);
    }

    private String getTextContent(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent().trim();
        }
        return "";
    }
}

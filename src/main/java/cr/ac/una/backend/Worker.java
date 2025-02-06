package cr.ac.una.backend;

import cr.ac.una.proxy.IService;
import cr.ac.una.proxy.Protocol;
import cr.ac.una.proxy.objects.*;
import java.io.*;

public class Worker {
    private final Server srv;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final IService service;
    private final User user;
    private boolean continuar;

    public Worker(Server srv, ObjectInputStream in, ObjectOutputStream out, User user, IService service) {
        this.srv = srv;
        this.in = in;
        this.out = out;
        this.user = user;
        this.service = service;
    }

    public User getUser() {
        return user;
    }

    public void start(){
        try {
            System.out.println("Worker atendiendo peticiones...");
            Thread t = new Thread(this::listen);
            continuar = true;
            t.start();
        } catch (Exception ignored) {}
    }

    public void stop() {
        continuar = false;
        System.out.println("Conexión cerrada para el usuario: " + user.getIdentificacion());
    }

    public void listen() {
        int method;
        while (continuar) {
            try {
                method = in.readInt();
                System.out.println("Operación recibida: " + method);
                switch(method) {
                    case Protocol.LOGOUT:
                        try {
                            srv.remove(user);
                        } catch (Exception ex) {
                            System.err.println("Error en logout: " + ex.getMessage());
                        }
                        stop();
                        break;
                    case Protocol.PEDIDO:
                        // Se recibe un pedido desde el cliente
                        Pedido pedido = (Pedido) in.readObject();
                        try {
                            // Se procesa el pedido: se actualiza el inventario, se genera la factura, etc.
                            service.confirmarPedido(pedido.getItems());
                            Factura factura = service.generarFactura(pedido);

                            // Se responde al cliente que realizó el pedido (por ejemplo, con la factura)
                            out.writeInt(Protocol.ERROR_NO_ERROR);
                            out.writeObject(factura);
                            out.flush();

                            // Se notifica a todos los clientes para que actualicen su información (por ejemplo, el inventario)
                            // Aquí se puede enviar la información actualizada o simplemente una señal para que el cliente haga una nueva solicitud.
                            srv.broadcastOrderUpdate("update_inventory");
                        } catch (Exception ex) {
                            out.writeInt(Protocol.ERROR_PEDIDO);
                            out.writeObject(ex.getMessage());
                            out.flush();
                        }
                        break;
                    default:
                        System.out.println("Operación no soportada: " + method);
                        break;
                }
            } catch (IOException | ClassNotFoundException ex) {
                System.err.println("Error en el Worker (" + user.getIdentificacion() + "): " + ex.getMessage());
                continuar = false;
            }
        }
    }

    // Método para enviar una notificación de actualización al cliente.
    public void deliverUpdate(Object updateInfo) {
        try {
            out.writeInt(Protocol.UPDATE_INVENTORY);
            out.writeObject(updateInfo);
            out.flush();
        } catch (IOException ex) {
            System.err.println("Error enviando actualización a " + user.getIdentificacion() + ": " + ex.getMessage());
        }
    }
}


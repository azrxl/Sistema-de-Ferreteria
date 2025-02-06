package cr.ac.una.frontend.logic;

import cr.ac.una.frontend.presentation.controller.Controller;
import cr.ac.una.proxy.IService;
import cr.ac.una.proxy.Protocol;
import cr.ac.una.proxy.objects.CarritoItem;
import cr.ac.una.proxy.objects.Factura;
import cr.ac.una.proxy.objects.Pedido;
import cr.ac.una.proxy.objects.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import javax.swing.SwingUtilities;

public class ProxyService implements IService {
    private static IService theInstance;

    public static IService instance() {
        if (theInstance == null) {
            theInstance = new ProxyService();
        }
        return theInstance;
    }

    ObjectInputStream in;
    ObjectOutputStream out;
    Controller controller;
    Socket skt;

    private void connect() throws Exception {
        skt = new Socket(Protocol.SERVER, Protocol.PORT);
        out = new ObjectOutputStream(skt.getOutputStream());
        out.flush();
        in = new ObjectInputStream(skt.getInputStream());
    }

    private void disconnect() throws Exception {
        skt.shutdownOutput();
        skt.close();
    }

    // Método de login (sin cambios importantes)
    public User login(User u) throws Exception {
        connect();
        try {
            out.writeInt(Protocol.LOGIN);
            out.writeObject(u);
            out.flush();
            int response = in.readInt();

            if (response == Protocol.ERROR_NO_ERROR) {
                User u1 = (User) in.readObject();
                this.start();
                return u1;
            } else if (response == Protocol.ERROR_ALREADY_LOGGED_IN) {
                disconnect();
                throw new Exception("El usuario ya tiene una sesión activa.");
            } else if (response == Protocol.ERROR_BLOCKED) {
                disconnect();
                throw new Exception("El usuario está bloqueado por múltiples intentos fallidos.");
            } else if (response == Protocol.ERROR_LOGIN) {
                disconnect();
                throw new Exception("Usuario o contraseña incorrectos. Por favor intente nuevamente.");
            } else {
                disconnect();
                throw new Exception("Error desconocido.");
            }
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }
    }

    // Logout: se envía la operación de cierre de sesión y se desconecta
    public void logout(User u) throws Exception {
        out.writeInt(Protocol.LOGOUT);
        out.writeObject(u);
        out.flush();
        this.stop();
        this.disconnect();
    }

    /**
     * Método para realizar un pedido.
     * Se envía al servidor el pedido (representado aquí como un objeto Message,
     * aunque podría ser otro objeto, por ejemplo, Pedido) usando el código de protocolo PEDIDO.
     */
    public void placeOrder(Pedido orderMessage) {
        try {
            out.writeInt(Protocol.PEDIDO);
            out.writeObject(orderMessage);
            out.flush();
        } catch (IOException ex) {
            // Aquí se podría notificar el error al controlador o registrar el fallo.
        }
    }

    // Métodos de escucha (LISTENING FUNCTIONS)
    boolean continuar = true;

    public void start() {
        System.out.println("Client worker atendiendo peticiones...");
        Thread t = new Thread(this::listen);
        continuar = true;
        t.start();
    }

    public void stop() {
        continuar = false;
    }

    /**
     * En este método se escucha constantemente el canal de entrada del socket.
     * Se procesa únicamente la notificación de actualización de existencias (cuando se recibe un pedido).
     */
    public void listen() {
        int method;
        while (continuar) {
            try {
                method = in.readInt();
                System.out.println("DELIVERY - Operación: " + method);
                // Se utiliza DELIVER para entregar la notificación de actualización de inventario
                if (method == Protocol.DELIVER) {
                    try {
                        Pedido orderUpdate = (Pedido) in.readObject();
                        deliver(orderUpdate);
                    } catch (ClassNotFoundException ignored) {
                    }
                    // Se pueden incluir otros casos si fueran necesarios
                }
                out.flush();
            } catch (IOException ex) {
                continuar = false;
            }
        }
    }

    /**
     * Envía la notificación al controlador en el hilo de eventos de Swing,
     * para actualizar la vista (por ejemplo, refrescar el inventario en pantalla).
     */
    private void deliver(final Pedido orderUpdate) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Se asume que el controlador implementa un método updateInventory que
                // actualiza la vista con la nueva información (existencias, precios, etc.)
                controller.updateInventory(orderUpdate);
            }
        });
    }

    // Método para asignar el controlador
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void confirmarPedido(List<CarritoItem> items) {

    }

    @Override
    public Factura generarFactura(Pedido pedido) {
        return null;
    }
}

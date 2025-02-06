package cr.ac.una.proxy;

public class Protocol {
    public static final String SERVER = "localhost";
    public static final int PORT = 1234;

    public static final int LOGIN = 1;
    public static final int LOGOUT = 2;
    public static final int PEDIDO = 3;      // Envío de pedido desde el cliente
    public static final int DELIVER = 4;     // Notificación de actualización (broadcast de pedido)
    public static final int UPDATE_INVENTORY = 5;

    public static final int ERROR_NO_ERROR = 0;
    public static final int ERROR_LOGIN = 1;
    public static final int ERROR_ALREADY_LOGGED_IN = 2;
    public static final int ERROR_BLOCKED = 3;
    public static final int ERROR_PEDIDO = 4;
}

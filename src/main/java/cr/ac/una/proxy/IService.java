package cr.ac.una.proxy;

import cr.ac.una.proxy.objects.CarritoItem;
import cr.ac.una.proxy.objects.Factura;
import cr.ac.una.proxy.objects.Pedido;
import cr.ac.una.proxy.objects.User;

import java.util.List;

public interface IService {
    void confirmarPedido(List<CarritoItem> items);

    Factura generarFactura(Pedido pedido);

    User login(User user) throws Exception;
    void logout(User loggedUser) throws Exception;
}

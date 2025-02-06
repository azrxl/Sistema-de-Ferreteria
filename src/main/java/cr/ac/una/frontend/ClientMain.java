package cr.ac.una.frontend;

import cr.ac.una.frontend.logic.ProxyService;
import cr.ac.una.proxy.objects.User;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== SISTEMA DE LOGIN ===");

        System.out.print("Ingrese su usuario: ");
        String usuario = scanner.nextLine();

        System.out.print("Ingrese su contraseña: ");
        String clave = scanner.nextLine();

        // Se crea el objeto User (ajusta según la implementación real)
        User user = new User(usuario, clave);

        try {
            // Se utiliza el ServiceProxy para realizar el login
            User loggedUser = ProxyService.instance().login(user);
            System.out.println("Login exitoso. Bienvenido, " + loggedUser.getIdentificacion() + "!");

            // Aquí se podría continuar con el resto de la aplicación,
            // por ejemplo, mostrando un menú o esperando órdenes.
            // En este ejemplo, se espera indefinidamente para simular la conexión.
            System.out.println("Presione ENTER para salir...");
            scanner.nextLine();

            // Al finalizar, se puede realizar logout
            ProxyService.instance().logout(loggedUser);
            System.out.println("Sesión cerrada. ¡Hasta luego!");
        } catch (Exception ex) {
            System.err.println("Error durante el login: " + ex.getMessage());
        } finally {
            scanner.close();
        }
    }
}

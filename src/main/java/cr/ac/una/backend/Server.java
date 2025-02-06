package cr.ac.una.backend;

import cr.ac.una.backend.logic.Service;
import cr.ac.una.proxy.Protocol;
import cr.ac.una.proxy.objects.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    private ServerSocket srv;
    private final List<Worker> workers = Collections.synchronizedList(new ArrayList<>());

    public Server() {
        try {
            srv = new ServerSocket(Protocol.PORT);
            System.out.println("Servidor iniciado...");
        } catch (IOException ex) {
            System.err.println("Error iniciando el servidor: " + ex.getMessage());
        }
    }

    public void run() {
        while (true) {
            Socket skt = null;
            ObjectInputStream in = null;
            ObjectOutputStream out = null;
            try {
                skt = srv.accept();
                in = new ObjectInputStream(skt.getInputStream());
                out = new ObjectOutputStream(skt.getOutputStream());
                System.out.println("Conexión establecida...");

                // Se realiza el proceso de login; se espera que el cliente envíe primero LOGIN y sus credenciales.
                User user = this.login(in, out);

                // Se crea un Worker para atender las peticiones de este cliente.
                Worker worker = new Worker(this, in, out, user, Service.instance());
                workers.add(worker);
                worker.start();
            }
            catch (IOException | ClassNotFoundException ex) {
                System.err.println("Error en la conexión: " + ex.getMessage());
            }
            catch (Exception ex) {
                try {
                    if (out != null) {
                        out.writeInt(Protocol.ERROR_LOGIN);
                        out.flush();
                    }
                    if (skt != null) {
                        skt.close();
                    }
                } catch (IOException ex1) {
                    System.err.println("Error. Cerrando socket: " + ex1.getMessage());
                }
                System.out.println("Conexión cerrada... Error: " + ex.getMessage());
            }
        }
    }

    private User login(ObjectInputStream in, ObjectOutputStream out) throws Exception {
        int method = in.readInt();
        if (method != Protocol.LOGIN) {
            throw new Exception("Debe autenticarse primero.");
        }
        User user = (User) in.readObject();
        if (!validateUser(user)) {
            throw new Exception("Autenticación fallida");
        }
        out.writeInt(Protocol.ERROR_NO_ERROR);
        out.writeObject(user);
        out.flush();

        return user;
    }

    // Método para enviar una actualización (por ejemplo, nueva información de inventario) a todos los clientes.
    public void broadcastOrderUpdate(Object updateInfo) {
        synchronized(workers) {
            for (Worker wk : workers) {
                wk.deliverUpdate(updateInfo);
            }
        }
    }

    public void remove(User u) {
        synchronized(workers) {
            workers.removeIf(wk -> wk.getUser().equals(u));
        }
        System.out.println("Conexiones activas: " + workers.size());
    }

    private boolean validateUser(User user) {
        for (User u : Service.instance().getUsuarios()) {
            if (u.getIdentificacion().equals(user.getIdentificacion()) && u.getContrasena().equals(user.getContrasena())) {
                return true;
            }
        }
        return false;
    }
}

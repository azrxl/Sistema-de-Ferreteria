package cr.ac.una.backend;

import cr.ac.una.backend.logic.Service;
import cr.ac.una.proxy.IService;
import cr.ac.una.proxy.Protocol;
import cr.ac.una.proxy.objects.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    ServerSocket srv;
    final List<Worker> workers =  Collections.synchronizedList(new ArrayList<>());
    private HashMap<String, Integer> failedAttempts;
    private Set<String> activeUsers;

    public Server() {
        try {
            srv = new ServerSocket(Protocol.PORT);
            failedAttempts = new HashMap<>();
            activeUsers = new HashSet<>();
            Service.instance().cargar();
            System.out.println("Servidor iniciado...");
        } catch (Exception ignored) {
        }
    }

    public void run(){
        IService service = Service.instance();

        ObjectInputStream in;
        ObjectOutputStream out=null;
        Socket skt=null;
        while (true) {
            try {
                skt = srv.accept();
                in = new ObjectInputStream(skt.getInputStream());
                out = new ObjectOutputStream(skt.getOutputStream() );
                System.out.println("Conexion Establecida...");
                User user=this.login(in,out);
                Worker worker = new Worker(this,in,out,user, service);
                workers.add(worker);
                worker.start();
            }
            catch (IOException | ClassNotFoundException ignored) {}
            catch (Exception ex) {
                try {
                    out.writeInt(Protocol.ERROR_LOGIN);
                    out.flush();
                    skt.close();
                } catch (IOException ignored) {}
                System.out.println("Conexion cerrada...  Error: " + ex.getMessage() );
            }
        }
    }

    private User login(ObjectInputStream in, ObjectOutputStream out) throws Exception {
        int method = in.readInt();
        if (method != Protocol.LOGIN) {
            throw new Exception("Should login first");
        }

        User user = (User) in.readObject();
        String userKey = user.getIdentificacion();

        if (activeUsers.contains(userKey)) {
            out.writeInt(Protocol.ERROR_ALREADY_LOGGED_IN);
            out.flush();
            throw new Exception("El usuario ya tiene una sesión activa");
        }

        if (failedAttempts.getOrDefault(userKey, 0) >= 3) {
            out.writeInt(Protocol.ERROR_BLOCKED);
            out.flush();
            throw new Exception("Usuario bloqueado debido a multiples fallos al iniciar sesion");
        }

        if (!validateUser(user)) {
            failedAttempts.put(userKey, failedAttempts.getOrDefault(userKey, 0) + 1);
            if (failedAttempts.get(userKey) >= 3) {
                out.writeInt(Protocol.ERROR_BLOCKED);
                out.flush();
                throw new Exception("Usuario bloqueado por 3 fallos");
            } else {
                throw new Exception("Usuario o contrasena incorrecta");
            }
        }

        failedAttempts.remove(userKey);

        out.writeInt(Protocol.ERROR_NO_ERROR);
        out.writeObject(user);
        out.flush();
        activeUsers.add(userKey);

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
        activeUsers.remove(u.getIdentificacion());
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

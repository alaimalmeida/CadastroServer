/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

import model.Usuarios;
import controller.UsuariosJpaController;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Alaim
 */

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final EntityManagerFactory emf;
    //private final UsuariosJpaController usuariosController;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        emf = Persistence.createEntityManagerFactory("CadastroServerPU");
        //usuariosController = new UsuariosJpaController(emf);
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String login = in.readLine();
            String senha = in.readLine();

            if (validateCredentials(login, senha)) {
                out.println("Login bem-sucedido");
                String command;
                while ((command = in.readLine()) != null) {
                    if (command.equals("L")) {
                        String products = getProducts();
                        out.println(products);
                    } else {
                        out.println("Comando invalido");
                    }
                }
            } else {
                try (clientSocket) {
                    out.println("Credenciais invalidas");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            emf.close();
        }
    }

    //private boolean validateCredentials(String login, String senha) {
    //    Usuarios usuario = usuariosController.findUsuario(login, senha);
    //    return usuario != null;
    //}

    private String getProducts() {
        // Implemente a lógica para buscar produtos do banco de dados.
        // Isso pode ser feito utilizando um controlador similar ao UsuarioJpaController para a entidade Produto.
        return "Lista de produtos"; // Ajustar conforme a necessidade
    }

    private boolean validateCredentials(String login, String senha) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;


import model.Usuarios;
import controller.ProdutosJpaController;
import controller.UsuariosJpaController;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import model.Produtos;

/**
 *
 * @author Alaim
 */

public class CadastroThread extends Thread {
    private ProdutosJpaController ctrl;
    private UsuariosJpaController ctrlUsu;
    private Socket s1;

    public CadastroThread(ProdutosJpaController ctrl, UsuariosJpaController ctrlUsu, Socket s1) {
        this.ctrl = ctrl;
        this.ctrlUsu = ctrlUsu;
        this.s1 = s1;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(s1.getInputStream())) {

            String login = (String) in.readObject();
            String senha = (String) in.readObject();

            Usuarios usuario = ctrlUsu.findUsuario(login, senha);

            if (usuario == null) {
                out.writeObject("Login falhou");
                s1.close();
                return;
            } else {
                out.writeObject("Usuario conectado com sucesso");
            }

            while (true) {
                String comando = (String) in.readObject();

                if ("L".equals(comando)) {
                    List<Produtos> produtos = ctrl.findProdutoEntities();
                    out.writeObject(produtos);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
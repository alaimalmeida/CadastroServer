/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroclient;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Alaim
 */
public class CadastroClientV2 {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 4321);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {

            // Enviar login e senha
            System.out.println("Insira o Nome e Senha do usuario:");
            System.out.print("Nome: ");
            String login = reader.readLine();
            System.out.print("Senha: ");
            String senha = reader.readLine();
            out.writeObject(login);
            out.writeObject(senha);
            out.flush();

            // Ler a resposta de login do servidor
            String loginResponse = (String) in.readObject();
            System.out.println("Resposta do servidor: " + loginResponse);

            if (!"Login bem-sucedido".equals(loginResponse)) {
                System.out.println("Falha no login: " + loginResponse);
                return;
            }

            // Iniciar a thread para leitura assíncrona
            SaidaFrame frame = new SaidaFrame();
            ThreadClient threadClient = new ThreadClient(in, frame.texto);
            threadClient.start();

            // Loop para interagir com o servidor
            while (true) {
                System.out.println("Menu:");
                System.out.println("L – Listar");
                System.out.println("E – Entrada");
                System.out.println("S – Saída");
                System.out.println("X – Finalizar");
                System.out.print("Escolha uma opção: ");
                String command = reader.readLine();
                out.writeObject(command);
                out.flush();

                if ("X".equals(command)) {
                    break;
                } else if ("L".equals(command)) {
                    // Apenas envia o comando
                } else if ("E".equals(command) || "S".equals(command)) {
                    System.out.print("Id da pessoa: ");
                    Long pessoaId = Long.valueOf(reader.readLine());
                    out.writeObject(pessoaId);
                    out.flush();

                    System.out.print("Id do produto: ");
                    Long produtoId = Long.valueOf(reader.readLine());
                    out.writeObject(produtoId);
                    out.flush();

                    System.out.print("Quantidade: ");
                    int quantidade = Integer.parseInt(reader.readLine());
                    out.writeObject(quantidade);
                    out.flush();

                    System.out.print("Valor unitário: ");
                    double valorUnitario = Double.parseDouble(reader.readLine());
                    out.writeObject(valorUnitario);
                    out.flush();
                } else {
                    System.out.println("Comando inválido.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
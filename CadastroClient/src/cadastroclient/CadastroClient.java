/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroclient;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import model.Produtos;

/**
 *
 * @author Alaim
 */


public class CadastroClient {
    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost";
        final int PORT = 4321;

        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            while (true) {
                // Solicitando nome e senha do usuário
                System.out.println("Insira o Nome e Senha do usuario:");
                System.out.print("Nome: ");
                String nome = userInput.readLine();
                System.out.print("Senha: ");
                String senha = userInput.readLine();

                // Enviando o nome e senha do usuário para o servidor
                out.writeObject(nome);
                out.writeObject(senha);

                // Recebendo resposta do servidor
                String response = (String) in.readObject();
                System.out.println(response);

                if (response.equals("Usuario conectado com sucesso")) {
                    break;
                }
            }

            String input;
            do {
                System.out.println("Digite 'L' para listar os produtos:");
                input = userInput.readLine();

                if (input.equalsIgnoreCase("L")) {
                    out.writeObject("L");
                    break;
                } else {
                    System.out.println("Comando invalido.");
                }
            } while (true);

            // Recebendo e exibindo a lista de produtos
            Object response = in.readObject();
            if (response instanceof List) {
                List<Produtos> produtos = (List<Produtos>) response;
                for (Produtos produto : produtos) {
                    System.out.println(produto);
                }
            } else {
                System.out.println("Erro ao receber a lista de produtos.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroclient;

/**
 *
 * @author Alaim
 */

import model.Produtos;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.util.List;


public class ThreadClient extends Thread {
    private final ObjectInputStream entrada;
    private final JTextArea textArea;

    public ThreadClient(ObjectInputStream entrada, JTextArea textArea) {
        this.entrada = entrada;
        this.textArea = textArea;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object response = entrada.readObject();
                if (response instanceof String) {
                    SwingUtilities.invokeLater(() -> textArea.append((String) response + "\n"));
                } else if (response instanceof List) {
                    List<?> produtos = (List<?>) response;
                    SwingUtilities.invokeLater(() -> {
                        for (Object obj : produtos) {
                            if (obj instanceof Produtos) {
                                Produtos produto = (Produtos) obj;
                                textArea.append("Produto: " + produto.getNome() + " - Quantidade: " + produto.getQuantidade() + "\n");
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
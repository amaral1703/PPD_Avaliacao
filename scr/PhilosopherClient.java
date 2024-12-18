// Updated client implementation with automatic actions and logging

package client;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;

public class PhilosopherClient {
    //
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private int id;
    private final String nome;

    public PhilosopherClient(String name) {
        this.nome = name;
    }

    public void start() {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            // Register philosopher
            out.println("REGISTER " + nome);
            String response = in.readLine();
            if (response.startsWith("OK")) {
                id = Integer.parseInt(response.split(" ")[1]);
                System.out.println("Conectado como filósofo ID: " + id);
                System.out.println(nome);
            } else {
                System.out.println("Registration failed: " + response);
                return;
            }

            // ciclo de vida do filósofo em que ele pensa, e se o garfo estiver disponível, ele come
            Random random = new Random();
            while (true) {
                think(out, in, random);
                if (requestFork(out, in)) {
                    eat(out, in, random);
                    releaseFork(out, in);
                }
            }

        } catch (IOException e) {
            System.out.println("Erro ao iniciar o socket: " + e.getMessage());
        }
    }
    //método que simula o pensamento do filósofo
    private void think(PrintWriter out, BufferedReader in, Random random) throws IOException {
        //tempo de pensamento aleatório como solicitado pela Avaliação, é uma distribuição gaussiana com média de 5000ms e desvio padrão de 2000ms
        int thinkTime = (int) Math.abs(random.nextGaussian() * 2000 + 5000);
        System.out.println("Filósofo " + nome + " pensando por " + thinkTime + "ms");
        
        out.println("THINK " + id);
        in.readLine(); // Log response

        try {
            Thread.sleep(thinkTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Erro ao dormir, thread interrompida: " + e.getMessage());
        }
    }
    //método que simula a solicitação de garfos pelo filósofo
    private boolean requestFork(PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("Filósofo " + nome + " solicitando garfos...");
        //logica para os garfos de acordo com o id do filósofo
        int left = id - 1;
        int right = id % 5; 

        out.println("REQUEST_FORK " + left + "," + right);
        String response = in.readLine();
        if ("GRANTED_FORK".equals(response)) {
            System.out.println("Filósofo " + nome + " pegou os garfos.");
            return true;
        } else {
            System.out.println("Filósofo " + nome + " foi negado os garfos.");
            return false;
        }
    }

    private void eat(PrintWriter out, BufferedReader in, Random random) throws IOException {
        int eatTime = 2000; 
        System.out.println("Filósofo " + nome + " comendo por " + eatTime + "ms");

        out.println("EAT " + id);
        in.readLine();

        try {
            Thread.sleep(eatTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void releaseFork(PrintWriter out, BufferedReader in) throws IOException {
        System.out.println("Filósofo " + nome + " liberando os garfos...");
        //libera os garfos com a mesma logica para pegar os garfos 
        int left = id - 1;
        int right = id % 5; 

        out.println("RELEASE_FORK " + left + "," + right);
        String response = in.readLine();
        if ("FORK_RELEASED".equals(response)) {
            System.out.println("Filósofo " + nome + " liberou os garfos.");
        }
    }
    //o main serve para criar um filosofo e direcionar o id dele
    public static void main(String[] args) {
        //le o nome do filósofo direto do terminal do vs code
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o nome do filósofo:");
        String nome = scanner.nextLine();

        PhilosopherClient client = new PhilosopherClient(nome);
        client.start();
    }
}

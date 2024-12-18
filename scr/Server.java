// Updated server implementation with real-time logging of philosopher statistics

package server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {

    private static final int PORT = 12345;
    private static final Map<Integer, Philosopher> philosophers = new ConcurrentHashMap<>();
    private static final Fork[] forks;

    static {
        //iniciliza os 5 garfos
        forks = new Fork[5];
        for (int i = 0; i < 5; i++) {
            forks[i] = new Fork(i);
        }
    }

    public static void main(String[] args) {
        //inicia o servidor
        System.out.println("Servidor iniciado na porta " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ClientHandler(socket)).start();
            }
        } catch (IOException e) {
            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        // cria a classe ClientHandler que implementa Runnable usada para lidar com as requisições dos clientes (filosofos)
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            //sobrescreve o método run() da interface Runnable (fazendo a leitura e escrita de dados da socket, permitindo a comunicação entre o filósofo e o servidor)
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String input;
                while ((input = in.readLine()) != null) {
                    //lê a requisição do cliente
                    String[] parts = input.split(" ", 2);
                    String command = parts[0];
                    String data = parts.length > 1 ? parts[1] : "";
                    //divide a requisição em comando e dados Registrando cada interação do filósofo.
                    switch (command) {
                        //checar os comandos de forma apropriada futuramente
                        case "REGISTER":
                            int id = registerPhilosopher(data);
                            out.println("OK " + id);
                            break;
                        case "REQUEST_FORK":
                            out.println(requestFork(data));
                            break;
                        case "RELEASE_FORK":
                            out.println(releaseFork(data));
                            break;
                        case "THINK":
                            logThink(data);
                            out.println("THINK_LOGGED");
                            break;
                        case "EAT":
                            logEat(data);
                            out.println("EAT_LOGGED");
                            break;
                        default:
                            out.println("Comando desconhecido: " + command);
                            break;
                    }
                }

            } catch (IOException e) {
                System.out.println("Erro ao processar requisição: " + e.getMessage());
            }
        }

        private synchronized int registerPhilosopher(String name) {
            // registra o filosofo a partir da ID
            int id = philosophers.size() + 1;
            philosophers.put(id, new Philosopher(id, name));
            System.out.println("filosofo " + id + " (name = " + name + ") Registrado.");
            return id;
        }

        private synchronized String requestFork(String data) {
            String[] ids = data.split(",");
            int left = Integer.parseInt(ids[0]);
            int right = Integer.parseInt(ids[1]);

            if (forks[left].isAvailable() && forks[right].isAvailable()) {
                //se os garfos estiverem disponíveis, o filósofo pega os garfos
                forks[left].pickUp();
                forks[right].pickUp();
                return "GRANTED_FORK";
            } else {
                return "DENIED_FORK";
            }
        }

        private synchronized String releaseFork(String data) {
            //libera os garfos para o uso de outros filósofos
            String[] ids = data.split(",");
            int left = Integer.parseInt(ids[0]);
            int right = Integer.parseInt(ids[1]);

            forks[left].putDown();
            forks[right].putDown();
            return "FORK_RELEASED";
        }

        //registra (log) o pensamento do filósofo, incrementando o contador de pensamentos
        private void logThink(String data) {
            int id = Integer.parseInt(data);
            Philosopher philosopher = philosophers.get(id);
            philosopher.incrementThoughts();
            System.out.println("filosofo " + id + " (name = " + philosopher.getName() + ") Pensando. Vezes que pensou: " + philosopher.getThoughts());
        }

        //registra (log) que o filosfo Comeu, incrementando o contador de refeições
        private void logEat(String data) {
            int id = Integer.parseInt(data);
            Philosopher philosopher = philosophers.get(id);
            philosopher.incrementMeals();
            System.out.println("filosofo " + id + " (name = " + philosopher.getName() + ") Comendo. Vezes que comeu: " + philosopher.getMeals());
        }
    }
    //classe Fork que representa os garfos, contendo apenas o Id e se o garfo esta ou nao disponivel
    private static class Fork {
        private final int id;
        private boolean available = true;

        public Fork(int id) {
            this.id = id;
        }

        public synchronized boolean isAvailable() {
            return available;
        }

        public synchronized void pickUp() {
            available = false;
        }

        public synchronized void putDown() {
            available = true;
        }
    }
    //classe Philosopher que representa o filosofo, contendo o Id, nome, numero de pensamentos e numero de refeições
    private static class Philosopher {
        private final int id;
        private final String name;
        private int thoughts;
        private int meals;

        public Philosopher(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void incrementThoughts() {
            thoughts++;
        }

        public void incrementMeals() {
            meals++;
        }

        public int getThoughts() {
            return thoughts;
        }

        public int getMeals() {
            return meals;
        }
    }
}

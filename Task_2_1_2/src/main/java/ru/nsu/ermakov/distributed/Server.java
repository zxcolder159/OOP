package ru.nsu.ermakov.distributed;

import ru.nsu.ermakov.distributed.protocol.TaskPacket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import static java.util.Arrays.stream;

public class Server {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Невалидное число аргументов, используйте <port> <clientCount> <path>");
            return;
        }
        int port = Integer.valueOf(args[0]);
        if(port < 1024 || port > 65535) {
            System.out.println("Номер порта должен быть в диапозоне: 1024–65535");
            return;
        }
        int clientCount = Integer.valueOf(args[1]);
        String strPath = args[2];
        try {
            long[] numbers = Files.readAllLines(java.nio.file.Path.of(strPath))
                    .stream()
                    .flatMapToLong(line -> stream(line.split("\\s+"))
                            .filter(s -> !s.isEmpty())
                            .mapToLong(Long::parseLong))
                    .toArray();
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Сервер запущен по порту:" + port);
                Socket[] clientSockets = new Socket[clientCount];
                for (int i = 0; i < clientCount; i++) {
                    clientSockets[i] = serverSocket.accept();
                    System.out.println("Клиент " + (i + 1) + " подключился");
                }
                TaskPacket[] packets = splitToPackets(numbers, clientCount);
                ClientHandler[] handlers = new ClientHandler[clientCount];
                var executor = Executors.newFixedThreadPool(clientCount + 5);
                ExecutorCompletionService<Boolean> completion = new ExecutorCompletionService<>(executor);
                Map<Future<Boolean>, ClientHandler> futureToHandler = new HashMap<>();

                for (int i = 0; i < clientCount; i++) {
                    handlers[i] = new ClientHandler(clientSockets[i], packets[i]);
                    Future<Boolean> f = completion.submit(handlers[i]);
                    futureToHandler.put(f, handlers[i]);
                }

                boolean allPrime = true;
                int successful = 0;
                int totalTasks = packets.length;

                try {
                    while (successful < totalTasks) {
                        Future<Boolean> completedFuture = completion.take();
                        ClientHandler handler = futureToHandler.remove(completedFuture);

                        Boolean clientAllPrime = null;
                        try {
                            clientAllPrime = completedFuture.get();
                        } catch (Exception e) {
                            clientAllPrime = null;
                        }

                        if (clientAllPrime == null) {
                            System.out.println("Сбой у клиента. Переназначаем задачу " + handler.getTaskPacket().getTaskId() + "...");
                            serverSocket.setSoTimeout(0);
                            System.out.println("Ожидание нового клиента на замену упавшему...");
                            Socket newClientSocket = serverSocket.accept();
                            System.out.println("Новый клиент подключился. Отправляем отложенную задачу.");
                            
                            ClientHandler newHandler = new ClientHandler(newClientSocket, handler.getTaskPacket());
                            Future<Boolean> newFuture = completion.submit(newHandler);
                            futureToHandler.put(newFuture, newHandler);
                        } else {
                            successful++;
                            if (!clientAllPrime) {
                                allPrime = false;
                                break;
                            }
                        }
                    }
                } finally {
                    if (!allPrime) {
                        for (Future<Boolean> future : futureToHandler.keySet()) {
                            future.cancel(true);
                        }
                        for (ClientHandler handler : handlers) {
                            if (handler != null) {
                                handler.closeSilently();
                            }
                        }
                        executor.shutdownNow();
                    } else {
                        executor.shutdown();
                    }
                }
                System.out.println("Результат проверки: " + (allPrime ? "Все числа простые" : "Есть составные числа"));
            } catch (IOException e) {
                System.out.println("Ошибка при чтении файла: " + e.getMessage());
                throw new RuntimeException(e);
            }


        } catch (Exception e) {
            System.out.println("Ошибка при запуске сервера: " + e.getMessage());
            e.printStackTrace();
        }

    }
    private static TaskPacket[] splitToPackets(long[] numbers, int parts) {
        TaskPacket[] packets = new TaskPacket[parts];
        int base = numbers.length / parts;
        int rem = numbers.length % parts;

        int offset = 0;
        for (int i = 0; i < parts; i++) {
            int size = base + (i < rem ? 1 : 0);
            long[] chunk = new long[size];
            System.arraycopy(numbers, offset, chunk, 0, size);
            offset += size;
            packets[i] = new TaskPacket(i, chunk);
        }
        return packets;
    }
}

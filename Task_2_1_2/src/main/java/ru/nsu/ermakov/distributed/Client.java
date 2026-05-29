package ru.nsu.ermakov.distributed;

import ru.nsu.ermakov.checkers.ParallelChecker;
import ru.nsu.ermakov.distributed.protocol.ResultPacket;
import ru.nsu.ermakov.distributed.protocol.TaskPacket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {


    public static void main(String[] args) {
        System.out.println("Попытка подключиться к серверу");
        try (Socket socket = new Socket(args[0], Integer.valueOf(args[1]))) {
            socket.setSoTimeout(30000);
            var oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            var ois = new ObjectInputStream(socket.getInputStream());

            System.out.println("Подключение к серверу успешно");
            var taskPacket = (TaskPacket) ois.readObject();
            System.out.println("Задача получена от сервера, количество чисел для проверки:" +
                    taskPacket.getNumbers().length);
            boolean allPrime = new ParallelChecker().runTest(taskPacket.getNumbers());
            var resultPacket = new ResultPacket(taskPacket.getTaskId(), allPrime);
            oos.writeObject(resultPacket);
            oos.flush();
            System.out.println("Результат отправлен на сервер");

        } catch (Exception e) {
            System.err.println("Ошибка при подключении к серверу: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

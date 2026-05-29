package ru.nsu.ermakov.distributed;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.nsu.ermakov.distributed.protocol.ResultPacket;
import ru.nsu.ermakov.distributed.protocol.TaskPacket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;

@RequiredArgsConstructor
public class ClientHandler implements Callable<Boolean> {
    private final Socket clientSocket;
    @Getter
    private final TaskPacket taskPacket;
    private String clientAddress;
    @Getter
    private ResultPacket resultPacket;
    @Override
    public Boolean call() {
        clientAddress = clientSocket
                .getRemoteSocketAddress().toString();
        try {
            clientSocket.setSoTimeout(30000);
            System.out.println("Клиент подключился: " + clientAddress);

            var oos = new ObjectOutputStream(clientSocket.getOutputStream());
            oos.writeObject(taskPacket);
            oos.flush();
            System.out.println("Задача отправлена клиенту: " + clientAddress);
            var ois = new ObjectInputStream(clientSocket.getInputStream());
            this.resultPacket = (ResultPacket) ois.readObject();
            System.out.println("Результат получен от клиента: " + clientAddress);
            return resultPacket.isAllPrime();
        } catch (Exception e) {
            System.err.println("Ошибка при работе с клиентом " + clientAddress + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            closeSilently();
        }
    }

    public void closeSilently() {
        try {
            clientSocket.close();
            String address = clientAddress != null
                    ? clientAddress
                    : String.valueOf(clientSocket.getRemoteSocketAddress());
            System.out.println("Клиент отключился: " + address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

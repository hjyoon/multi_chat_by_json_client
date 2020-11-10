package multi_chat_by_json_client;

import java.io.*;
import java.net.*;

import com.google.gson.*;

public class MyReceiveThread extends Thread {
    private Socket clientSocket;
    private ObjectInputStream ois;
    private Gson gson;

    MyReceiveThread(Socket clientSocket, ObjectInputStream ois) {
        this.clientSocket = clientSocket;
        this.ois = ois;
    }

    public void init() {
        gson = new Gson();
    }

    @Override
    public void run() {
        try {
            while(true) {
                if(!clientSocket.isConnected() && clientSocket.isClosed()) {
                    break;
                }

                String receivedData = (String)ois.readObject();
                if(receivedData == null) {
                    break;
                }

                // 받은 데이터 파싱
                Data data = gson.fromJson(receivedData, Data.class);
                if(data.getOp().equals("sendMsg")) {
                    System.out.println(data.getData()); // 서버에서 받은 데이터를 출력
                }

            }
            clientSocket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
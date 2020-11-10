package multi_chat_by_json_client;

import java.io.*;
import java.net.*;

import com.google.gson.*;

public class MySendThread extends Thread {
    private Socket clientSocket;
    private BufferedReader br;  // ����ڷ� ���� �����͸� �Է¹ޱ� ����
    private ObjectOutputStream oos;
    private String nickname;
    private Gson gson;

    MySendThread(Socket clientSocket, BufferedReader br, ObjectOutputStream oos, String nickname) {
        this.clientSocket = clientSocket;
        this.br = br;
        this.oos = oos;
        this.nickname = nickname;
    }

    public void init() {
        gson = new Gson();
        Data data = new Data("setNickname", nickname);
        String json = gson.toJson(data);
        try {
            oos.writeObject(json);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                if(!clientSocket.isConnected() && clientSocket.isClosed()) {
                    break;
                }

                System.out.print("You> ");

                String inputData = br.readLine();
                if(inputData == null) {
                    break;
                }

                Data data = new Data("sendMsg", inputData);
                String json = gson.toJson(data);
                oos.writeObject(json);  // ���� ������ �о�ͼ� ����
            }

            clientSocket.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
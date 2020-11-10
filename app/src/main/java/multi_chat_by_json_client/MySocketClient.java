package multi_chat_by_json_client;

import java.io.*;
import java.net.*;

public class MySocketClient {
    private Socket clientSocket;            // 클라이언트 소켓
    private BufferedReader br_from_user;    // 사용자로 부터 데이터를 입력받기 위함
    private ObjectInputStream ois;
    private ObjectOutputStream oos;


    private String ip;
    private int port;
    private String nickname;

    public MySocketClient(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void init() {
        br_from_user = new BufferedReader(new InputStreamReader(System.in));

        try {
            System.out.print("nickname : ");
            nickname = br_from_user.readLine(); // 닉네임 설정
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            System.out.println("Trying to connect...");
            clientSocket = new Socket(ip, port);
            //clientSocket = new Socket();
            //clientSocket.connect(new InetSocketAddress(ip, port), 5);
            //clientSocket.setSoTimeout(1000);
        } 
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            oos = new ObjectOutputStream(clientSocket.getOutputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ois = new ObjectInputStream(clientSocket.getInputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        MySendThread send_thread = new MySendThread(clientSocket, br_from_user, oos, nickname);
        MyReceiveThread rec_thread = new MyReceiveThread(clientSocket, ois);
        send_thread.init();
        send_thread.start();
        rec_thread.init();
        rec_thread.start();
    }
}
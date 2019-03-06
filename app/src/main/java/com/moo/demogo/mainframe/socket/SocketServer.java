package com.moo.demogo.mainframe.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public class SocketServer {
    public static final int PORT = 9898;
    private List<Socket> clients = new ArrayList<>();
    private ServerSocket serverSocket;
    private ThreadPoolExecutor executor;

    public static void main(String[] args) {
        new SocketServer().startServer();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("服务器启动");
            while (true) {
                Socket accept = serverSocket.accept();
                clients.add(accept);
                System.out.println(accept.getInetAddress());
                new Thread(new ServerRunnable(accept)).start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ServerRunnable implements Runnable {
        private Socket socket;
        private BufferedReader bufferedReader;

        public ServerRunnable(Socket socket) {
            this.socket = socket;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(socket.getInetAddress() + "  建立连接");
            sendMsg("建立连接");
        }

        @Override
        public void run() {
            try {
                while (true) {
                    System.out.println("开始读取信息");
                    String str = bufferedReader.readLine();
                    System.out.println(str + "");
                    if (str != null) {
                        System.out.println(str);
                        if (str.equals("own exit")) {
                            clients.remove(socket);
                            bufferedReader.close();
                            socket.close();
                        } else {
                            sendMsg(str);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void sendMsg(String msg) {
            for (int i = 0; i < clients.size(); i++) {
                Socket socket = clients.get(i);
                try {
                    PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                    msg = socket.getInetAddress() + "|" + msg;
                    printWriter.println(msg);
                    printWriter.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

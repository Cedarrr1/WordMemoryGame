package server;

import tool.VocabularyManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final int PORT = 12345;
    private VocabularyManager vocabularyManager;
    private Map<String, String> userCredentials;
    private ServerSocket serverSocket;

    public Server() {
        vocabularyManager = new VocabularyManager();
        vocabularyManager.initializeDatabase();
        vocabularyManager.loadWordsFromJson("Words.json"); // 词汇表文件名

        loadUserCredentials(); // 加载用户凭证
    }

    private void loadUserCredentials() {
        userCredentials = new HashMap<>();
        // 这里可以加载用户凭证文件 users.txt
        // 每行格式为：username,password

        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    userCredentials.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (serverSocket != null && !serverSocket.isClosed()) {
                        serverSocket.close();
                        System.out.println("Server socket closed.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                String request;
                while ((request = in.readLine()) != null) {
                    if ("LOGIN_REQUEST".equals(request)) {
                        String username = in.readLine();
                        String password = in.readLine();
                        if (authenticate(username, password)) {
                            out.println("LOGIN_SUCCESS");
                            handleGameSession();
                        } else {
                            out.println("LOGIN_FAILURE");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeResources();
            }
        }

        private boolean authenticate(String username, String password) {
            return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
        }

        private void handleGameSession() throws IOException {
            // 游戏逻辑将在后续添加
            out.println("GAME_STARTED");
            while (true) {
                String command = in.readLine();
                if ("QUIT".equals(command)) {
                    break;
                }
            }
            out.println("GAME_OVER");
        }

        private void closeResources() {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (clientSocket != null) clientSocket.close();
                System.out.println("Client resources closed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new Server().start();
    }
}











//package server;
//
//import java.io.*;
//import java.net.*;
//import java.util.List;
//import java.util.Map;
//
//public class Server {
//    private static final int PORT = 12345;
//    private static final String FILE_PATH = "Words.json";
//    private VocabularyManager vocabularyManager;
//
//    public Server() {
//        vocabularyManager = new VocabularyManager();
//        vocabularyManager.initializeDatabase();
//        vocabularyManager.loadWordsFromJson(FILE_PATH);
//    }
//
//    public void start() throws IOException {
//        ServerSocket serverSocket = null;
//        try {
//            serverSocket = new ServerSocket(PORT);
//            System.out.println("服务器启动，等待客户端连接...");
//
//            // 等待客户端连接
//            Socket clientSocket = serverSocket.accept();
//            System.out.println("客户端连接成功: " + clientSocket.getInetAddress());
//
//            // 获取输入流和输出流
//            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//
//            // 读取客户端发送的消息
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
//                System.out.println("收到客户端消息: " + inputLine);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (serverSocket != null) {
//                try {
//                    serverSocket.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) throws IOException {
//        new Server().start();
//        System.out.println("服务器启动");
//    }
//}




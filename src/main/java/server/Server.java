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
        vocabularyManager.loadWordsFromJson("Words.json"); // 加载词汇表至数据库
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

    /**
     * 启动服务器，监听指定端口并处理客户端连接
     */
    public void start() {
        try {
            // 创建ServerSocket对象，监听指定端口
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            //确保界面关闭时 对应clientHandler管理的socket正常关闭
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

            //主线程等待客户端连接
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");
                // 为每个客户端连接创建一个新线程，并启动
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 客户端处理器，负责处理单个客户端的连接和通信
     */
    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        /**
         * 构造函数，初始化客户端处理器
         *
         * @param clientSocket 客户端套接字
         */
        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 线程执行方法，处理客户端请求
         */
        @Override
        public void run() {
            try {
                String request;
                // 循环读取客户端发送的请求
                while ((request = in.readLine()) != null) {
                    if ("LOGIN_REQUEST".equals(request)) {
                        // 接收到client发送的login请求
                        //读取用户名和密码
                        String username = in.readLine();
                        String password = in.readLine();
                        if (authenticate(username, password)) {
                            out.println("LOGIN_SUCCESS");
                            // 处理游戏会话
                            handleGameSession();
                        } else {
                            out.println("LOGIN_FAILURE");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 关闭资源
                closeResources();
            }
        }

        /**
         * 验证账号密码
         *
         * @param username 用户名
         * @param password 密码
         * @return 验证结果
         */
        // 验证账号密码
        private boolean authenticate(String username, String password) {
            return userCredentials.containsKey(username) && userCredentials.get(username).equals(password);
        }

        /**
         * 处理游戏会话
         *
         * @throws IOException 异常
         */
        private void handleGameSession() throws IOException {
            out.println("GAME_STARTED");
            while (true) {
                String command = in.readLine();
                if ("QUIT".equals(command)) {
                    break;
                }
            }
            out.println("GAME_OVER");
        }

        /**
         * 关闭资源
         */
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







package server;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Map;

public class Server {
    private static final int PORT = 12345;
    private static final String FILE_PATH = "Words.json";
    private VocabularyManager vocabularyManager;

    public Server() {
        vocabularyManager = new VocabularyManager();
        vocabularyManager.initializeDatabase();
        vocabularyManager.loadWordsFromJson(FILE_PATH);
    }

    public void start() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("服务器启动，等待客户端连接...");

            // 打印前几个单词来测试
            List<Map<String, String>> words = vocabularyManager.getWords();
            for (int i = 0; i < Math.min(words.size(), 5); i++) {
                Map<String, String> word = words.get(i);
                System.out.println("英文单词: " + word.get("enS"));
                System.out.println("词性: " + word.get("pos") + ", 意思: " + word.get("mean"));
            }

            // 等待客户端连接
            Socket clientSocket = serverSocket.accept();
            System.out.println("客户端连接成功: " + clientSocket.getInetAddress());

            // 获取输入流和输出流
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // 读取客户端发送的消息
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("收到客户端消息: " + inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Server().start();
        System.out.println("服务器启动");
    }
}




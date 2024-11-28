package client;

import tool.FileUtil;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client {
    private static final String SERVER_ADDRESS = "localhost"; // 服务器地址
    private static final int PORT = 12345; // 服务器端口号
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client() {
        try {
            socket = new Socket(SERVER_ADDRESS, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("客户端连接成功");
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                closeResources();
                System.out.println("客户端资源已关闭。");
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendLoginRequest(String username, String password, LoginGui loginGui) {
        try {
            // 发送登录请求
            out.println("LOGIN_REQUEST");
            out.println(username);
            out.println(password);

            // 读取服务器响应
            String response = in.readLine();
            if ("LOGIN_SUCCESS".equals(response)) {
                // 登录成功
                SwingUtilities.invokeLater(() -> {
                    loginGui.showSuccessMessage();
                });
            } else {
                // 登录失败
                SwingUtilities.invokeLater(() -> {
                    loginGui.showErrorMessage("用户名或密码错误，请重试。");
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PrintWriter getOut() {
        return out;
    }

    public BufferedReader getIn() {
        return in;
    }

    public static void main(String[] args) {
        new Client().start();
    }

    public void start() {
        try {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                // 读取服务器响应
                String response = in.readLine();
                if (response != null) {
                    System.out.println("服务器: " + response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private void closeResources() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}














//package client;
//
//import java.io.*;
//import java.net.*;
//
//public class Client {
//    private static final String SERVER_ADDRESS = "localhost"; // 服务器地址
//    private static final int PORT = 12345; // 服务器端口号
//    private Socket socket;
//    private PrintWriter out;
//    private BufferedReader in;
//
//    public Client() {
//        try {
//            socket = new Socket(SERVER_ADDRESS, PORT);
//            out = new PrintWriter(socket.getOutputStream(), true);
//            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            System.out.println("客户端连接成功");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void start() {
//        try {
//            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
//            String userInput;
//            while ((userInput = stdIn.readLine()) != null) {
//                out.println(userInput);
//                // 读取服务器响应
//                String response = in.readLine();
//                if (response != null) {
//                    System.out.println("服务器: " + response);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (in != null) in.close();
//                if (out != null) out.close();
//                if (socket != null) socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        new Client().start();
//    }
//}
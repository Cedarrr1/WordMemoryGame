package client.gameMode;

import client.Client;
import client.MainGui;
import tool.VisGui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordsReview extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JButton prevButton;
    private JButton nextButton;
    private JButton quitButton;
    private JButton displayButton;
    private int currentPage = 0;
    private final int ROWS_PER_PAGE = 10;
    private List<String[]> data;

    public WordsReview(Client client) {
        setTitle("单词复习");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // 初始化数据
        loadDataFromFile();

        // 创建表格模型
        model = new DefaultTableModel(new Object[]{"英文", "中文"}, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        prevButton = new JButton("上一页");
        nextButton = new JButton("下一页");
        quitButton = new JButton("退出复习界面");
        displayButton = new JButton("作答可视化");

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 0) {
                    currentPage--;
                    updateTable();
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((currentPage + 1) * ROWS_PER_PAGE < data.size()) {
                    currentPage++;
                    updateTable();
                }
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // 关闭当前窗口
                SwingUtilities.invokeLater(() -> new MainGui(client)); // 返回主界面
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VisGui.display(); // 跳转至展示界面
            }
        });

        // 使用 BoxLayout 布局管理器来水平排列按钮
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(prevButton);
        buttonPanel.add(Box.createHorizontalGlue()); // 添加弹性空间
        buttonPanel.add(nextButton);
        buttonPanel.add(Box.createHorizontalGlue()); // 添加弹性空间
        buttonPanel.add(displayButton);
        buttonPanel.add(Box.createHorizontalGlue()); // 添加弹性空间
        buttonPanel.add(quitButton);

        // 设置布局
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // 初始更新表格
        updateTable();

        // 显示窗口
        setVisible(true);
    }

    private void loadDataFromFile() {
        data = new ArrayList<>();
        try (BufferedReader masteredReader = new BufferedReader(new FileReader("已掌握单词.txt"));
             BufferedReader unmasteredReader = new BufferedReader(new FileReader("未掌握单词.txt"))) {

            String line;
            while ((line = masteredReader.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    data.add(new String[]{parts[0], parts[1]});
                } else {
                    System.err.println("Invalid format in 已掌握单词.txt: " + line);
                }
            }

            while ((line = unmasteredReader.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    data.add(new String[]{parts[0], parts[1]});
                } else {
                    System.err.println("Invalid format in 未掌握单词.txt: " + line);
                }
            }

            System.out.println("Loaded " + data.size() + " words from files.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading word files: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable() {
        model.setRowCount(0); // 清空现有数据
        int start = currentPage * ROWS_PER_PAGE;
        int end = Math.min(start + ROWS_PER_PAGE, data.size());

        for (int i = start; i < end; i++) {
            model.addRow(data.get(i));
        }
    }

    // 主方法用于测试
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Client dummyClient = new Client(); // 假设有一个简单的Client类
            WordsReview review = new WordsReview(dummyClient);
        });
    }
}






//package client.gameMode;
//
//import client.Client;
//import client.MainGui;
//import tool.VisGui;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class WordsReview extends JFrame {
//    private JTable table;
//    private DefaultTableModel model;
//    private JButton prevButton;
//    private JButton nextButton;
//    private JButton quitButton;
//    private JButton displayButton;
//    private int currentPage = 0;
//    private final int ROWS_PER_PAGE = 10;
//    private List<String[]> data;
//
//    public WordsReview(Client client) {
//        setTitle("单词复习");
//        setSize(600, 400);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        // 初始化数据
//        loadDataFromFile();
//
//        // 创建表格模型
//        model = new DefaultTableModel(new Object[]{"英文", "中文"}, 0);
//        table = new JTable(model);
//        JScrollPane scrollPane = new JScrollPane(table);
//
//        // 创建按钮面板
//        JPanel buttonPanel = new JPanel();
//        prevButton = new JButton("上一页");
//        nextButton = new JButton("下一页");
//        quitButton = new JButton("退出复习界面");
//        displayButton = new JButton("作答可视化");
//
//        prevButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (currentPage > 0) {
//                    currentPage--;
//                    updateTable();
//                }
//            }
//        });
//
//        nextButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if ((currentPage + 1) * ROWS_PER_PAGE < data.size()) {
//                    currentPage++;
//                    updateTable();
//                }
//            }
//        });
//
//        quitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                dispose(); // 关闭当前窗口
//                SwingUtilities.invokeLater(() -> new MainGui(client)); // 返回主界面
//            }
//        });
//
//        displayButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                VisGui.display(); // 跳转至展示界面
//            }
//        });
//
//        // 使用 BoxLayout 布局管理器来水平排列按钮
//        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
//        buttonPanel.add(prevButton);
//        buttonPanel.add(Box.createHorizontalGlue()); // 添加弹性空间
//        buttonPanel.add(nextButton);
//        buttonPanel.add(Box.createHorizontalGlue()); // 添加弹性空间
//        buttonPanel.add(displayButton);
//        buttonPanel.add(Box.createHorizontalGlue()); // 添加弹性空间
//        buttonPanel.add(quitButton);
//
//        // 设置布局
//        getContentPane().setLayout(new BorderLayout());
//        getContentPane().add(scrollPane, BorderLayout.CENTER);
//        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
//
//        // 初始更新表格
//        updateTable();
//
//        // 显示窗口
//        setVisible(true);
//    }
//
//    private void loadDataFromFile() {
//        data = new ArrayList<>();
//        try (BufferedReader masteredReader = new BufferedReader(new FileReader("已掌握单词.txt"));
//             BufferedReader unmasteredReader = new BufferedReader(new FileReader("未掌握单词.txt"))) {
//
//            String line;
//            while ((line = masteredReader.readLine()) != null) {
//                String[] parts = line.split(": ");
//                if (parts.length == 2) {
//                    data.add(new String[]{parts[0], parts[1]});
//                } else {
//                    System.err.println("Invalid format in 已掌握单词.txt: " + line);
//                }
//            }
//
//            while ((line = unmasteredReader.readLine()) != null) {
//                String[] parts = line.split(": ");
//                if (parts.length == 2) {
//                    data.add(new String[]{parts[0], parts[1]});
//                } else {
//                    System.err.println("Invalid format in 未掌握单词.txt: " + line);
//                }
//            }
//
//            System.out.println("Loaded " + data.size() + " words from files.");
//        } catch (IOException e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error loading word files: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
//    private void updateTable() {
//        model.setRowCount(0); // 清空现有数据
//        int start = currentPage * ROWS_PER_PAGE;
//        int end = Math.min(start + ROWS_PER_PAGE, data.size());
//
//        for (int i = start; i < end; i++) {
//            model.addRow(data.get(i));
//        }
//    }
//
//    // 主方法用于测试
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            Client dummyClient = new Client(); // 假设有一个简单的Client类
//            WordsReview review = new WordsReview(dummyClient);
//        });
//    }
//}






//package client.gameMode;
//
//import client.Client;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class WordsReview extends JFrame {
//    private JTable table;
//    private DefaultTableModel model;
//    private JButton prevButton;
//    private JButton nextButton;
//    private int currentPage = 0;
//    private final int ROWS_PER_PAGE = 10;
//    private List<String[]> data;
//
//    public WordsReview(Client client) {
//        setTitle("单词复习");
//        setSize(600, 400);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        // 初始化数据
//        loadDataFromFile();
//
//        // 创建表格模型
//        model = new DefaultTableModel(new Object[]{"英文", "中文"}, 0);
//        table = new JTable(model);
//        JScrollPane scrollPane = new JScrollPane(table);
//
//        // 创建按钮面板
//        JPanel buttonPanel = new JPanel();
//        prevButton = new JButton("上一页");
//        nextButton = new JButton("下一页");
//
//        prevButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (currentPage > 0) {
//                    currentPage--;
//                    updateTable();
//                }
//            }
//        });
//
//        nextButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if ((currentPage + 1) * ROWS_PER_PAGE < data.size()) {
//                    currentPage++;
//                    updateTable();
//                }
//            }
//        });
//
//        buttonPanel.add(prevButton);
//        buttonPanel.add(nextButton);
//
//        // 设置布局
//        getContentPane().setLayout(new BorderLayout());
//        getContentPane().add(scrollPane, BorderLayout.CENTER);
//        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
//
//        // 初始更新表格
//        updateTable();
//    }
//
//    private void loadDataFromFile() {
//        data = new ArrayList<>();
//        try (BufferedReader masteredReader = new BufferedReader(new FileReader("已掌握单词.txt"));
//             BufferedReader unmasteredReader = new BufferedReader(new FileReader("未掌握单词.txt"))) {
//
//            String line;
//            while ((line = masteredReader.readLine()) != null) {
//                String[] parts = line.split(": ");
//                if (parts.length == 2) {
//                    data.add(new String[]{parts[0], parts[1]});
//                }
//            }
//
//            while ((line = unmasteredReader.readLine()) != null) {
//                String[] parts = line.split(": ");
//                if (parts.length == 2) {
//                    data.add(new String[]{parts[0], parts[1]});
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void updateTable() {
//        model.setRowCount(0); // 清空现有数据
//        int start = currentPage * ROWS_PER_PAGE;
//        int end = Math.min(start + ROWS_PER_PAGE, data.size());
//
//        for (int i = start; i < end; i++) {
//            model.addRow(data.get(i));
//        }
//    }
//
//    // 主方法用于测试
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            Client dummyClient = new Client(); // 假设有一个简单的Client类
//            WordsReview review = new WordsReview(dummyClient);
//            review.setVisible(true);
//        });
//    }
//}






//package client.gameMode;
//
//import client.Client;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class WordsReview extends JFrame {
//    private JTable table;
//    private DefaultTableModel model;
//    private JButton prevButton;
//    private JButton nextButton;
//    private int currentPage = 0;
//    private final int ROWS_PER_PAGE = 10;
//    private List<String[]> data;
//
//    public WordsReview(Client client) {
//        setTitle("单词复习");
//        setSize(600, 400);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        // 初始化数据
//        loadDataFromFile();
//
//        // 创建表格模型
//        model = new DefaultTableModel(new Object[]{"英文", "中文"}, 0);
//        table = new JTable(model);
//        JScrollPane scrollPane = new JScrollPane(table);
//
//        // 创建按钮面板
//        JPanel buttonPanel = new JPanel();
//        prevButton = new JButton("上一页");
//        nextButton = new JButton("下一页");
//
//        prevButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (currentPage > 0) {
//                    currentPage--;
//                    updateTable();
//                }
//            }
//        });
//
//        nextButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if ((currentPage + 1) * ROWS_PER_PAGE < data.size()) {
//                    currentPage++;
//                    updateTable();
//                }
//            }
//        });
//
//        buttonPanel.add(prevButton);
//        buttonPanel.add(nextButton);
//
//        // 设置布局
//        getContentPane().setLayout(new BorderLayout());
//        getContentPane().add(scrollPane, BorderLayout.CENTER);
//        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
//
//        // 初始更新表格
//        updateTable();
//    }
//
//    private void loadDataFromFile() {
//        data = new ArrayList<>();
//        try (BufferedReader masteredReader = new BufferedReader(new FileReader("已掌握单词.txt"));
//             BufferedReader unmasteredReader = new BufferedReader(new FileReader("未掌握单词.txt"))) {
//
//            String line;
//            while ((line = masteredReader.readLine()) != null) {
//                String[] parts = line.split(": ");
//                if (parts.length == 2) {
//                    data.add(new String[]{parts[0], parts[1]});
//                }
//            }
//
//            while ((line = unmasteredReader.readLine()) != null) {
//                String[] parts = line.split(": ");
//                if (parts.length == 2) {
//                    data.add(new String[]{parts[0], parts[1]});
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void updateTable() {
//        model.setRowCount(0); // 清空现有数据
//        int start = currentPage * ROWS_PER_PAGE;
//        int end = Math.min(start + ROWS_PER_PAGE, data.size());
//
//        for (int i = start; i < end; i++) {
//            model.addRow(data.get(i));
//        }
//    }
//}
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            WordsReview review = new WordsReview();
//            review.setVisible(true);
//        });
//    }
























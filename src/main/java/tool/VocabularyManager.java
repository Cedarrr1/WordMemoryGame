package tool;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VocabularyManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/vocabulary_db";
    private static final String USER = "root";
    private static final String PASS = "wod.1314";

    public void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            Statement stmt = conn.createStatement();
            //创建words表 id列作为主键， ens列存储英文拼写
            // pos列存储词性、mean列存储中文含义
            String sql = "CREATE TABLE IF NOT EXISTS words (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "enS VARCHAR(255) NOT NULL," +
                    "pos VARCHAR(50)," +
                    "mean TEXT)";
            //执行查询
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadWordsFromJson(String filePath) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String jsonContent = readJsonFile(filePath);
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> dataMap = gson.fromJson(jsonContent, type);
            // 读取对象中的data字段
            Map<String, Object> data = (Map<String, Object>) dataMap.get("data");
            if (data != null) {
                for (String key : data.keySet()) {
                    Map<String, Object> wordData = (Map<String, Object>) data.get(key);
                    String enS = (String) wordData.get("enS");
                    List<Map<String, Object>> chSList = (List<Map<String, Object>>) wordData.get("chS");
                    for (Map<String, Object> chS : chSList) {
                        String pos = (String) chS.get("pos");
                        String mean = String.join(", ", (List<String>) chS.get("mean"));
                        insertWord(conn, enS, pos, mean);
                    }
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertWord(Connection conn, String enS, String pos, String mean) throws SQLException {
        String sql = "INSERT INTO words (enS, pos, mean) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, enS);
            pstmt.setString(2, pos);
            pstmt.setString(3, mean);
            pstmt.executeUpdate();
        }
    }

    public List<Map<String, String>> getWords() {
        List<Map<String, String>> words = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "SELECT * FROM words";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Map<String, String> word = new java.util.HashMap<>();
                    word.put("id", Integer.toString(rs.getInt("id")));
                    word.put("enS", rs.getString("enS"));
                    word.put("pos", rs.getString("pos"));
                    word.put("mean", rs.getString("mean"));
                    words.add(word);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }

    private String readJsonFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
}
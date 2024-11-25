package tool;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
    private static final String MASTERED_WORDS_FILE = "已掌握单词.txt";
    private static final String UNMASTERED_WORDS_FILE = "未掌握单词.txt";

    public static void saveMasteredWord(String word, String meaning) {
        saveToFile(MASTERED_WORDS_FILE, word, meaning);
    }

    public static void saveUnmasteredWord(String word, String meaning, String status) {
        saveToFile(UNMASTERED_WORDS_FILE, word, meaning + " (" + status + ")");
    }

    private static void saveToFile(String filePath, String word, String detail) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(word + ": " + detail);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




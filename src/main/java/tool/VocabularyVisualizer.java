package tool;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VocabularyVisualizer {

    public static void main(String[] args) {
        // 获取项目根目录的路径
        String projectRoot = System.getProperty("user.dir");

        // 使用相对路径构建文件路径
        String filePath = Paths.get(projectRoot, "未掌握单词.txt").toString();

        // 从文件中读取数据
        Map<String, Integer> data = readDataFromFile(filePath);

        // 创建柱状图
        CategoryChart chart = new CategoryChartBuilder()
                .width(800)
                .height(600)
                .title("未掌握词汇状态")
                .xAxisTitle("词汇状态")
                .yAxisTitle("词汇数量")
                .build();

        // 设置图表样式
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setToolTipsEnabled(true);

        // 准备数据
        List<String> categories = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        categories.add("作答错误");
        categories.add("未作答");
        values.add(data.getOrDefault("作答错误", 0));
        values.add(data.getOrDefault("未作答", 0));

        // 添加系列数据
        chart.addSeries("词汇数量", categories, values);

        // 显示图表
        new SwingWrapper<>(chart).displayChart();
    }

    private static Map<String, Integer> readDataFromFile(String filePath) {
        Map<String, Integer> result = new HashMap<>();
        result.put("作答错误", 0);
        result.put("未作答", 0);

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("(作答错误)")) {
                    result.merge("作答错误", 1, Integer::sum);
                } else if (line.contains("(未作答)")) {
                    result.merge("未作答", 1, Integer::sum);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}
package report;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pojo.RecipeData;

public class JSONReportGenerator {

    public static void generateReport(List<RecipeData> data) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(data);

        try (FileWriter writer = new FileWriter("report.json")) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("JSON report generated successfully.");
    }
}

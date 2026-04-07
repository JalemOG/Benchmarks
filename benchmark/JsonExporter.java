package benchmark;

import java.io.FileWriter;
import java.io.IOException;

public class JsonExporter {

    public static void exportResults(BenchmarkResult[] results, String filePath) throws IOException {
        StringBuilder json = new StringBuilder();
        json.append("[\n");

        for (int i = 0; i < results.length; i++) {
            BenchmarkResult r = results[i];
            json.append("  {\n");
            json.append("    \"structure\": \"").append(r.getStructureName()).append("\",\n");
            json.append("    \"operation\": \"").append(r.getOperationName()).append("\",\n");
            json.append("    \"dataSize\": ").append(r.getDataSize()).append(",\n");

            json.append("    \"executionTimesNs\": [");
            long[] times = r.getExecutionTimes();
            for (int j = 0; j < times.length; j++) {
                json.append(times[j]);
                if (j < times.length - 1) json.append(", ");
            }
            json.append("],\n");

            json.append("    \"memoryUsagesBytes\": [");
            long[] memory = r.getMemoryUsages();
            for (int j = 0; j < memory.length; j++) {
                json.append(memory[j]);
                if (j < memory.length - 1) json.append(", ");
            }
            json.append("],\n");

            json.append("    \"averageTimeNs\": ").append(r.getAverageTimeNs()).append(",\n");
            json.append("    \"averageMemoryBytes\": ").append(r.getAverageMemoryBytes()).append("\n");
            json.append("  }");

            if (i < results.length - 1) json.append(",");
            json.append("\n");
        }

        json.append("]");

        FileWriter writer = new FileWriter(filePath);
        writer.write(json.toString());
        writer.close();
    }
}
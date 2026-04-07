import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class ChartGenerator {

    private static class DataPoint {
        int dataSize;
        double value;

        DataPoint(int dataSize, double value) {
            this.dataSize = dataSize;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get("charts"));

            // Comparación 1: estructuras lineales
            String[] linearFiles = {
                "array_results.json",
                "singly_results.json",
                "doubly_results.json",
                "double_ended_results.json",
                "circular_results.json"
            };

            String[] linearOperations = {
                "addFirst",
                "addLast",
                "insertMiddle",
                "removeMiddle",
                "search",
                "getByIndex",
                "replace"
            };

            for (String operation : linearOperations) {
                Map<String, List<DataPoint>> timeSeries = new LinkedHashMap<>();
                Map<String, List<DataPoint>> memorySeries = new LinkedHashMap<>();

                for (String file : linearFiles) {
                    String structureName = parseStructureNameFromFile(file);
                    timeSeries.put(structureName, extractSeries(file, operation, "averageTimeNs"));
                    memorySeries.put(structureName, extractSeries(file, operation, "averageMemoryBytes"));
                }

                drawLineChart(
                    timeSeries,
                    "Tiempo vs datos - " + operation,
                    "Cantidad de datos",
                    "Tiempo promedio (ns)",
                    "charts/time_" + operation + ".png"
                );

                drawLineChart(
                    memorySeries,
                    "Memoria vs datos - " + operation,
                    "Cantidad de datos",
                    "Memoria promedio (bytes)",
                    "charts/memory_" + operation + ".png"
                );
            }

            // Comparación 2: escenarios
            Map<String, List<DataPoint>> undoTime = new LinkedHashMap<>();
            undoTime.put("StackArray", extractScenarioSeries("undo_stack_array.json", "averageTimeNs"));
            undoTime.put("QueueArray", extractScenarioSeries("undo_queue_array.json", "averageTimeNs"));
            undoTime.put("StackLinkedList", extractScenarioSeries("undo_stack_list.json", "averageTimeNs"));
            undoTime.put("QueueLinkedList", extractScenarioSeries("undo_queue_list.json", "averageTimeNs"));

            drawLineChart(
                undoTime,
                "Escenario Undo - Tiempo",
                "Cantidad de datos",
                "Tiempo promedio (ns)",
                "charts/undo_time.png"
            );

            Map<String, List<DataPoint>> fifoTime = new LinkedHashMap<>();
            fifoTime.put("QueueArray", extractScenarioSeries("fifo_queue_array.json", "averageTimeNs"));
            fifoTime.put("StackArray", extractScenarioSeries("fifo_stack_array.json", "averageTimeNs"));
            fifoTime.put("QueueLinkedList", extractScenarioSeries("fifo_queue_list.json", "averageTimeNs"));
            fifoTime.put("StackLinkedList", extractScenarioSeries("fifo_stack_list.json", "averageTimeNs"));

            drawLineChart(
                fifoTime,
                "Escenario FIFO - Tiempo",
                "Cantidad de datos",
                "Tiempo promedio (ns)",
                "charts/fifo_time.png"
            );

            Map<String, List<DataPoint>> undoMemory = new LinkedHashMap<>();
            undoMemory.put("StackArray", extractScenarioSeries("undo_stack_array.json", "averageMemoryBytes"));
            undoMemory.put("QueueArray", extractScenarioSeries("undo_queue_array.json", "averageMemoryBytes"));
            undoMemory.put("StackLinkedList", extractScenarioSeries("undo_stack_list.json", "averageMemoryBytes"));
            undoMemory.put("QueueLinkedList", extractScenarioSeries("undo_queue_list.json", "averageMemoryBytes"));

            drawLineChart(
                undoMemory,
                "Escenario Undo - Memoria",
                "Cantidad de datos",
                "Memoria promedio (bytes)",
                "charts/undo_memory.png"
            );

            Map<String, List<DataPoint>> fifoMemory = new LinkedHashMap<>();
            fifoMemory.put("QueueArray", extractScenarioSeries("fifo_queue_array.json", "averageMemoryBytes"));
            fifoMemory.put("StackArray", extractScenarioSeries("fifo_stack_array.json", "averageMemoryBytes"));
            fifoMemory.put("QueueLinkedList", extractScenarioSeries("fifo_queue_list.json", "averageMemoryBytes"));
            fifoMemory.put("StackLinkedList", extractScenarioSeries("fifo_stack_list.json", "averageMemoryBytes"));

            drawLineChart(
                fifoMemory,
                "Escenario FIFO - Memoria",
                "Cantidad de datos",
                "Memoria promedio (bytes)",
                "charts/fifo_memory.png"
            );

            System.out.println("Gráficos generados correctamente en la carpeta charts/");

        } catch (Exception e) {
            System.err.println("Error al generar gráficos:");
            e.printStackTrace();
        }
    }

    private static String parseStructureNameFromFile(String fileName) {
        if (fileName.startsWith("array")) return "MyArray";
        if (fileName.startsWith("singly")) return "SinglyLinkedList";
        if (fileName.startsWith("doubly")) return "DoublyLinkedList";
        if (fileName.startsWith("double_ended")) return "DoubleEndedList";
        if (fileName.startsWith("circular")) return "CircularLinkedList";
        return fileName;
    }

    private static List<DataPoint> extractSeries(String filePath, String operation, String metric) throws IOException {
        String json = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
        List<String> objects = splitJsonObjects(json);
        List<DataPoint> points = new ArrayList<>();

        for (String obj : objects) {
            String op = extractStringField(obj, "operation");
            if (!operation.equals(op)) {
                continue;
            }

            int dataSize = extractIntField(obj, "dataSize");
            double value = extractDoubleField(obj, metric);
            points.add(new DataPoint(dataSize, value));
        }

        points.sort((a, b) -> Integer.compare(a.dataSize, b.dataSize));
        return points;
    }

    private static List<DataPoint> extractScenarioSeries(String filePath, String metric) throws IOException {
        String json = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
        List<String> objects = splitJsonObjects(json);
        List<DataPoint> points = new ArrayList<>();

        for (String obj : objects) {
            int dataSize = extractIntField(obj, "dataSize");
            double value = extractDoubleField(obj, metric);
            points.add(new DataPoint(dataSize, value));
        }

        points.sort((a, b) -> Integer.compare(a.dataSize, b.dataSize));
        return points;
    }

    private static List<String> splitJsonObjects(String json) {
        List<String> objects = new ArrayList<>();
        int braceCount = 0;
        int start = -1;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (c == '{') {
                if (braceCount == 0) {
                    start = i;
                }
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0 && start != -1) {
                    objects.add(json.substring(start, i + 1));
                    start = -1;
                }
            }
        }

        return objects;
    }

    private static String extractStringField(String jsonObject, String fieldName) {
        String marker = "\"" + fieldName + "\"";
        int fieldIndex = jsonObject.indexOf(marker);
        if (fieldIndex == -1) {
            throw new IllegalArgumentException("No se encontró el campo: " + fieldName);
        }

        int colonIndex = jsonObject.indexOf(':', fieldIndex);
        int firstQuote = jsonObject.indexOf('"', colonIndex + 1);
        int secondQuote = jsonObject.indexOf('"', firstQuote + 1);

        return jsonObject.substring(firstQuote + 1, secondQuote);
    }

    private static int extractIntField(String jsonObject, String fieldName) {
        String value = extractRawField(jsonObject, fieldName);
        return Integer.parseInt(value.trim());
    }

    private static double extractDoubleField(String jsonObject, String fieldName) {
        String value = extractRawField(jsonObject, fieldName);
        return Double.parseDouble(value.trim());
    }

    private static String extractRawField(String jsonObject, String fieldName) {
        String marker = "\"" + fieldName + "\"";
        int fieldIndex = jsonObject.indexOf(marker);
        if (fieldIndex == -1) {
            throw new IllegalArgumentException("No se encontró el campo: " + fieldName);
        }

        int colonIndex = jsonObject.indexOf(':', fieldIndex);
        int start = colonIndex + 1;

        while (start < jsonObject.length() && Character.isWhitespace(jsonObject.charAt(start))) {
            start++;
        }

        int end = start;
        while (end < jsonObject.length()) {
            char c = jsonObject.charAt(end);
            if (c == ',' || c == '\n' || c == '\r' || c == '}') {
                break;
            }
            end++;
        }

        return jsonObject.substring(start, end);
    }

    private static void drawLineChart(
            Map<String, List<DataPoint>> seriesMap,
            String title,
            String xLabel,
            String yLabel,
            String outputPath) throws IOException {

        int width = 1200;
        int height = 800;
        int left = 100;
        int right = 80;
        int top = 100;
        int bottom = 120;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, width, height);

        // Fuentes
        Font titleFont = new Font("SansSerif", Font.BOLD, 24);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 18);
        Font tickFont = new Font("SansSerif", Font.PLAIN, 14);

        // Área del gráfico
        int plotX = left;
        int plotY = top;
        int plotWidth = width - left - right;
        int plotHeight = height - top - bottom;

        // Hallar máximos
        int maxX = 0;
        double maxY = 0.0;

        for (List<DataPoint> points : seriesMap.values()) {
            for (DataPoint p : points) {
                if (p.dataSize > maxX) maxX = p.dataSize;
                if (p.value > maxY) maxY = p.value;
            }
        }

        if (maxY == 0) {
            maxY = 1;
        }

        // Ejes
        g.setColor(java.awt.Color.BLACK);
        g.setStroke(new BasicStroke(2f));
        g.drawLine(plotX, plotY + plotHeight, plotX + plotWidth, plotY + plotHeight);
        g.drawLine(plotX, plotY, plotX, plotY + plotHeight);

        // Título
        g.setFont(titleFont);
        g.drawString(title, 80, 50);

        // Etiquetas
        g.setFont(labelFont);
        g.drawString(xLabel, plotX + plotWidth / 2 - 70, height - 40);

        g.rotate(-Math.PI / 2);
        g.drawString(yLabel, -plotY - plotHeight / 2 - 70, 35);
        g.rotate(Math.PI / 2);

        // Marcas X
        int[] xTicks = {10, 100, 1000, 10000};
        g.setFont(tickFont);
        for (int xTick : xTicks) {
            int x = plotX + (int) ((xTick / (double) maxX) * plotWidth);
            g.drawLine(x, plotY + plotHeight, x, plotY + plotHeight + 8);
            g.drawString(String.valueOf(xTick), x - 15, plotY + plotHeight + 25);
        }

        // Marcas Y
        int yDivisions = 6;
        for (int i = 0; i <= yDivisions; i++) {
            double value = (maxY / yDivisions) * i;
            int y = plotY + plotHeight - (int) ((value / maxY) * plotHeight);

            g.setColor(new java.awt.Color(220, 220, 220));
            g.drawLine(plotX, y, plotX + plotWidth, y);

            g.setColor(java.awt.Color.BLACK);
            g.drawLine(plotX - 8, y, plotX, y);
            g.drawString(formatValue(value), 10, y + 5);
        }

        // Colores automáticos simples
        java.awt.Color[] colors = {
            java.awt.Color.BLUE,
            java.awt.Color.RED,
            java.awt.Color.GREEN.darker(),
            java.awt.Color.MAGENTA,
            java.awt.Color.ORANGE,
            java.awt.Color.CYAN.darker(),
            java.awt.Color.PINK,
            java.awt.Color.GRAY
        };

        // Dibujar series
        int colorIndex = 0;
        int legendX = plotX + plotWidth - 220;
        int legendY = plotY + 20;

        for (Map.Entry<String, List<DataPoint>> entry : seriesMap.entrySet()) {
            java.awt.Color color = colors[colorIndex % colors.length];
            List<DataPoint> points = entry.getValue();

            g.setColor(color);
            g.setStroke(new BasicStroke(3f));

            for (int i = 0; i < points.size() - 1; i++) {
                DataPoint p1 = points.get(i);
                DataPoint p2 = points.get(i + 1);

                int x1 = plotX + (int) ((p1.dataSize / (double) maxX) * plotWidth);
                int y1 = plotY + plotHeight - (int) ((p1.value / maxY) * plotHeight);

                int x2 = plotX + (int) ((p2.dataSize / (double) maxX) * plotWidth);
                int y2 = plotY + plotHeight - (int) ((p2.value / maxY) * plotHeight);

                g.drawLine(x1, y1, x2, y2);
            }

            for (DataPoint p : points) {
                int x = plotX + (int) ((p.dataSize / (double) maxX) * plotWidth);
                int y = plotY + plotHeight - (int) ((p.value / maxY) * plotHeight);
                g.fillOval(x - 4, y - 4, 8, 8);
            }

            // Leyenda
            g.fillRect(legendX, legendY + colorIndex * 24, 18, 10);
            g.setColor(java.awt.Color.BLACK);
            g.drawString(entry.getKey(), legendX + 28, legendY + 10 + colorIndex * 24);

            colorIndex++;
        }

        g.dispose();
        ImageIO.write(image, "png", Paths.get(outputPath).toFile());
    }

    private static String formatValue(double value) {
        if (value >= 1_000_000_000) {
            return String.format("%.1fB", value / 1_000_000_000.0);
        }
        if (value >= 1_000_000) {
            return String.format("%.1fM", value / 1_000_000.0);
        }
        if (value >= 1_000) {
            return String.format("%.1fK", value / 1_000.0);
        }
        return String.format("%.0f", value);
    }
}
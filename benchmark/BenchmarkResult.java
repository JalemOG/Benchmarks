package benchmark;

public class BenchmarkResult {
    private String structureName;
    private String operationName;
    private int dataSize;
    private long[] executionTimes;
    private long[] memoryUsages;
    private double averageTimeNs;
    private double averageMemoryBytes;

    public BenchmarkResult(String structureName, String operationName, int dataSize, int runs) {
        this.structureName = structureName;
        this.operationName = operationName;
        this.dataSize = dataSize;
        this.executionTimes = new long[runs];
        this.memoryUsages = new long[runs];
    }

    public void setRunData(int runIndex, long timeNs, long memoryBytes) {
        executionTimes[runIndex] = timeNs;
        memoryUsages[runIndex] = memoryBytes;
    }

    public void calculateAverages() {
        long totalTime = 0;
        long totalMemory = 0;

        for (int i = 0; i < executionTimes.length; i++) {
            totalTime += executionTimes[i];
            totalMemory += memoryUsages[i];
        }

        averageTimeNs = (double) totalTime / executionTimes.length;
        averageMemoryBytes = (double) totalMemory / memoryUsages.length;
    }

    public String getStructureName() {
        return structureName;
    }

    public String getOperationName() {
        return operationName;
    }

    public int getDataSize() {
        return dataSize;
    }

    public long[] getExecutionTimes() {
        return executionTimes;
    }

    public long[] getMemoryUsages() {
        return memoryUsages;
    }

    public double getAverageTimeNs() {
        return averageTimeNs;
    }

    public double getAverageMemoryBytes() {
        return averageMemoryBytes;
    }
}
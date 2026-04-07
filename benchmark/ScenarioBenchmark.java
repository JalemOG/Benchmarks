package benchmark;

public class ScenarioBenchmark {

    private static final int[] TEST_SIZES = {10, 100, 1000, 10000};
    private static final int RUNS = 5;

    public static BenchmarkResult[] benchmarkUndoScenario(
            String name,
            StackFactory stackFactory,
            QueueFactory queueFactory,
            boolean useStack) {

        BenchmarkResult[] results = new BenchmarkResult[TEST_SIZES.length];

        for (int i = 0; i < TEST_SIZES.length; i++) {
            int size = TEST_SIZES[i];
            BenchmarkResult result = new BenchmarkResult(name, "undo", size, RUNS);

            for (int run = 0; run < RUNS; run++) {

                System.gc();
                long memBefore = usedMemory();
                long start = System.nanoTime();

                if (useStack) {
                    StackStructure<String> s = stackFactory.create();
                    for (int j = 0; j < size; j++) s.push("a");
                    while (!s.isEmpty()) s.pop();
                } else {
                    QueueStructure<String> q = queueFactory.create();
                    for (int j = 0; j < size; j++) q.enqueue("a");
                    while (!q.isEmpty()) q.dequeue();
                }

                long end = System.nanoTime();
                long memAfter = usedMemory();

                result.setRunData(run, end - start, Math.max(0, memAfter - memBefore));
            }

            result.calculateAverages();
            results[i] = result;
        }

        return results;
    }

    public static BenchmarkResult[] benchmarkFifoScenario(
            String name,
            StackFactory stackFactory,
            QueueFactory queueFactory,
            boolean useQueue) {

        BenchmarkResult[] results = new BenchmarkResult[TEST_SIZES.length];

        for (int i = 0; i < TEST_SIZES.length; i++) {
            int size = TEST_SIZES[i];
            BenchmarkResult result = new BenchmarkResult(name, "fifo", size, RUNS);

            for (int run = 0; run < RUNS; run++) {

                System.gc();
                long memBefore = usedMemory();
                long start = System.nanoTime();

                if (useQueue) {
                    QueueStructure<String> q = queueFactory.create();
                    for (int j = 0; j < size; j++) q.enqueue("c");
                    while (!q.isEmpty()) q.dequeue();
                } else {
                    StackStructure<String> s = stackFactory.create();
                    for (int j = 0; j < size; j++) s.push("c");
                    while (!s.isEmpty()) s.pop();
                }

                long end = System.nanoTime();
                long memAfter = usedMemory();

                result.setRunData(run, end - start, Math.max(0, memAfter - memBefore));
            }

            result.calculateAverages();
            results[i] = result;
        }

        return results;
    }

    private static long usedMemory() {
        Runtime r = Runtime.getRuntime();
        return r.totalMemory() - r.freeMemory();
    }

    public interface StackFactory {
        StackStructure<String> create();
    }

    public interface QueueFactory {
        QueueStructure<String> create();
    }
}
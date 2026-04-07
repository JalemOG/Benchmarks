package benchmark;

public class StructureBenchmark {

    private static final int[] TEST_SIZES = {10, 100, 1000, 10000};
    private static final int RUNS = 5;

    public static BenchmarkResult[] benchmarkLinearStructure(String name, StructureFactory factory) {

        String[] operations = {
                "addFirst", "addLast", "insertMiddle",
                "removeMiddle", "search", "getByIndex", "replace"
        };

        BenchmarkResult[] results = new BenchmarkResult[TEST_SIZES.length * operations.length];
        int index = 0;

        for (int size : TEST_SIZES) {
            for (String op : operations) {

                BenchmarkResult result = new BenchmarkResult(name, op, size, RUNS);

                for (int run = 0; run < RUNS; run++) {
                    LinearStructure<Integer> s = factory.create();

                    // preload
                    for (int i = 0; i < size; i++) {
                        s.addLast(i);
                    }

                    System.gc();
                    long memBefore = usedMemory();

                    long start = System.nanoTime();

                    int mid = s.size() / 2;

                    switch (op) {
                        case "addFirst": s.addFirst(-1); break;
                        case "addLast": s.addLast(-1); break;
                        case "insertMiddle": s.insert(mid, -1); break;
                        case "removeMiddle": if (!s.isEmpty()) s.removeAt(mid); break;
                        case "search": s.indexOf(size - 1); break;
                        case "getByIndex": if (!s.isEmpty()) s.get(mid); break;
                        case "replace": if (!s.isEmpty()) s.set(mid, 999); break;
                    }

                    long end = System.nanoTime();

                    long memAfter = usedMemory();

                    result.setRunData(run, end - start, Math.max(0, memAfter - memBefore));
                }

                result.calculateAverages();
                results[index++] = result;
            }
        }

        return results;
    }

    private static long usedMemory() {
        Runtime r = Runtime.getRuntime();
        return r.totalMemory() - r.freeMemory();
    }

    public interface StructureFactory {
        LinearStructure<Integer> create();
    }
}
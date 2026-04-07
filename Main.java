import benchmark.BenchmarkResult;
import benchmark.JsonExporter;
import benchmark.StructureBenchmark;
import benchmark.ScenarioBenchmark;

import structures.MyArray;
import structures.SinglyLinkedList;
import structures.DoublyLinkedList;
import structures.DoubleEndedList;
import structures.CircularLinkedList;
import structures.StackArray;
import structures.StackLinkedList;
import structures.QueueArray;
import structures.QueueLinkedList;

public class Main {

    public static void main(String[] args) {
        try {
            // =========================
            // COMPARACIÓN 1: ARRAYS VS LISTAS
            // =========================
            BenchmarkResult[] arrayResults = StructureBenchmark.benchmarkLinearStructure(
                    "MyArray",
                    MyArray::new
            );

            BenchmarkResult[] singlyResults = StructureBenchmark.benchmarkLinearStructure(
                    "SinglyLinkedList",
                    SinglyLinkedList::new
            );

            BenchmarkResult[] doublyResults = StructureBenchmark.benchmarkLinearStructure(
                    "DoublyLinkedList",
                    DoublyLinkedList::new
            );

            BenchmarkResult[] doubleEndedResults = StructureBenchmark.benchmarkLinearStructure(
                    "DoubleEndedList",
                    DoubleEndedList::new
            );

            BenchmarkResult[] circularResults = StructureBenchmark.benchmarkLinearStructure(
                    "CircularLinkedList",
                    CircularLinkedList::new
            );

            JsonExporter.exportResults(arrayResults, "array_results.json");
            JsonExporter.exportResults(singlyResults, "singly_results.json");
            JsonExporter.exportResults(doublyResults, "doubly_results.json");
            JsonExporter.exportResults(doubleEndedResults, "double_ended_results.json");
            JsonExporter.exportResults(circularResults, "circular_results.json");

            // =========================
            // COMPARACIÓN 2: ESCENARIO UNDO
            // Stack correcto / Queue incorrecto
            // =========================
            BenchmarkResult[] undoStackArray = ScenarioBenchmark.benchmarkUndoScenario(
                    "StackArray",
                    StackArray::new,
                    QueueArray::new,
                    true
            );

            BenchmarkResult[] undoQueueArray = ScenarioBenchmark.benchmarkUndoScenario(
                    "QueueArray",
                    StackArray::new,
                    QueueArray::new,
                    false
            );

            BenchmarkResult[] undoStackList = ScenarioBenchmark.benchmarkUndoScenario(
                    "StackLinkedList",
                    StackLinkedList::new,
                    QueueLinkedList::new,
                    true
            );

            BenchmarkResult[] undoQueueList = ScenarioBenchmark.benchmarkUndoScenario(
                    "QueueLinkedList",
                    StackLinkedList::new,
                    QueueLinkedList::new,
                    false
            );

            JsonExporter.exportResults(undoStackArray, "undo_stack_array.json");
            JsonExporter.exportResults(undoQueueArray, "undo_queue_array.json");
            JsonExporter.exportResults(undoStackList, "undo_stack_list.json");
            JsonExporter.exportResults(undoQueueList, "undo_queue_list.json");

            // =========================
            // COMPARACIÓN 2: ESCENARIO FIFO
            // Queue correcto / Stack incorrecto
            // =========================
            BenchmarkResult[] fifoQueueArray = ScenarioBenchmark.benchmarkFifoScenario(
                    "QueueArray",
                    StackArray::new,
                    QueueArray::new,
                    true
            );

            BenchmarkResult[] fifoStackArray = ScenarioBenchmark.benchmarkFifoScenario(
                    "StackArray",
                    StackArray::new,
                    QueueArray::new,
                    false
            );

            BenchmarkResult[] fifoQueueList = ScenarioBenchmark.benchmarkFifoScenario(
                    "QueueLinkedList",
                    StackLinkedList::new,
                    QueueLinkedList::new,
                    true
            );

            BenchmarkResult[] fifoStackList = ScenarioBenchmark.benchmarkFifoScenario(
                    "StackLinkedList",
                    StackLinkedList::new,
                    QueueLinkedList::new,
                    false
            );

            JsonExporter.exportResults(fifoQueueArray, "fifo_queue_array.json");
            JsonExporter.exportResults(fifoStackArray, "fifo_stack_array.json");
            JsonExporter.exportResults(fifoQueueList, "fifo_queue_list.json");
            JsonExporter.exportResults(fifoStackList, "fifo_stack_list.json");

            System.out.println("Benchmarks ejecutados correctamente.");
            System.out.println("Archivos JSON generados:");
            System.out.println("- array_results.json");
            System.out.println("- singly_results.json");
            System.out.println("- doubly_results.json");
            System.out.println("- double_ended_results.json");
            System.out.println("- circular_results.json");
            System.out.println("- undo_stack_array.json");
            System.out.println("- undo_queue_array.json");
            System.out.println("- undo_stack_list.json");
            System.out.println("- undo_queue_list.json");
            System.out.println("- fifo_queue_array.json");
            System.out.println("- fifo_stack_array.json");
            System.out.println("- fifo_queue_list.json");
            System.out.println("- fifo_stack_list.json");

            System.out.println("Generando gráficos...");
            ChartGenerator.main(new String[0]);
            System.out.println("Gráficos generados correctamente en charts/");

        } catch (Exception e) {
            System.err.println("Error al ejecutar los benchmarks:");
            e.printStackTrace();
        }
    }
}
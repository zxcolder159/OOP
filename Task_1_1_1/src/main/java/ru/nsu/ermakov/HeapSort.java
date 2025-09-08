package ru.nsu.ermakov;
import java.util.Scanner;
/**
 * Класс для сортировки массива с помощью алгоритма пирамидальной сортировки (Heapsort)
 */
public class HeapSort {

    /**
     * Конструктор класса Heapsort
     */
    public HeapSort() {
        // Конструктор по умолчанию
    }

    /**
     * Меняет местами два элемента массива
     * @param array массив
     * @param i индекс первого элемента
     * @param j индекс второго элемента
     */
    private static void swap(int[] array, int i, int j){
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Преобразует поддерево в max-кучу
     * @param array массив
     * @param n размер кучи
     * @param i корневой индекс
     */
    private static void heapify(int[] array, int n, int i){
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        if (left < n && array[left] > array[largest]) {
            largest = left;
        }
        if (right < n && array[right] > array[largest]) {
            largest = right;
        }
        if (largest != i) {
            swap(array, i, largest);
            heapify(array, n, largest);
        }
    }

    /**
     * Строит max-кучу из массива
     * @param array массив для преобразования
     * @param n размер массива
     */
    private static void buildMaxHeap(int[] array, int n) {
        for(int i = n / 2 - 1; i >= 0; i--){
            heapify(array, n, i);
        }
    }

    /**
     * Выполняет пирамидальную сортировку массива
     * @param array массив для сортировки
     * @return строка с отсортированным массивом в формате [1, 2, 3]
     * @throws IllegalArgumentException если массив null
     */
    public static String heapsort(int[] array){
        if (array == null) {
            throw new IllegalArgumentException("Массив не может быть null");
        }
        
        int n = array.length;
        if (n == 0) {
            return "[]";
        }
        
        int[] sortedArray = array.clone();
        buildMaxHeap(sortedArray, n);
        for(int i = n - 1; i >= 0; i--){
            swap(sortedArray, 0, i);
            heapify(sortedArray, i, 0);
        }
        
        return arrayToString(sortedArray);
    }

    /**
     * Преобразует массив в строку
     * @param array массив
     * @return строка в формате [1, 2, 3]
     */
    private static String arrayToString(int[] array) {
        StringBuilder result = new StringBuilder("[");
        for(int i = 0; i < array.length; i++){
            result.append(array[i]);
            if (i < array.length - 1) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }

    /**
     * Вспомогательный метод для тестирования
     * @param array массив для сортировки
     * @return отсортированная копия массива
     * @throws IllegalArgumentException если массив null
     */
    public static int[] heapsortForTest(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Массив не может быть null");
        }
        
        int[] copy = array.clone();
        if (copy.length == 0) {
            return copy;
        }
        
        int n = copy.length;
        buildMaxHeap(copy, n);
        for(int i = n - 1; i >= 0; i--){
            swap(copy, 0, i);
            heapify(copy, i, 0);
        }
        return copy;
    }

    /**
     * Главный метод приложения
     * @param args аргументы командной строки
     */
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите числа через пробел:");
        String input = scanner.nextLine();

        if (input.trim().isEmpty()) {
            System.out.println("Вы не ввели числа!");
            scanner.close();
            return;
        }

        try {
            String[] numbers = input.split(" ");
            int[] array = new int[numbers.length];
            for (int i = 0; i < numbers.length; i++) {
                array[i] = Integer.parseInt(numbers[i]);
            }

            String result = heapsort(array);
            System.out.println("Отсортированный массив:");
            System.out.println(result);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите только целые числа!");
        } catch (Exception e) {
            System.out.println("Произошла ошибка: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}

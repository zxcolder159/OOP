package ru.nsu.masolygin;

/**
 * Сортировка кучей (Heapsort)
 */
public class Heapsort {
    /**
     * Сортирует массив целых чисел с использованием алгоритма пирамидальной сортировки
     *
     * @param arr Массив для сортировки
     */
    public static void heapsort(int[] arr) {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            shiftDown(arr, n, i);
        }
        for (int i = n - 1; i >= 0; i--) {
            swap(arr, 0, i);
            shiftDown(arr, i, 0);
        }
    }

    /**
     * Меняет местами два элемента в массиве
     *
     * @param arr Массив
     * @param i   Индекс первого элемента
     * @param j   Индекс второго элемента
     */
    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Восстанавливает свойство кучи, начиная с узла i, в массиве размером n
     *
     * @param arr Массив, представляющий кучу
     * @param n   Размер кучи
     * @param i   Индекс узла, с которого начинается восстановление
     */
    public static void shiftDown(int[] arr, int n, int i) {
        int root = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;


        if (left < n && arr[left] > arr[root]) {
            root = left;
        }
        if (right < n && arr[right] > arr[root]) {
            root = right;
        }

        if (root != i) {
            swap(arr, i, root);
            shiftDown(arr, n, root);
        }
    }

}
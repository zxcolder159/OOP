import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.InputStream;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Test
public void testMainWithValidInput() {
    String input = "5 3 8 1 4\n";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
    
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));
    
    // Запускаем main
    assertDoesNotThrow(() -> HeapSort.main(new String[]{}));
    
    // Восстанавливаем стандартный вывод
    System.setOut(originalOut);
    System.setIn(System.in);
    
    String output = outContent.toString();
    assertTrue(output.contains("Отсортированный массив:"));
    assertTrue(output.contains("[1, 3, 4, 5, 8]"));
}

@Test
public void testMainWithEmptyInput() {
    String input = "\n";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
    
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));
    
    HeapSort.main(new String[]{});
    
    System.setOut(originalOut);
    System.setIn(System.in);
    
    String output = outContent.toString();
    assertTrue(output.contains("Вы не ввели числа!"));
}

@Test
public void testMainWithInvalidInput() {
    String input = "5 abc 3\n";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
    
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));
    
    HeapSort.main(new String[]{});
    
    System.setOut(originalOut);
    System.setIn(System.in);
    
    String output = outContent.toString();
    assertTrue(output.contains("Ошибка: введите только целые числа!"));
}

@Test
public void testMainWithSingleNumber() {
    String input = "42\n";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
    
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));
    
    HeapSort.main(new String[]{});
    
    System.setOut(originalOut);
    System.setIn(System.in);
    
    String output = outContent.toString();
    assertTrue(output.contains("Отсортированный массив:"));
    assertTrue(output.contains("[42]"));
}

@Test
public void testMainWithNegativeNumbers() {
    String input = "-5 3 -1 0\n";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
    
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));
    
    HeapSort.main(new String[]{});
    
    System.setOut(originalOut);
    System.setIn(System.in);
    
    String output = outContent.toString();
    assertTrue(output.contains("Отсортированный массив:"));
    assertTrue(output.contains("[-5, -1, 0, 3]"));
}

@Test
public void testMainWithMultipleSpaces() {
    String input = "   5   3   8   1   \n";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);
    
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outContent));
    
    HeapSort.main(new String[]{});
    
    System.setOut(originalOut);
    System.setIn(System.in);
    
    String output = outContent.toString();
    assertTrue(output.contains("Отсортированный массив:"));
    assertTrue(output.contains("[1, 3, 5, 8]"));
}

@Test
public void testHeapifyMethodDirectly() {
    int[] array = {3, 1, 4, 1, 5};

    assertDoesNotThrow(() -> {
        HeapSort.sort(array);
    });
}

@Test
public void testBuildMaxHeapIndirectly() {
    int[] array = {3, 1, 4};

    HeapSort.sort(array);
    assertArrayEquals(new int[]{1, 3, 4}, array);
}

@Test
public void testSwapLogicThroughIntegration() {
    int[] array = {5, 3};
    HeapSort.sort(array);
    assertArrayEquals(new int[]{3, 5}, array);
}

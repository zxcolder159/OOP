package ru.nsu.ermakov;

import java.util.Map;
import java.util.Scanner;

/**
 * Main class to test the expression parsing, evaluation, and differentiation.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter expression: ");
        String expression = scanner.nextLine();

        System.out.print("Enter variable assignments (e.g., x=10; y=20): ");
        String assignments = scanner.nextLine();

        try {
            // Создаем экземпляр парсера
            Parser parser;
            parser = new Parser(expression);
            // Парсим выражение
            Expression parsedExpr = parser.parse(expression);

            // Разбираем переменные
            Assignments assignmentsParser = new Assignments();
            Map<String, Integer> env = assignmentsParser.parseEnv(assignments);

            // Вычисляем результат
            int result = parsedExpr.eval(env);
            System.out.println("Result: " + result);

            // Вычисляем производную
            Expression derivative = parsedExpr.derivative("x");
            System.out.println("Derivative (w.r.t x): " + derivative.print());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }
}

@echo off
chcp 65001 >nul

echo === Heapsort Test ===
echo.

echo 1. Компиляция тестов...
javac -cp build test/java/HeapsortTest.java
if %errorlevel% neq 0 (
    echo Ошибка компиляции тестов!
    pause
    exit /b 1
)

echo 2. Запуск тестов...
java -cp build;test/java HeapsortTest
if %errorlevel% neq 0 (
    echo Тесты не прошли!
    pause
    exit /b 1
)

echo.
echo === Тесты прошли успешно ===
pause
@echo off
chcp 65001 >nul

echo === Heapsort Full Cycle ===
echo.

echo 1. Очистка...
if exist build rmdir /s /q build
if exist docs rmdir /s /q docs
del Heapsort.jar 2>nul
del test\java\*.class 2>nul

echo 2. Сборка...
javac -d build src/java/Heapsort.java
if %errorlevel% neq 0 (
    echo Ошибка компиляции!
    pause
    exit /b 1
)

cd build
jar cfe ../Heapsort.jar Heapsort *
cd ..

echo 3. Тестирование...
javac -cp build test/java/HeapsortTest.java
if %errorlevel% neq 0 (
    echo Ошибка компиляции тестов!
    pause
    exit /b 1
)

java -cp build;test/java HeapsortTest
if %errorlevel% neq 0 (
    echo Тесты не прошли!
    pause
    exit /b 1
)

echo 4. Запуск...
java -jar Heapsort.jar

echo.
echo === Полный цикл завершен ===
pause
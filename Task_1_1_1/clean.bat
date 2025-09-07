@echo off
chcp 65001 >nul

echo === Heapsort Clean ===
echo.

echo Удаление сборок...
if exist build rmdir /s /q build
if exist docs rmdir /s /q docs
del Heapsort.jar 2>nul
del test\java\*.class 2>nul

echo Очистка завершена!
pause
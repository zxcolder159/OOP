@echo off
chcp 65001 >nul

echo === Heapsort Direct Run ===
echo.

if not exist build (
    echo Сначала выполните build.bat
    pause
    exit /b 1
)

java -cp build Heapsort
pause
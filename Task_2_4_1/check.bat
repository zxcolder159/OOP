@echo off
set CONFIG=%1
if "%CONFIG%"=="" set CONFIG=config.groovy

set REPORT=%2
if "%REPORT%"=="" set REPORT=report.html

echo 🚀 Запуск автоматической проверки ООП...
echo 📄 Используем конфигурацию: %CONFIG%
echo 📊 Результат будет сохранен в: %REPORT%
echo --------------------------------------------------------

call gradlew.bat -q run --args="report %CONFIG% %REPORT%"

echo --------------------------------------------------------
echo ✅ Проверка завершена!
echo 🌐 Итоговый отчет сгенерирован: %REPORT%

# Структура проекта Task_2_4_1

Ниже краткая карта репозитория и пояснения — чтобы быстро вникнуть, где что находится и за что отвечает.

> Проект Java/Gradle для автоматической проверки заданий курса (клонирование репозиториев, запуск сборки/тестов, формирование HTML-отчёта).

---

## Корневая структура (важные файлы/папки)

- `build.gradle`, `settings.gradle`, `gradlew`, `gradlew.bat` — конфигурация и wrapper Gradle
- `config.groovy` — пример/дефолтный DSL-конфиг курса
- `src/` — исходники проекта
  - `main/java/` — основной код
  - `test/java/` — тесты (если есть)
- `report.html`, `preview-report.html` — примеры сгенерированных отчётов/предпросмотра

---

## Важные пакеты и файлы (src/main/java)

- `ru.nsu.ermakov` — точка входа
  - `Main.java` — main(). Парсит аргументы, загружает конфигурацию, вызывает проверку или только клонирование.

- `ru.nsu.ermakov.checker` — логика проверки курса
  - `CourseChecker.java` — основной оркестратор: проходит по группам и студентам, вызывает `TaskCheckService`.
  - `GitRepositoryInspector.java` — вспомогательный класс для чтения метаданных git (последние коммиты, статистика активности).
  - Другие классы (TaskCheckService, TaskPathResolver, GradleTaskRunner, TestReportParser, ScoringPolicy и т.д.) — используются для запуска сборки и парсинга результатов.
  - `ActivityStats`, `TestStats`, `TestCheckResult` и т.п. — DTO/record-ы для результатов.

- `ru.nsu.ermakov.entity` — модели данных (Config, Group, Student, Task, SystemSettings, Checkpoint и т.д.)
  - `Config` — объект конфигурации курса (группы, задачи, настройки и т.д.)
  - `SystemSettings` — настройки времени сборки/git, весов частей оценки.

- `ru.nsu.ermakov.dsl` — загрузчик и парсер DSL-конфига на groovy
  - `ConfigLoader.java` — читает/интерпретирует `config.groovy` и возвращает `Config`
  - `ConfigBuilder.java` — строитель конфигурации, используемый `ConfigLoader` (обрабатывает include(), группы, задачи, т.д.)
  - `ConfigIncludeException.java` — единый тип исключения для ошибок include()

- `ru.nsu.ermakov.vcs` — работу с VCS
  - `RepoDownloader.java` — клонирование/обновление репозиториев студентов
  - `CommandExecutor.java` — выполнение процесса (git/gradle) и сбор логов

- `ru.nsu.ermakov.report` — генерация HTML-отчёта
  - `HtmlReportRenderer.java` — собирает HTML-отчёт по результатам проверки. В проекте используется Java text block для статической части шаблона и затем динамически подставляются данные.

- `ru.nsu.ermakov.util` — мелкие утилиты
  - `Log.java` — простая утилита для форматированных логов (Log.info / Log.error). Сейчас выводит в stdout; при желании можно перенести ошибки в stderr или подключить реальный логгер.

---

## Что делает программа (кратко)

- Загружает конфигурацию курса (groovy DSL) — группы, студенты, задачи, настройки.
- Для каждой группы/студента (в зависимости от резолвера задач) клонирует/обновляет репозиторий студента.
- Переключается на `main` или `master` ветку, собирает проект и запускает тесты (через Gradle runner).
- Собирает результаты по задачам и контрольным точкам, рассчитывает баллы и финальные оценки.
- Генерирует HTML-отчёт (печатается в stdout) — `Main` печатает HTML в stdout, чтобы его можно было перенаправить в файл.

---

## Файлы, которые мы недавно правили

(чтобы было проще ориентироваться по истории изменений)

- `CourseChecker.java` — логи переведены на `Log.info(...)`, а раньше использовался `System.err`.
- `GitRepositoryInspector.java` — добавлено детальное диагностическое логирование при ошибках git/парсинга дат.
- `ConfigBuilder.java` — include() начал бросать `ConfigIncludeException` вместо нескольких разных исключений.
- `ConfigIncludeException.java` — новый класс исключения.
- `HtmlReportRenderer.java` — статическая часть HTML вынесена в Java text block; динамические вставки переведены на `String.format`/плейсхолдеры для чистоты кода.
- `Log.java` — новая утилита логирования (используется в нескольких местах).
- `Main.java` — `cloneRepositories` сделан нестатическим (ООП-стиль), вызов в `main` — через `new Main()`.

---

## Быстрые команды (как запускать)

- Сборка проекта (Gradle wrapper):

```bash
./gradlew build
```

- Запуск тестов:

```bash
./gradlew test
```

- Запуск генерации отчёта (пример):

```bash
# выведет HTML в stdout, перенаправьте в файл
java -cp build/libs/Task_2_4_1-1.0-SNAPSHOT.jar ru.nsu.ermakov.Main report config.groovy > report.html
# или через gradle run (если настроен)
./gradlew run --args="report config.groovy"
```

- Клонирование репозиториев (пример):

```bash
java -cp build/libs/Task_2_4_1-1.0-SNAPSHOT.jar ru.nsu.ermakov.Main clone config.groovy
```

---

## Важные замечания / советы

- Java версии: в коде используется text block ("""), это фича Java 15+. Убедитесь, что ваша JDK (и CI) поддерживает text blocks. Если требуется совместимость с Java 11, text block нужно заменить на обычные строки.

- Логирование: сейчас простое `Log` выводит всё в stdout. Если вы будете перенаправлять stdout в файл для отчёта, логи попадут в тот же файл. Для разделения отчёта и логов имеет смысл вывод логов в stderr или подключить полноценный логгер (slf4j, java.util.logging).

- DSL-конфигурация: смотрите `config.groovy` — через `ConfigBuilder` формируется объект `Config`. Если будете править DSL, тестируйте include() и относительные пути — для include добавлено единое исключение `ConfigIncludeException`.

- Где смотреть первоочередно при отладке:
  - ошибки клонирования/checkout — `RepoDownloader` / `CommandExecutor` / вывод логов в `Log`.
  - проблемы с датами/активностью — `GitRepositoryInspector` (теперь логирует причины, почему вернул null).
  - генерация HTML — `HtmlReportRenderer` (шаблон + динамика).

---

Если нужно, могу:
- добавить диаграмму зависимостей (кто вызывает кого) в markdown;
- сделать readme с подробным шагом «как запустить отладочную сессию»;
- вернуть text block в обычные строки для совместимости с Java 11;
- унифицировать вывод логов (stdout vs stderr) как вам удобнее.

Напишите, что из этого хотите — допилю. Удачи в разборе проекта!

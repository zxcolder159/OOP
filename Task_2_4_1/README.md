# OOP Course Repository Checker

Консольное приложение для преподавателя ООП, которое по Groovy DSL-конфигурации:
- клонирует/обновляет учебные Git-репозитории студентов;
- проверяет задачи через Gradle пайплайн;
- считает баллы, оценки по контрольным точкам и итоговую оценку;
- учитывает недельную активность по коммитам;
- печатает HTML-отчет в стандартный вывод.

## Что реализовано

- DSL на Groovy для описания:
  - задач курса;
  - групп и студентов;
  - заданий на проверку (assignments);
  - контрольных точек;
  - системных настроек (таймауты, веса, пороги и т.д.).
- Поддержка include в конфиге для разделения:
  - долгоживущих данных (например, задачи курса),
  - данных на семестр (например, список групп),
  - оперативных запусков (например, assignments и бонусы).
- Проверка репозитория в ветке main/master:
  - compile,
  - затем javadoc + checkstyle,
  - затем test (только если docs/style успешны).
- Подсчет статистики тестов: passed / failed / skipped.
- Взвешенная формула балла задачи с дедлайн-штрафами.
- Итоговые оценки по контрольным точкам и финалу.
- Учет недельной активности и влияние на финальную оценку.
- HTML-отчет в stdout.

## Требования

- JDK 21
- Gradle Wrapper (уже в проекте)
- Установленный git в PATH

## Сборка и тесты

```bash
./gradlew test
./gradlew clean build
```

## Запуск программы

Мы добавили удобные скрипты для запуска: `check.sh` (Linux/Mac) и `check.bat` (Windows). 
Они скрывают лишний вывод Gradle и автоматически сохраняют HTML-отчет в файл.

**Запуск с конфигурацией по умолчанию (`config.groovy`) в файл `report.html`:**
```bash
./check.sh
```

**Запуск с вашей конфигурацией и своим файлом-отчетом:**
```bash
./check.sh semester.groovy result.html
```

*(На Windows используйте команду `check.bat` соответственно).*

### Продвинутый запуск (через Gradle напрямую)

Приложение также можно запустить через main-класс `ru.nsu.ermakov.Main`:

```bash
# Получить HTML в stdout:
./gradlew -q run

# Указать конфиг и перенаправить в файл
./gradlew -q run --args='report config.groovy' > report.html

# Только склонировать репозитории (без проверки):
./gradlew run --args='clone config.groovy'
```

## Формат DSL

Дата: dd-MM-yyyy.

### Блок tasks

```groovy
tasks {
    task {
        id = "Task_2_4_1"           // идентификатор
        title = "Course Checker"    // название
        // name можно использовать как обратную совместимость вместо id/title
        maxScore = 5
        softDeadline = "01-04-2026"
        hardDeadline = "08-04-2026"
    }
}
```

### Блок groups

```groovy
groups {
    group {
        name = "24216"
        students = [
            [fio: "Иванов Иван Иванович", githubNick: "ivan", repoUrl: "https://github.com/user/repo.git"],
            [fio: "Петров Петр Петрович", githubNick: "petr", repoUrl: "https://github.com/user/repo2.git"]
        ]
    }
}
```

### Блок assignments

Позволяет проверять не всех студентов и/или не все задачи.

```groovy
assignments {
    assignment {
        githubNick = "ivan"
        tasks = ["Task_2_4_1", "Task_2_3_1"]
    }

    assignment {
        fio = "Петров Петр Петрович"
        tasks = ["Task_2_1_1"]
    }
}
```

Если assignments отсутствует, проверяются все студенты и все задачи.

### Блок checkpoints

```groovy
checkpoints {
    checkpoint {
        name = "Контрольная 1"
        date = "15-04-2026"
    }
    checkpoint {
        name = "Итог"
        date = "30-06-2026"
    }
}
```

### Блок settings

```groovy
settings {
    buildTimeoutSeconds = 300
    gitTimeoutSeconds = 60

    // Веса частей балла задачи (нормализуются автоматически)
    compilePart = 0.5
    docsStylePart = 0.2
    testsPart = 0.3

    // Штрафы за пропуск дедлайнов
    deadlineMissPenalty = 0.5
    maxDeadlinePenalty = 1.0

    // Пороги оценок в процентах
    excellentThreshold = 85.0
    goodThreshold = 70.0
    satisfactoryThreshold = 50.0

    // Влияние активности
    activityBonusThreshold = 0.60
    activityPenaltyThreshold = 0.30
}
```

## Include и разделение конфигов

Пример структуры:

- tasks.base.groovy: задачи курса (редко меняются)
- semester.groovy: группы/студенты (раз в семестр)
- run.groovy: assignments и оперативные настройки (часто)

Пример run.groovy:

```groovy
include "semester.groovy"

assignments {
    assignment {
        githubNick = "ivan"
        tasks = ["Task_2_4_1"]
    }
}

settings {
    compilePart = 0.6
    docsStylePart = 0.2
    testsPart = 0.2
}
```

Пример semester.groovy:

```groovy
include "tasks.base.groovy"

groups {
    group {
        name = "24216"
        students = [
            [fio: "Иванов Иван Иванович", githubNick: "ivan", repoUrl: "https://github.com/user/repo.git"]
        ]
    }
}

checkpoints {
    checkpoint {
        name = "КТ1"
        date = "15-04-2026"
    }
}
```

## Логика проверки задачи

1. Ищется папка задачи в репозитории студента.
2. Запускается compile.
3. Если compile успешен, запускаются javadoc и checkstyle.
4. Если docs/style успешны, запускаются test и парсится test-report.
5. Считается балл задачи по весам и дедлайн-штрафам.

## Формула балла

Пусть:
- M — maxScore задачи,
- C, D, T — веса compile/docsStyle/tests,
- p — доля успешно пройденных тестов,
- P — суммарный штраф за дедлайны.

Тогда базовый балл:

score_base = M * (C * I_compile + D * I_docsStyle + T * p)

Где:
- I_compile = 1, если compile успешен, иначе 0;
- I_docsStyle = 1, если javadoc + checkstyle успешны, иначе 0;
- p = passed / (passed + failed + skipped), если тесты есть.

Итоговый балл задачи:

score = clamp(score_base - P, 0, M)

## Активность

Студент считается активным в учебную неделю, если сделал минимум один коммит за неделю.

- activeWeeks — число активных недель;
- totalWeeks — число недель между первой и последней неделями коммитов;
- activityRatio = activeWeeks / totalWeeks.

Влияние на финальную оценку:
- если activityRatio >= activityBonusThreshold, итоговая оценка повышается на 1 (до 5);
- если activityRatio < activityPenaltyThreshold, итоговая оценка понижается на 1 (не ниже 2).

## HTML-отчет

В отчете есть:
- по каждому студенту: баллы, итоговая оценка, активность;
- по каждой задаче: статус, score, compile/docs-style/tests, дата последнего коммита, комментарий;
- контрольные точки: баллы и оценка.

Отчет печатается в stdout. Для сохранения в файл:

```bash
./gradlew run --args='report config.groovy' > report.html
```

## Ограничения

- Работа с GitHub API не используется.
- Работа с git идет через системный git-клиент.
- Включен неинтерактивный режим git (без запросов логина/пароля во время запуска).
- Если нет доступа к репозиторию, это отражается в результате по студенту.

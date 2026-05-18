package ru.nsu.ermakov.report;

import ru.nsu.ermakov.checker.StudentCheckResult;
import ru.nsu.ermakov.checker.TaskCheckResult;
import ru.nsu.ermakov.entity.Config;
import ru.nsu.ermakov.entity.Task;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HtmlReportRenderer {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    /**
     * Рендерит HTML-отчёт по результатам проверки.
     */
    public String render(Config config, List<StudentCheckResult> results) {
        List<StudentCheckResult> safeResults = results == null ? List.of() : results;
        Map<String, List<StudentCheckResult>> groupedResults = groupByName(safeResults);

        StringBuilder html = new StringBuilder();
        html.append("""
                <!doctype html>
                <html lang="ru">
                <head>
                  <meta charset="UTF-8">
                  <title>OOP Course Report</title>
                  <style>
                    body { font-family: 'Segoe UI', Tahoma, sans-serif; margin: 24px; color: #1f2937; }
                    h1, h2, h3 { margin-bottom: 8px; }
                    .muted { color: #6b7280; }
                    .ok { color: #166534; font-weight: 600; }
                    .fail { color: #991b1b; font-weight: 600; }
                    .warn { color: #9a3412; font-weight: 600; }
                    .card { border: 1px solid #e5e7eb;
                             border-radius: 8px; padding: 16px; margin-bottom: 18px; }
                    table { border-collapse: collapse; width: 100%; margin-top: 10px; }
                    th, td { border: 1px solid #d1d5db; padding: 6px 8px;
                               text-align: left; vertical-align: top; }
                    th { background: #f3f4f6; }
                    .mono { font-family: ui-monospace, SFMono-Regular, Menlo, monospace; }
                  </style>
                </head>
                <body>
                """);

        html.append("<h1>Автоматическая проверка ООП</h1>\n");
        html.append(String.format("<p class=\"muted\">Всего студентов: %d</p>%n", safeResults.size()));

        if (config != null && config.getTasks() != null) {
            html.append(String.format("<p class=\"muted\">Задач в конфиге: %d</p>%n", config.getTasks().size()));
        }

        String scoringDescriptionPart1 = "Формула балла задачи: maxScore минус штраф за пропущенные дедлайны; ";
        String scoringDescriptionPart2 = "compile/docs/style/tests выводятся в таблице как индикаторы есть/нет и не уменьшают балл.";
        String scoringDescription = scoringDescriptionPart1 + scoringDescriptionPart2;
        html.append(String.format("<p class=\"muted\">%s</p>%n", escape(scoringDescription)));

        for (Map.Entry<String, List<StudentCheckResult>> entry : groupedResults.entrySet()) {
            renderGroup(html, entry.getKey(), entry.getValue(), scoringDescription);
        }

        html.append("</body>\n");
        html.append("</html>\n");
        return html.toString();
    }

    /**
     * Рендерит блок группы со списком студентов.
     */
    private void renderGroup(StringBuilder html, String groupName, List<StudentCheckResult> students, String scoringDescription) {
        html.append("<div class=\"card\">\n");
        html.append(String.format("<h2>Группа: %s</h2>%n", escape(groupName)));

        int scoreSum = 0;
        int maxSum = 0;
        for (StudentCheckResult studentResult : students) {
            scoreSum += studentResult.getTotalScore();
            maxSum += studentResult.getMaxScore();
        }

        html.append(String.format("<p class=\"muted\">Суммарный балл группы: %d / %d</p>%n", scoreSum, maxSum));

        for (StudentCheckResult studentResult : students) {
            renderStudent(html, studentResult, scoringDescription);
        }

        html.append("</div>\n");
    }

    /**
     * Рендерит карточку одного студента с деталями и таблицами.
     */
    private void renderStudent(StringBuilder html, StudentCheckResult result, String scoringDescription) {
        String fio = result.getStudent() == null ? "Unknown" : result.getStudent().getFio();
        String github = result.getStudent() == null ? "" : result.getStudent().getGithubNick();
        String repoUrl = result.getStudent() == null ? "" : result.getStudent().getRepoUrl();

        html.append("<div class=\"card\">\n");
        html.append(String.format("<h3>%s</h3>%n", escape(fio)));
        html.append(String.format("<p><b>GitHub:</b> <span class=\"mono\">@%s</span></p>%n", escape(github)));

        html.append(String.format("<p><b>Repo:</b> %s</p>%n", escape(repoUrl)));

        if (result.hasCloneError()) {
            html.append(String.format("<p class=\"fail\">Ошибка репозитория: %s</p>%n", escape(result.getCloneError())));
            html.append("</div>\n");
            return;
        }
        html.append(String.format("<p><b>Локальный путь:</b> <span class=\"mono\">%s</span></p>%n", escape(result.getRepoPath())));

        html.append(String.format("<p><b>Баллы:</b> %d / %d; <b>Итоговая оценка:</b> %s</p>%n",
            result.getTotalScore(), result.getMaxScore(), String.valueOf(result.getFinalGrade())));

        html.append(String.format("<p><b>Активность:</b> %d из %d недель (%s%%)</p>%n",
            result.getActiveWeeks(), result.getTotalWeeks(), String.format("%.0f", result.getActivityRatio() * 100)));

        renderTaskTable(html, result.getTaskResults(), scoringDescription);
        renderCheckpointTable(html, result);

        html.append("</div>\n");
    }

    /**
     * Рендерит таблицу с результатами по задачам для студента.
     */
    private void renderTaskTable(StringBuilder html, List<TaskCheckResult> taskResults, String scoringDescription) {
        html.append("<p class=\"muted\">").append(escape(scoringDescription)).append("</p>\n");
        html.append("<table>\n");
        html.append("<thead><tr>");
        html.append("<th>Задача</th>");
        html.append("<th>Статус</th>");
        html.append("<th>Score</th>");
        html.append("<th>Compile (есть/нет)</th>");
        html.append("<th>Docs+Style (есть/нет)</th>");
        html.append("<th>Тесты/покрытие (есть/нет)</th>");
        html.append("<th>Последний коммит</th>");
        html.append("<th>Комментарий</th>");
        html.append("</tr></thead>\n");
        html.append("<tbody>\n");

        if (taskResults == null || taskResults.isEmpty()) {
            html.append("<tr><td colspan=\"8\" class=\"warn\">Нет задач для проверки</td></tr>\n");
        } else {
            for (TaskCheckResult taskResult : taskResults) {
                Task task = taskResult.getTask();
                String taskName = task == null ? "unknown" : task.getName();
                String taskId = task == null ? "" : task.getId();
                String taskLabel = taskId == null || taskId.isBlank() || taskId.equals(taskName)
                        ? taskName
                        : taskId + " / " + taskName;
                int max = task == null ? 0 : task.getMaxScore();
                String statusClass = switch (taskResult.getStatus()) {
                    case PASSED -> "ok";
                    case FAILED -> "fail";
                    case SUBMITTED -> "warn";
                    case NOT_SUBMITTED -> "muted";
                };

                html.append("<tr>");
                html.append("<td class=\"mono\">").append(escape(taskLabel)).append("</td>");
                html.append("<td class=\"").append(statusClass).append("\">")
                    .append(escape(taskResult.getStatus().name()))
                    .append("</td>");
                html.append("<td>")
                    .append(taskResult.getScore())
                    .append("/")
                    .append(max)
                    .append("</td>");
                boolean hasTestsCoverage = taskResult.isTestsExecuted() && taskResult.totalTests() > 0;
                html.append("<td>").append(taskResult.isCompilePassed() ? "есть" : "нет").append("</td>");
                html.append("<td>").append(taskResult.isDocsStylePassed() ? "есть" : "нет").append("</td>");
                html.append("<td>").append(hasTestsCoverage ? "есть" : "нет").append("</td>");

                String commitDate = taskResult.getLastCommitDate() == null
                    ? "-"
                    : DATE_TIME_FORMATTER.format(taskResult.getLastCommitDate());
                html.append("<td>").append(escape(commitDate)).append("</td>");
                html.append("<td>").append(escape(taskResult.getNote())).append("</td>");
                html.append("</tr>\n");
            }
        }

        html.append("</tbody>\n");
        html.append("</table>\n");
    }

    /**
     * Рендерит таблицу с результатами по контрольным точкам.
     */
    private void renderCheckpointTable(StringBuilder html, StudentCheckResult result) {
        if (result.getCheckpointScores().isEmpty()) {
            return;
        }

        html.append("<table>\n");
        html.append("<thead><tr><th>Контрольная точка</th><th>Баллы</th><th>Оценка</th></tr></thead>\n");
        html.append("<tbody>\n");

        for (Map.Entry<String, Integer> checkpoint : result.getCheckpointScores().entrySet()) {
            String name = checkpoint.getKey();
            int score = checkpoint.getValue();
            Integer grade = result.getCheckpointGrades().get(name);

            html.append("<tr>");
            html.append("<td>").append(escape(name)).append("</td>");
            html.append("<td>").append(score).append("</td>");
            html.append("<td>").append(grade == null ? "-" : grade).append("</td>");
            html.append("</tr>\n");
        }

        html.append("</tbody>\n");
        html.append("</table>\n");
    }

    /**
     * Группирует результаты по имени группы.
     */
    private Map<String, List<StudentCheckResult>> groupByName(List<StudentCheckResult> results) {
        Map<String, List<StudentCheckResult>> grouped = new LinkedHashMap<>();
        for (StudentCheckResult result : results) {
            String groupName = result.getGroupName() == null || result.getGroupName().isBlank()
                ? "unknown-group"
                : result.getGroupName();
            grouped.computeIfAbsent(groupName, ignored -> new ArrayList<>()).add(result);
        }
        return grouped;
    }

    /**
     * Экранирует специальные символы в тексте для вставки в HTML.
     */
    private String escape(String text) {
        if (text == null) {
            return "";
        }
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }

}

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

    public String render(Config config, List<StudentCheckResult> results) {
        List<StudentCheckResult> safeResults = results == null ? List.of() : results;
        Map<String, List<StudentCheckResult>> groupedResults = groupByName(safeResults);

        StringBuilder html = new StringBuilder();
        html.append("<!doctype html>\n");
        html.append("<html lang=\"ru\">\n");
        html.append("<head>\n");
        html.append("  <meta charset=\"UTF-8\">\n");
        html.append("  <title>OOP Course Report</title>\n");
        html.append("  <style>\n");
        html.append("    body { font-family: 'Segoe UI', Tahoma, sans-serif; margin: 24px; color: #1f2937; }\n");
        html.append("    h1, h2, h3 { margin-bottom: 8px; }\n");
        html.append("    .muted { color: #6b7280; }\n");
        html.append("    .ok { color: #166534; font-weight: 600; }\n");
        html.append("    .fail { color: #991b1b; font-weight: 600; }\n");
        html.append("    .warn { color: #9a3412; font-weight: 600; }\n");
        html.append("    .card { border: 1px solid #e5e7eb; border-radius: 8px; padding: 16px; margin-bottom: 18px; }\n");
        html.append("    table { border-collapse: collapse; width: 100%; margin-top: 10px; }\n");
        html.append("    th, td { border: 1px solid #d1d5db; padding: 6px 8px; text-align: left; vertical-align: top; }\n");
        html.append("    th { background: #f3f4f6; }\n");
        html.append("    .mono { font-family: ui-monospace, SFMono-Regular, Menlo, monospace; }\n");
        html.append("  </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");

        html.append("<h1>Автоматическая проверка ООП</h1>\n");
        html.append("<p class=\"muted\">Всего студентов: ").append(safeResults.size()).append("</p>\n");

        if (config != null && config.getTasks() != null) {
            html.append("<p class=\"muted\">Задач в конфиге: ").append(config.getTasks().size()).append("</p>\n");
        }

        String scoringDescription = "Формула балла задачи: maxScore минус штраф за пропущенные дедлайны; "
                + "compile/docs/style/tests выводятся в таблице как индикаторы есть/нет и не уменьшают балл.";
        html.append("<p class=\"muted\">").append(escape(scoringDescription)).append("</p>\n");

        for (Map.Entry<String, List<StudentCheckResult>> entry : groupedResults.entrySet()) {
            renderGroup(html, entry.getKey(), entry.getValue(), scoringDescription);
        }

        html.append("</body>\n");
        html.append("</html>\n");
        return html.toString();
    }

    private void renderGroup(StringBuilder html, String groupName, List<StudentCheckResult> students, String scoringDescription) {
        html.append("<div class=\"card\">\n");
        html.append("<h2>Группа: ").append(escape(groupName)).append("</h2>\n");

        int scoreSum = 0;
        int maxSum = 0;
        for (StudentCheckResult studentResult : students) {
            scoreSum += studentResult.getTotalScore();
            maxSum += studentResult.getMaxScore();
        }

        html.append("<p class=\"muted\">Суммарный балл группы: ")
            .append(scoreSum)
            .append(" / ")
            .append(maxSum)
            .append("</p>\n");

        for (StudentCheckResult studentResult : students) {
            renderStudent(html, studentResult, scoringDescription);
        }

        html.append("</div>\n");
    }

    private void renderStudent(StringBuilder html, StudentCheckResult result, String scoringDescription) {
        String fio = result.getStudent() == null ? "Unknown" : result.getStudent().getFio();
        String github = result.getStudent() == null ? "" : result.getStudent().getGithubNick();
        String repoUrl = result.getStudent() == null ? "" : result.getStudent().getRepoUrl();

        html.append("<div class=\"card\">\n");
        html.append("<h3>").append(escape(fio)).append("</h3>\n");
        html.append("<p><b>GitHub:</b> <span class=\"mono\">@")
            .append(escape(github))
            .append("</span></p>\n");

        html.append("<p><b>Repo:</b> ").append(escape(repoUrl)).append("</p>\n");

        if (result.hasCloneError()) {
            html.append("<p class=\"fail\">Ошибка репозитория: ")
                .append(escape(result.getCloneError()))
                .append("</p>\n");
            html.append("</div>\n");
            return;
        }

        html.append("<p><b>Локальный путь:</b> <span class=\"mono\">")
            .append(escape(result.getRepoPath()))
            .append("</span></p>\n");

        html.append("<p><b>Баллы:</b> ")
            .append(result.getTotalScore())
            .append(" / ")
            .append(result.getMaxScore())
            .append("; <b>Итоговая оценка:</b> ")
            .append(result.getFinalGrade())
            .append("</p>\n");

        html.append("<p><b>Активность:</b> ")
            .append(result.getActiveWeeks())
            .append(" из ")
            .append(result.getTotalWeeks())
            .append(" недель (")
            .append(String.format("%.0f", result.getActivityRatio() * 100))
            .append("%)</p>\n");

        renderTaskTable(html, result.getTaskResults(), scoringDescription);
        renderCheckpointTable(html, result);

        html.append("</div>\n");
    }

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

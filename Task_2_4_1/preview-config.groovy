groups {
    group {
        name = "preview"
        students = [
            [fio: "Preview Student", githubNick: "preview", repoUrl: "file:///home/zxcolder/Desktop/OOP/OOP/Task_2_4_1"]
        ]
    }
}

tasks {
    task {
        id = "Task_2_4_1"
        title = "Course Checker"
        maxScore = 1
        softDeadline = null
        hardDeadline = null
    }
}

checkpoints {
    checkpoint {
        name = "Preview"
        date = "31-12-2026"
    }
}

settings {
    buildTimeoutSeconds = 120
    gitTimeoutSeconds = 30
    compilePart = 0.5
    docsStylePart = 0.2
    testsPart = 0.3
    deadlineMissPenalty = 0.5
    maxDeadlinePenalty = 1.0
    excellentThreshold = 85.0
    goodThreshold = 70.0
    satisfactoryThreshold = 50.0
    activityBonusThreshold = 0.60
    activityPenaltyThreshold = 0.30
}

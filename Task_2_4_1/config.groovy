include "groups.groovy"

tasks {
    task {
        name = "Task_1_1_1"
        softDeadline = "15-09-2025"
        hardDeadline = "15-09-2025"
        maxScore = 1
    }
    task {
        name = "Task_1_1_2"
        softDeadline = "22-09-2025"
        hardDeadline = "29-09-2025"
        maxScore = 2
    }
    task {
        name = "Task_1_1_3"
        softDeadline = "06-10-2025"
        hardDeadline = "13-10-2025"
        maxScore = 2
    }
    task {
        name = "Task_1_2_1"
        softDeadline = "20-10-2025"
        hardDeadline = "27-10-2025"
        maxScore = 2
    }
    task {
        name = "Task_1_2_2"
        softDeadline = "10-11-2025"
        hardDeadline = "17-11-2025"
        maxScore = 2
    }
    task {
        name = "Task_1_3_1"
        softDeadline = "22-11-2025"
        hardDeadline = "22-11-2025"
        maxScore = 2
    }
    task {
        name = "Task_1_4_1"
        softDeadline = null
        hardDeadline = "30-11-2025"
        maxScore = 1
    }
    task {
        name = "Task_1_5_1"
        softDeadline = "15-12-2025"
        hardDeadline = "27-12-2025"
        maxScore = 4
    }
    task {
        name = "Task_2_1_1"
        softDeadline = null
        hardDeadline = null
        maxScore = 1
    }
    task {
        name = "Task_2_2_1"
        softDeadline = null
        hardDeadline = null
        maxScore = 1
    }
    task {
        name = "Task_2_3_1"
        softDeadline = null
        hardDeadline = null
        maxScore = 1
    }
    task {
        name = "Task_2_4_1"
        softDeadline = null
        hardDeadline = null
        maxScore = 1
    }
    task {
        name = "Task_2_1_2"
        softDeadline = null
        hardDeadline = null
        maxScore = 1
    }
}

checkpoints {
    checkpoint {
        name = "Midterm"
        date = "01-11-2025"
    }
    checkpoint {
        name = "Final"
        date = "31-12-2025"
    }
}

settings {
    buildTimeoutSeconds = 300
    gitTimeoutSeconds = 60

    // Components of task score (normalized automatically if sum != 1.0)
    compilePart = 1.00
    docsStylePart = 0.00
    testsPart = 0.00

    deadlineMissPenalty = 0.50
    maxDeadlinePenalty = 1.00

    // Grade thresholds in percent
    excellentThreshold = 85.0
    goodThreshold = 70.0
    satisfactoryThreshold = 50.0

    // Activity influence on final grade
    activityBonusThreshold = 0.60
    activityPenaltyThreshold = 0.30
    semester = 1
}
 
// Example: explicit selection of students/tasks for checking.
// If this block is omitted, checker runs all tasks for all students.
// assignments {
//     assignment {
//         githubNick = "zxcolder159"
//         tasks = ["Task_2_4_1"]
//     }
// }


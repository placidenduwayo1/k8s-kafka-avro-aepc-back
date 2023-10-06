package com.placide.k8skafkaavroaepccleanarchibsmicrosproject.domain.beans.project;

public enum Priority {
    P1(1),
    P2(2),
    P3(3),
    P4(4);
    private final int projectPriority;

    Priority(int projectPriority) {
        this.projectPriority = projectPriority;
    }

    public int getProjectPriority() {
        return projectPriority;
    }
}

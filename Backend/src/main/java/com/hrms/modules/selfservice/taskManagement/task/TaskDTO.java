package com.hrms.modules.selfservice.taskManagement.task;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TaskDTO {

    private Long id;
    private String taskName;

    private String taskDescription;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date taskStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date taskEndDate;

    private String taskStatus;

    private Long taskAssignedToId;
    private String taskAssignedToLoginCode;
    private String taskAssignedToName;

    private Long taskAssignedById;
    private String taskAssignedByLoginCode;
    private String taskAssignedByName;

    private Date creationDateTime;
    private Date lastUpdateDateTime;
    private String creationUser;
    private String lastUpdateUser;

    public TaskDTO() {
    }

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.taskName = task.getTaskName();
        this.taskDescription = task.getTaskDescription();
        this.taskStartDate = task.getTaskStartDate();
        this.taskEndDate = task.getTaskEndDate();
        this.taskStatus = task.getTaskStatus();
        this.taskAssignedToId = task.getTaskAssignedTo() != null ? task.getTaskAssignedTo().getId() : null;
        this.taskAssignedToName = task.getTaskAssignedTo() != null ? task.getTaskAssignedTo().getDisplayName() : null;
        this.taskAssignedToLoginCode = task.getTaskAssignedTo() != null ? task.getTaskAssignedTo().getLoginCode() : null;
//        this.taskAssignedById = task.getTaskAssignedBy() != null ? task.getTaskAssignedBy().getId() : null;
//        this.taskAssignedByName = task.getTaskAssignedBy() != null ? task.getTaskAssignedBy().getDisplayName() : null;
        this.taskAssignedByLoginCode = task.getTaskAssignedBy() != null ? task.getTaskAssignedBy().getLoginCode() : null;
        this.creationDateTime = task.getCreationDateTime();
        this.lastUpdateDateTime = task.getLastUpdateDateTime();
        this.creationUser = task.getCreationUser();
        this.lastUpdateUser = task.getLastUpdateUser();
    }


}

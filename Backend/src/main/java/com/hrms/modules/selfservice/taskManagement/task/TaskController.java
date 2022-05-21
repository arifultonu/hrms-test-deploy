package com.hrms.modules.selfservice.taskManagement.task;


import com.hrms.util.PaginatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin("*")
@RequestMapping("/task")
public class TaskController {


    @Autowired
    private TaskService taskService;

    public Map<String, String> clientParams;

    // create a task
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    // get task list
    @GetMapping("/getTaskList")
    public ResponseEntity<?> getAllPaginatedResponse(HttpServletRequest request,
                                                     @RequestParam Map<String, String> clientParams) {
        this.clientParams = clientParams;
        PaginatorService ps = new PaginatorService(request);
        Page<TaskDTO> page = this.taskService.getAllPaginatedLists(this.clientParams, ps.pageNum, ps.pageSize, ps.sortField, ps.sortDir);
        List<TaskDTO> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (ps.sortDir.equals("asc") ? "desc" : "asc"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //update task
    @PutMapping("/update")
    public ResponseEntity<?> updateTask(@RequestBody TaskDTO taskDTO) {
        return taskService.updateTask(taskDTO);
    }

    // get task by id
    @GetMapping("/getTaskById/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long taskId) {
        return taskService.getTaskById(taskId);
    }

    //delete by id
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<?> deleteTaskById(@PathVariable Long taskId) {
        return taskService.deleteTaskById(taskId);
    }

}

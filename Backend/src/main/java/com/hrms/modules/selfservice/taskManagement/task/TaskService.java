package com.hrms.modules.selfservice.taskManagement.task;

import com.hrms.acl.auth.repository.UserRepository;
import com.hrms.dto.response.MessageResponse;
import com.hrms.modules.hris.repository.HrCrEmpRepository;
import com.hrms.util.user.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.Map;


@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private HrCrEmpRepository hrCrEmpRepository;

    @Autowired
    private UserRepository userRepository;

    public Task setAttributeForCreateUpdate(Task entityInst, String activeOperation) {

        if (activeOperation.equals("Create")) {
            entityInst.setCreationDateTime(new Date());
            entityInst.setCreationUser(UserUtil.getLoginUser());

        } else if (activeOperation.equals("Update")) {
            entityInst.setLastUpdateDateTime(new Date());
            entityInst.setLastUpdateUser(UserUtil.getLoginUser());
        }
        return entityInst;

    }

    public ResponseEntity<?> createTask(Task createEntity) {
        // call setAttributeForCreateUpdate
        createEntity = this.setAttributeForCreateUpdate(createEntity, "Create");


        //check employee is available or not
        if (!hrCrEmpRepository.findById(createEntity.getTaskAssignedBy().getId()).isPresent()) {
            createEntity.setTaskAssignedBy(null);
        }

        taskRepository.save(createEntity);
        return new ResponseEntity<>(new MessageResponse("Task created successfully", true), HttpStatus.OK);
    }

    public ResponseEntity<?> getTaskList() {
        return new ResponseEntity<>(taskRepository.findAll(), HttpStatus.OK);
    }

    public Page<TaskDTO> getAllPaginatedLists(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<Task> entities = taskRepository.findAll((Specification<Task>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (!clientParams.isEmpty()) {

                if (clientParams.containsKey("code")) {
                    if (StringUtils.hasLength(clientParams.get("code"))) {
                        p = cb.and(p, cb.equal(root.get("code"), clientParams.get("code")));
                    }
                }
                if(clientParams.containsKey("taskAssignedToId")){
                    if (StringUtils.hasLength(clientParams.get("taskAssignedToId"))) {
                        if(!clientParams.get("taskAssignedToId").equals("null")){
                            p = cb.and(p, cb.equal(root.get("taskAssignedTo").get("id"), clientParams.get("taskAssignedToId")));
                        }else {
                            if (clientParams.containsKey("taskAssignedById")) {
                                if (StringUtils.hasLength(clientParams.get("taskAssignedById"))) {
                                    if (!clientParams.get("taskAssignedById").equals("null")) {
                                        p = cb.and(p, cb.equal(root.get("taskAssignedBy").get("id"), clientParams.get("taskAssignedById")));
                                    }
                                }
                            }
                        }
                    }
                }
//                if(clientParams.containsKey("taskAssignedById")){
//                    if (StringUtils.hasLength(clientParams.get("taskAssignedById"))) {
//                        if(!clientParams.get("taskAssignedById").equals("null")){
//                            p = cb.and(p, cb.equal(root.get("taskAssignedBy").get("id"), clientParams.get("taskAssignedById")));
//                        }
//
//                    }
//                }
            }
            return p;
        }, pageable);

        return entities.map(TaskDTO::new);
    }

    public ResponseEntity<?> getTaskById(Long taskId) {
        if (taskRepository.findById(taskId).isPresent()) {
            TaskDTO taskDTO = new TaskDTO(taskRepository.findById(taskId).get());
            return new ResponseEntity<>(new MessageResponse("Task found", true, taskDTO), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("Task not found", false), HttpStatus.OK);

    }

    public ResponseEntity<?> deleteTaskById(Long taskId) {
        if (taskRepository.findById(taskId).isPresent()) {
            taskRepository.deleteById(taskId);
            return new ResponseEntity<>(new MessageResponse("Task deleted successfully", true), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("Task not found", false), HttpStatus.OK);
    }

    public ResponseEntity<?> updateTask(TaskDTO taskDTO) {
        if (taskRepository.findById(taskDTO.getId()).isPresent()) {
            Task task = taskRepository.findById(taskDTO.getId()).get();
            // call setAttributeForCreateUpdate
            this.setAttributeForCreateUpdate(task, "Update");

            task.setId(taskDTO.getId());
            task.setTaskName(taskDTO.getTaskName());
            task.setTaskDescription(taskDTO.getTaskDescription());
//            task.setTaskAssignedBy(hrCrEmpRepository.findById(taskDTO.getTaskAssignedById()).isPresent() ?
//                    hrCrEmpRepository.findById(taskDTO.getTaskAssignedById()).get() : null);
            task.setTaskAssignedTo(hrCrEmpRepository.findById(taskDTO.getTaskAssignedToId()).isPresent() ?
                    hrCrEmpRepository.findById(taskDTO.getTaskAssignedToId()).get() : null);
            task.setTaskStatus(taskDTO.getTaskStatus());
            task.setTaskStartDate(taskDTO.getTaskStartDate());
            task.setTaskEndDate(taskDTO.getTaskEndDate());

            taskRepository.save(task);
            return new ResponseEntity<>(new MessageResponse("Task updated successfully", true), HttpStatus.OK);
        }
        return new ResponseEntity<>(new MessageResponse("Task not found", false), HttpStatus.OK);
    }

}

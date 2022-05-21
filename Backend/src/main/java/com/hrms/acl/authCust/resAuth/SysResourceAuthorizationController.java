package com.hrms.acl.authCust.resAuth;


import com.hrms.acl.auth.entity.Role;
import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.service.UserService;
import com.hrms.exception.CustomException;
import com.hrms.util.PaginatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sysAuth")
@CrossOrigin("*")
public class SysResourceAuthorizationController {

    @Autowired
    private SysResourceAuthorizationService service;
    @Autowired
    private UserService userService;

    public Map<String, String> clientParams;

    /** Creating Instance api*/
    @PostMapping(value = "/create")
    public SysResourceAuthorization create(@RequestBody SysResourceAuthorization entity) throws CustomException {
        return this.service.create(entity);
    }

    @GetMapping(value = "/get")
    ResponseEntity<Map<String, Object>> getAllPaginatedSysAuth(HttpServletRequest request, @RequestParam Map<String,String> clientParams){
        this.clientParams = clientParams;
        PaginatorService pSrv = new PaginatorService(request);
        Page<SysResourceAuthorization> page = this.service.getAllPaginatedSysAuth(this.clientParams,pSrv.pageNum, pSrv.pageSize, pSrv.sortField, pSrv.sortDir);
        List<SysResourceAuthorization> listData = page.getContent();

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", listData);
        response.put("currentPage", page.getNumber());
        response.put("totalPages", page.getTotalPages());
        response.put("totalItems", page.getTotalElements());
        response.put("reverseSortDir", (pSrv.sortDir.equals("asc") ? "desc" : "asc"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping(value = "/find/{id}")
    ResponseEntity<Map<String, Object>> getSysAuthObj( @PathVariable Long id){

        SysResourceAuthorization sysResAuth = this.service.findById(id);
        // prepare response
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> refFields = new HashMap<>();

        if(sysResAuth != null){

            response.put("id", sysResAuth.id);
            response.put("username",sysResAuth.getUsername());
            response.put("systemResourceName", sysResAuth.getSystemResourceName());
            response.put("createAuth", sysResAuth.getCreateAuth());
            response.put("readAuth",sysResAuth.getReadAuth());
            response.put("updateAuth",sysResAuth.getUpdateAuth());
            response.put("deleteAuth",sysResAuth.getDeleteAuth());
            response.put("queryAuth",sysResAuth.getQueryAuth());
            response.put("submitAuth",sysResAuth.getSubmitAuth());
            response.put("crudqsString",sysResAuth.getCrudqsString());
            response.put("othersString",sysResAuth.getOthersString());
            response.put("fullPrivilegeString",sysResAuth.getFullPrivilegeString());
            response.put("visibleToAll",sysResAuth.getVisibleToAll());

            // process ref object fields
            String authUsername = sysResAuth.getUsername();
            User authUser = this.userService.getUserByUsername(authUsername);
            if(authUser != null){
                // --------- for username field start
                Map<String, Object> userRefObj = new HashMap<>();
                userRefObj.put("username", authUser.getUsername());
                userRefObj.put("userTitle", authUser.getUserTitle());
                ArrayList<Object> userRefList=new ArrayList<Object>();//creating new generic arraylist
                userRefList.add(userRefObj);
                refFields.put("username", userRefList);
                // --------- for username field end


//                // --------- for role field start
                Role roleObj = sysResAuth.getRole();
                if (roleObj!=null){
                    Map<String, Object> roleRefObj = new HashMap<>();
                    roleRefObj.put("roleName", sysResAuth.getRole().getRoleName());
                    ArrayList<Object> roleRefList=new ArrayList<Object>();//creating new generic arraylist
                    roleRefList.add(roleRefObj);
                    refFields.put("role", roleRefList);
                }
//                // --------- for username field end

                response.put("refFields", refFields);

            }
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/get/{sysDefId}")
    public List<SysResourceAuthorization> getById(@PathVariable(name = "sysDefId") Long sysDefId) throws CustomException {
        return service.getById(sysDefId);
    }

    @PutMapping(value = "/update")
    public SysResourceAuthorization update(@RequestBody SysResourceAuthorization entity) throws CustomException {
        return this.service.update(entity);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete (@PathVariable(name = "id") Long id) throws CustomException {
        return this.service.delete(id);
    }


}

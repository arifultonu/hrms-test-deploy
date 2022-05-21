package com.hrms.acl.authCust.resAuth;


import com.hrms.acl.authCust.resDef.SysResourceDefinition;
import com.hrms.acl.authCust.resDef.SysResourceDefinitionRepository;
import com.hrms.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SysResourceAuthorizationService {

    @Autowired
    private SysResourceAuthorizationRepository repository;

    @Autowired
    private SysResourceDefinitionRepository definitionRepository;


    public SysResourceAuthorization create(SysResourceAuthorization entity) throws CustomException {
        SysResourceDefinition definitionInst = definitionRepository.findById(entity.getSystemResource().getId()).get();
        SysResourceAuthorization authorizationInst = repository.findBySystemResourceAndUsername(definitionInst,entity.getUsername());
        if(authorizationInst!=null){
            throw new CustomException("Already assigned");
        }
        entity.setSystemResourceName(definitionInst.getEntityName());
        // Generating C R U D... privilege string......
        String vCRUDSString="";

        String setAuthC = ( entity.getCreateAuth() == null || entity.getCreateAuth().equals("") )  ?  "-" : "C";
        vCRUDSString += setAuthC;
        String setAuthR = ( entity.getReadAuth() == null || entity.getReadAuth().equals("") )  ?  "-" : "R";
        vCRUDSString += setAuthR;
        String setAuthU = ( entity.getUpdateAuth() == null || entity.getUpdateAuth().equals("") )  ?  "-" : "U";
        vCRUDSString += setAuthU;
        String setAuthD = ( entity.getDeleteAuth() == null || entity.getDeleteAuth().equals("") )  ?  "-" : "D";
        vCRUDSString += setAuthD;
        String setAuthQ = ( entity.getQueryAuth() == null || entity.getQueryAuth().equals("") )  ?  "-" : "Q";
        vCRUDSString += setAuthQ;
        String setAuthS = ( entity.getSubmitAuth() == null || entity.getSubmitAuth().equals("") )  ?  "-" : "S";
        vCRUDSString += setAuthS;

        entity.setCrudqsString(vCRUDSString);

        return repository.save(entity);
    }


    public SysResourceAuthorization findById( Long id ){
        Optional<SysResourceAuthorization> entity = this.repository.findById(id);
        if(entity.isPresent()){
            return entity.get();
        }
        return null;
    }

    public Page<SysResourceAuthorization> getAllPaginatedSysAuth(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        return this.repository.findAll((Specification<SysResourceAuthorization>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("id")){
                    if (StringUtils.hasLength(clientParams.get("id"))) {
                        p = cb.and(p, cb.equal(root.get("id"), clientParams.get("id")));
                    }
                }
                if(clientParams.containsKey("systemResourceName")){
                    if (StringUtils.hasLength(clientParams.get("systemResourceName"))) {
                        p = cb.and(p, cb.like(root.get("systemResourceName"), "%"+clientParams.get("systemResourceName")+"%"));
                    }
                }
                if(clientParams.containsKey("systemResourceId")){
                    if (StringUtils.hasLength(clientParams.get("systemResourceId"))) {
                        SysResourceDefinition sysResDefInst = definitionRepository.findById(Long.parseLong(clientParams.get("systemResourceId"))).get();
                        p = cb.and(p, cb.equal(root.get("systemResource"), sysResDefInst));
                    }
                }
                return p;
            }
            return null;
        }, pageable);

    }

    public List<SysResourceAuthorization> getById(Long sysDefId) throws CustomException {
        SysResourceDefinition resourceDefinitionInst = definitionRepository.findById(sysDefId).orElseThrow(()
                -> new CustomException("Resource Definition not found for this id :: " + sysDefId));

        return repository.findBySystemResource(resourceDefinitionInst);
    }

    public SysResourceAuthorization update(SysResourceAuthorization entity) throws CustomException {

        // Generating C R U D... privilege string......
        String vCRUDSString="";

        String setAuthC = ( entity.getCreateAuth() == null || entity.getCreateAuth().equals("") )  ?  "-" : "C";
        vCRUDSString += setAuthC;
        String setAuthR = ( entity.getReadAuth() == null || entity.getReadAuth().equals("") )  ?  "-" : "R";
        vCRUDSString += setAuthR;
        String setAuthU = ( entity.getUpdateAuth() == null || entity.getUpdateAuth().equals("") )  ?  "-" : "U";
        vCRUDSString += setAuthU;
        String setAuthD = ( entity.getDeleteAuth() == null || entity.getDeleteAuth().equals("") )  ?  "-" : "D";
        vCRUDSString += setAuthD;
        String setAuthQ = ( entity.getQueryAuth() == null || entity.getQueryAuth().equals("") )  ?  "-" : "Q";
        vCRUDSString += setAuthQ;
        String setAuthS = ( entity.getSubmitAuth() == null || entity.getSubmitAuth().equals("") )  ?  "-" : "S";
        vCRUDSString += setAuthS;


        SysResourceAuthorization resourceAuthInst = repository.findById(entity.getId()).orElseThrow(()
                -> new CustomException("Resource Auth not found for this id :: " + entity.getId()));
        resourceAuthInst.setSystemResource(entity.getSystemResource());
        resourceAuthInst.setSystemResourceName(entity.getSystemResourceName());

        resourceAuthInst.setCreateAuth(entity.getCreateAuth());
        resourceAuthInst.setReadAuth(entity.getReadAuth());
        resourceAuthInst.setUpdateAuth(entity.getUpdateAuth());
        resourceAuthInst.setDeleteAuth(entity.getDeleteAuth());
        resourceAuthInst.setQueryAuth(entity.getQueryAuth());
        resourceAuthInst.setSubmitAuth(entity.getSubmitAuth());

        resourceAuthInst.setOthersString(entity.getOthersString());
        resourceAuthInst.setFullPrivilegeString(entity.getFullPrivilegeString());
        resourceAuthInst.setVisibleToAll(entity.getVisibleToAll());
        resourceAuthInst.setUsername(entity.getUsername());
        resourceAuthInst.setRole(entity.getRole());
        resourceAuthInst.setCrudqsString(vCRUDSString);

        return this.repository.save(resourceAuthInst);
    }


    public ResponseEntity<?> delete(Long id) throws CustomException {
        SysResourceAuthorization sysResAuthInst = repository.findById(id).orElseThrow(()
                -> new CustomException("Resource Auth not found for this id :: " + id));
        repository.delete(sysResAuthInst);
        return ResponseEntity.ok(new CustomException("Successfully Deleted Data"));
    }
}

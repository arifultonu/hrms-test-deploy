package com.hrms.service.common;

import com.hrms.acl.auth.entity.User;
import com.hrms.acl.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("unchecked")
public class SystemCommonService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManagerFactory efm;


    public Map<String, Object> getAllPaginatedUserData(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<User> pageUser = this.userRepository.findAll((Specification<User>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();

            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("username")){
                    if (StringUtils.hasLength(clientParams.get("username"))) {
                        p = cb.and(p, cb.like(root.get("username"), "%" + clientParams.get("username") + "%"));
                    }
                }
                return p;
            }

            return null;
        }, pageable);


        // process
        List<User> users = pageUser.getContent();

        ArrayList<Map<String, Object>> userList = new ArrayList<>();
        users.forEach( user -> {
            Map<String, Object> userObj = new HashMap<>();
            userObj.put("username", user.getUsername());
            userObj.put("userTitle", user.getUserTitle());
            userList.add(userObj);
        });
        Map<String, Object> response = new HashMap<>();
        response.put("objectList", userList);
        response.put("currentPage", pageUser.getNumber());
        response.put("totalPages", pageUser.getTotalPages());
        response.put("totalItems", pageUser.getTotalElements());
        return response;

    }



    public Integer countEmpData( Map<String, String> clientParams ) {

        String q = clientParams.get("hrCrEmp");
        String qq = (q != null) ? "'" + q + "'" : null;

        String countSql = "" +
                "SELECT\n" +
                "    COUNT(*)\n" +
                "FROM\n" +
                "    HR_CR_EMP\n" +
                "WHERE\n" +
                "    ( 1 = 1 )\n" +
                "    AND ( ( "+qq+" IS NULL ) OR ((CODE = '"+q+"') OR (DISPLAY_NAME LIKE '%"+q+"%')) )\n" +
                "    AND ( '2' = '2' )\n";

        return jdbcTemplate.queryForObject(countSql, Integer.class);

    }
    public Integer countData(String tableName, Map<String, String> clientParams) {

        String q = clientParams.get("hrCrEmp");
        String qq = (q != null) ? "'" + q + "'" : null;

        String countSql = "" +
                "SELECT\n" +
                "    COUNT(*)\n" +
                "FROM\n" +
                "    "+tableName+"\n" +
                "WHERE\n" +
                "    ( 1 = 1 )\n" +
                "    AND ( '2' = '2' )\n";

        return jdbcTemplate.queryForObject(countSql, Integer.class);

    }

    public int getTotalPages( long total, long size ) {
        return size == 0 ? 1 : (int) Math.ceil((double) total / (double) size);
    }

    public Map<String, Object> getAllPaginatedEmpData(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        EntityManager em = efm.createEntityManager();
        System.out.println(clientParams);

        int offset = (pageNum-1) * pageSize;
        String q = clientParams.get("hrCrEmp");

        String queryString = "SELECT ID AS DDL_CODE, CODE || ' - ' || DISPLAY_NAME AS DDL_DESCRIPTION, CODE AS DDL_EMP_CODE FROM HR_CR_EMP WHERE (1=1)";
        if(q != null) queryString += " AND ((CODE = '"+q+"') OR (DISPLAY_NAME LIKE '%"+q+"%'))" ;
        log.info(queryString);

        Query query = em.createNativeQuery(queryString, Tuple.class);
        List<Tuple> listData = query.setFirstResult(offset).setMaxResults(pageSize).getResultList();
        em.close();


        List<Map<String, Object>> dataList = new ArrayList<>();
        listData.forEach( record-> {

            Long ddlCode = Long.parseLong( record.get("DDL_CODE").toString() );
            String ddlDescription = (record.get("DDL_DESCRIPTION") != null) ? record.get("DDL_DESCRIPTION").toString() : "";
            String ddlEmpCode = (record.get("DDL_EMP_CODE") != null) ? record.get("DDL_EMP_CODE").toString() : "";

            Map<String, Object> empBean = new HashMap<>();
            empBean.put("ddlCode", ddlCode );
            empBean.put("ddlDescription", ddlDescription);
            empBean.put("ddlEmpCode", ddlEmpCode );
            dataList.add(empBean);

        });

        Integer total = this.countEmpData(clientParams);

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", dataList);
        response.put("currentPage", pageNum);
        response.put("totalPages", this.getTotalPages( total, pageSize) );
        response.put("totalItems", total);
        return response;

    }


    public Map<String, Object> getAllPaginatedAlkpData(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        System.out.println(clientParams);

        int offset = (pageNum-1) * pageSize;
        String q = clientParams.get("keyword");

        String sqlString = "" +
                "SELECT\n" +
                "    ID,\n" +
                "    CODE,\n" +
                "    TITLE\n" +
                "    FROM ALKP\n" +
                "WHERE\n" +
                "PARENT_ID = (SELECT ID FROM ALKP WHERE KEYWORD ='"+q+"') \n" +
                "ORDER BY\n" +
                "    ID\n" +
                "OFFSET "+ offset +" ROWS FETCH NEXT "+ pageSize +" ROWS ONLY";

        log.info(sqlString);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sqlString);

        // process
        List<Map<String, Object>> alkpDataList = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            Map<String, Object> mapBean = new HashMap<>();
            mapBean.put("id", row.get("ID"));
            mapBean.put("title", row.get("TITLE"));
            mapBean.put("code", row.get("CODE"));
            alkpDataList.add(mapBean);
        }
        Integer total = this.countData("ALKP",clientParams);

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", alkpDataList);
        response.put("currentPage", pageNum);
        response.put("totalPages", this.getTotalPages( total, pageSize) );
        response.put("totalItems", total);
        return response;

    }

    public Map<String, Object> getAllPaginatedPrlElmData(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {
        EntityManager em = efm.createEntityManager();
        System.out.println(clientParams);

        int offset = (pageNum-1) * pageSize;
        String q = clientParams.get("payrollElementDef");

        String queryString = "SELECT ID, CODE , TITLE FROM PRL_PAYROLL_ELEMENT WHERE (IS_ACTIVE = TRUE)";
        if(q != null) queryString += " AND ((CODE = '"+q+"') OR (TITLE LIKE '%"+q+"%'))" ;
        log.info(queryString);

        Query query = em.createNativeQuery(queryString, Tuple.class);
        List<Tuple> listData = query.setFirstResult(offset).setMaxResults(pageSize).getResultList();
        em.close();


        List<Map<String, Object>> dataList = new ArrayList<>();
        listData.forEach( record-> {

            Long id = Long.parseLong( record.get("ID").toString() );
            String code = (record.get("CODE").toString() != null) ? record.get("CODE").toString() : "";
            String title = (record.get("TITLE") != null) ? record.get("TITLE").toString() : "";

            Map<String, Object> empBean = new HashMap<>();
            empBean.put("id", id );
            empBean.put("code", code );
            empBean.put("title", title);
            dataList.add(empBean);

        });

        Integer total = this.countData("PRL_PAYROLL_ELEMENT",clientParams);

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", dataList);
        response.put("currentPage", pageNum);
        response.put("totalPages", this.getTotalPages( total, pageSize) );
        response.put("totalItems", total);
        return response;
    }

    public Map<String, Object> getAllPaginatedAllOrgMst(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        EntityManager em = efm.createEntityManager();
        System.out.println(clientParams);

        int offset = (pageNum-1) * pageSize;
        String q = clientParams.get("orgType");

        System.out.println("q = "+q);
        System.out.println("offset = "+offset);

        String queryString = "SELECT ID, ORG_TYPE , TITLE FROM ALL_ORG_MST WHERE (APPROVAL_STATUS = 'Approved')";
        if(q != null) queryString += " AND ((ORG_TYPE = '"+q+"') OR (TITLE LIKE '%"+q+"%'))" ;
        log.info(queryString);

        Query query = em.createNativeQuery(queryString, Tuple.class);
        List<Tuple> listData = query.setFirstResult(offset).setMaxResults(pageSize).getResultList();
        em.close();

        List<Map<String, Object>> dataList = new ArrayList<>();
        listData.forEach( record-> {

            Long id = Long.parseLong( record.get("ID").toString() );
            String orgType = (record.get("ORG_TYPE").toString() != null) ? record.get("ORG_TYPE").toString() : "";
            String title = (record.get("TITLE") != null) ? record.get("TITLE").toString() : "";

            Map<String, Object> allOrgMstBean = new HashMap<>();
            allOrgMstBean.put("id", id );
            allOrgMstBean.put("orgType", orgType );
            allOrgMstBean.put("title", title);
            dataList.add(allOrgMstBean);

        });

        Integer total = this.countData("ALL_ORG_MST",clientParams);

        Map<String, Object> response = new HashMap<>();
        response.put("objectList", dataList);
        response.put("currentPage", pageNum);
        response.put("totalPages", this.getTotalPages( total, pageSize) );
        response.put("totalItems", total);
        System.out.println("page Size = "+pageSize);
        return response;
    }
}

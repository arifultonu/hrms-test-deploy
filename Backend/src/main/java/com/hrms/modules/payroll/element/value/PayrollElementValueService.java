package com.hrms.modules.payroll.element.value;

import com.hrms.modules.payroll.element.def.PayrollElement;
import com.hrms.modules.payroll.element.def.PayrollElementRepository;
import com.hrms.util.user.UserUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Service
public class PayrollElementValueService {


    @Autowired
    private PayrollElementValueRepository repository;

    @Autowired
    private PayrollElementRepository payrollElementRepository;


    /**
     * @return List
     */
    public List<PayrollElementValue> getAll() {

        List<PayrollElementValue> result = this.repository.findAll();
        if(result.size() > 0) {
            return result;
        } else {
            return new ArrayList<>();
        }

    }

    /**
     *
     * @param clientParams Map
     * @param pageNum int
     * @param pageSize int
     * @param sortField string
     * @param sortDir string
     * @return page list
     */
    public Page<PayrollElementValue> getAllPaginated(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        return this.repository.findAll( (Specification<PayrollElementValue>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if(!clientParams.isEmpty()){
                if(clientParams.containsKey("id")){
                    if (StringUtils.hasLength(clientParams.get("id"))) {
                        p = cb.and(p, cb.equal(root.get("id"), clientParams.get("id")));
                    }
                }
                if(clientParams.containsKey("entityName")){
                    if (StringUtils.hasLength(clientParams.get("entityName"))) {
                        p = cb.and(p, cb.like(root.get("entityName"), "%"+clientParams.get("entityName")+"%"));
                    }
                }
                return p;
            }
            return null;
        }, pageable);
    }

    /**
     * @param id Long
     * @return instance
     */
    public PayrollElementValue findById(Long id) {
        Optional<PayrollElementValue> entity = this.repository.findById(id);
        return entity.orElse(null);
    }

    /**
     * @param id Long
     * @return instance
     */
    public PayrollElementValue getById(Long id) {
        return this.findById(id);
    }

    /**
     * @param entityInst instance
     * @return instance
     */
    public PayrollElementValue setAttributeForCreateUpdate( PayrollElementValue entityInst , String activeOperation ){

        if(activeOperation.equals("Create")){
            entityInst.setCreationDateTime(new Date());
            entityInst.setCreationUser( UserUtil.getLoginUser() );

        } else if(activeOperation.equals("Update")){
            entityInst.setLastUpdateDateTime(new Date());
            entityInst.setLastUpdateUser(  UserUtil.getLoginUser() );

        }

        return entityInst;

    }

    /**
     * @param createEntity instance
     * @return instance
     */
    public PayrollElementValue create( PayrollElementValue createEntity ) {

        PayrollElement element = this.payrollElementRepository.findById(createEntity.getPayrollElement().getId()).orElse(null);
        if (element!=null){
            createEntity.setElementTitle(element.getCode());
        }

        Date activeStartDateOnly = DateUtils.truncate(createEntity.getActiveStartDate(), Calendar.DATE);
        createEntity.setActiveStartDate(activeStartDateOnly);

        Date activeEndDateOnly = DateUtils.truncate(createEntity.getActiveEndDate(), Calendar.DATE);
        createEntity.setActiveEndDate(activeEndDateOnly);

        createEntity.setEmpCode(createEntity.getEmpCode());

        // call setAttributeForCreateUpdate
        createEntity = this.setAttributeForCreateUpdate(createEntity, "Create");
        return this.repository.save(createEntity);

    }

    /**
     * @param editEntity instance
     * @return instance
     */
    public PayrollElementValue update( PayrollElementValue editEntity ) {

        Optional<PayrollElementValue> dbEntityInstOp = this.repository.findById(editEntity.getId());
        if(dbEntityInstOp.isPresent()) {
            PayrollElementValue dbEntityInst = dbEntityInstOp.get();
            dbEntityInst = this.setAttributeForCreateUpdate(dbEntityInst, "Update");
            // set updated values
            dbEntityInst.setElementTitle(  editEntity.getElementTitle() );
            dbEntityInst.setElementAmount(  editEntity.getElementAmount() );
            return this.repository.save(dbEntityInst);
        }
        return editEntity;

    }

    /**
     * @param id Long
     * @return Map
     */
    public Map<String, Object> deleteById( Long id ) {

        Map<String, Object> status = new HashMap<>();

        try {
            Optional<PayrollElementValue> entityInst = this.repository.findById(id);
            if(entityInst.isPresent()){
                this.repository.deleteById(id);
                status.put("deleteStatus", true);
            } else {
                status.put("deleteStatus", false);
                status.put("errorMsg", "Resource not found for this id: " + id);
            }
        } catch (Exception e){
            status.put("deleteStatus", false);
            status.put("errorMsg", e.getMessage());
        }

        return status;

    }

    public List<PayrollElementValue> writeFileDataInDb(MultipartFile file) {
        List<PayrollElementValue> entityList = new ArrayList<>();

        try {

            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);
            System.out.println("worksheet.getLastRowNum() : " + worksheet.getPhysicalNumberOfRows());

            for (int index = 0; index < worksheet.getPhysicalNumberOfRows(); index++) {
                if (index > 0) {

                    System.out.println("index: " + index);
                    PayrollElementValue entity = new PayrollElementValue();

                    XSSFRow row = worksheet.getRow(index);

                    System.out.println("row 0: " + row.getCell(0).getStringCellValue());
                    System.out.println("row 1: " + row.getCell(1).getStringCellValue());
                    System.out.println("row 2: " + row.getCell(2).getStringCellValue());
                    System.out.println("row 3: " + row.getCell(3).getStringCellValue());
                    System.out.println("row 4: " + row.getCell(4).getStringCellValue());

                    String activeStartDate_ = row.getCell(0).getStringCellValue(); // 2021-01-01 // ISO format
                    String activeEndDate_ = row.getCell(1).getStringCellValue();
                    if( (activeEndDate_ == null || activeEndDate_.isEmpty()) || (activeStartDate_ == null || activeStartDate_.isEmpty()) ){
                        continue;
                    }
                    Date activeStartDate = DateUtils.truncate(DateUtils.parseDate(activeStartDate_, "yyyy-MM-dd"), Calendar.DATE);
                    Date activeEndDate = DateUtils.truncate(DateUtils.parseDate(activeEndDate_, "yyyy-MM-dd"), Calendar.DATE);

                    entity.setActiveStartDate(activeStartDate);
                    entity.setActiveEndDate(activeEndDate);
                    entity.setElementAmount(Double.valueOf(row.getCell(2).getStringCellValue()));
                    entity.setElementTitle(row.getCell(3).getStringCellValue());
                    entity.setEmpCode(row.getCell(4).getStringCellValue());

                    // call setAttributeForCreateUpdate
                    entity = this.setAttributeForCreateUpdate(entity, "Create");
                    PayrollElementValue inst = this.repository.findByActiveStartDateAndActiveEndDateAndEmpCodeAndElementTitle(
                            entity.getActiveStartDate(), entity.getActiveEndDate(), entity.getEmpCode(),entity.getElementTitle());
                    if(inst == null){
                        entityList.add(entity);
                    }


                }
            }
            // save in DB
            this.repository.saveAll(entityList);


        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return entityList;
    }
}

package com.hrms.serviceImpl.test;
import com.hrms.dto.test.TestDTO;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.entity.test.Test;
import com.hrms.modules.common.repository.AllOrgMstRepository;
import com.hrms.repository.test.TestRepository;
import com.hrms.service.generic.impl.ServiceGenericImpl;
import com.hrms.service.test.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl extends ServiceGenericImpl<Test> implements ITestService{

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private AllOrgMstRepository allOrgMstRepository;

    @Override
    public Test save2(TestDTO test) {
//        if(test.getAllOrgMstSection() != null && test.getAllOrgMstSection().getId() != null) {
//            Long id = test.getAllOrgMstSection().getId();
//            test.setAllOrgMstSection(allOrgMstRepository.findById(id).get());
//        }
//
//        if(test.getAllOrgMstSubSection() != null && test.getAllOrgMstSubSection().getId() != null ) {
//            Long id = test.getAllOrgMstSubSection().getId();
//            test.setAllOrgMstSubSection(allOrgMstRepository.findById(id).get());
//        }
//        return testRepository.save(test);

        Test test1 = new Test();
        AllOrgMst section = allOrgMstRepository.findById(test.getAllOrgMstSectionId()).get();
        AllOrgMst subSection = allOrgMstRepository.findById(test.getAllOrgMstSubSection()).get();
        test1.setAllOrgMstSection(section);
        test1.setAllOrgMstSubSection(subSection);
        return testRepository.save(test1);
    }
}

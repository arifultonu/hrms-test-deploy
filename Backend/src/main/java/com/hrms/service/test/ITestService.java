package com.hrms.service.test;
import com.hrms.dto.test.TestDTO;
import com.hrms.entity.test.Test;
import com.hrms.service.generic.ServiceGeneric;
import org.springframework.http.ResponseEntity;

public interface ITestService extends ServiceGeneric<Test>{

    Test save2(TestDTO entity);
}

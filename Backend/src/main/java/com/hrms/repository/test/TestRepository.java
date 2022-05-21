package com.hrms.repository.test;
import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.entity.test.Test;
import com.hrms.repository.generic.GenericRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TestRepository extends GenericRepository<Test>{
   
}

package com.hrms.service.generic;

import com.hrms.entity.baseEntity.BaseEntity;
import com.hrms.exception.CustomException;
import com.hrms.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface ServiceGeneric<T extends BaseEntity>  {

    List<T> findAll() throws CustomException;
    T save(T entity) throws CustomException;
    void delete(Long id) throws CustomException;
    Optional<T> findById(Long id) throws NotFoundException;
    T update(T entity) throws CustomException;
}


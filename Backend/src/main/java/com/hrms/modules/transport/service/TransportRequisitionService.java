package com.hrms.modules.transport.service;

import com.hrms.exception.CustomException;
import com.hrms.modules.transport.entity.TransportRequisition;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface TransportRequisitionService {
    ResponseEntity<TransportRequisition> save(TransportRequisition transportRequisition);

    Page<TransportRequisition> getAllPaginate(Map<String, String> clientParams, int pageNum, int pageSize, String sortField, String sortDir);

    ResponseEntity<TransportRequisition> getById(Long id);

    ResponseEntity<TransportRequisition> update(TransportRequisition transportRequisition);

    ResponseEntity<Void> delete(Long id) throws CustomException;
}

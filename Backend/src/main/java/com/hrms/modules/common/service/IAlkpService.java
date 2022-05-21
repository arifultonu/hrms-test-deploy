package com.hrms.modules.common.service;

import com.hrms.modules.common.dto.AlkpDTO;
import com.hrms.modules.common.entity.Alkp;
import com.hrms.service.generic.ServiceGeneric;

import java.util.List;

public interface IAlkpService extends ServiceGeneric<Alkp> {
    List<Alkp> findActiveAlkp();

    List<Alkp> findByIsActiveTrueAndParentIsNull();


    Alkp create(AlkpDTO alkpDTO);

    Alkp search(String keyword);
}

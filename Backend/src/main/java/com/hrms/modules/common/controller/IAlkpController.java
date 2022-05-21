package com.hrms.modules.common.controller;

import com.hrms.controller.generic.ControllerGeneric;
import com.hrms.modules.common.dto.AlkpDTO;
import com.hrms.modules.common.entity.Alkp;

import java.util.List;

public interface IAlkpController extends ControllerGeneric<Alkp> {

    public List<Alkp> getActiveAlkp();

    public List<Alkp> getParentAndActiveAlkp();

    public Alkp create(AlkpDTO alkpDTO);

    public Alkp search(String keyword);
}

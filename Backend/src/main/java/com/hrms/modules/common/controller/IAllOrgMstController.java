package com.hrms.modules.common.controller;
import com.hrms.modules.common.entity.AllOrgMst;
import com.hrms.controller.generic.ControllerGeneric;

import java.util.List;

public interface IAllOrgMstController extends ControllerGeneric<AllOrgMst>{


    public List<AllOrgMst> getAllOrgMstByOrgType(String orgType);
   
}

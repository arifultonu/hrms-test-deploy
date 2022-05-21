package com.hrms.controller.system;

import com.hrms.controller.generic.ControllerGeneric;
import com.hrms.dto.System.SystemMenuAdminCoreUiDTO;
import com.hrms.entity.system.SystemMenu;

import java.util.List;

public interface ISystemMenuController extends ControllerGeneric<SystemMenu> {

    public List<SystemMenuAdminCoreUiDTO> getSystemMenuAdminCoreUiResponse();
}

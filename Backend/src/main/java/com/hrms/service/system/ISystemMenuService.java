package com.hrms.service.system;

import com.hrms.dto.System.SystemMenuAdminCoreUiDTO;
import com.hrms.entity.system.SystemMenu;
import com.hrms.service.generic.ServiceGeneric;

import java.util.List;

public interface ISystemMenuService extends ServiceGeneric<SystemMenu> {


    List<SystemMenuAdminCoreUiDTO> getAdminCoreUiResponse();
}

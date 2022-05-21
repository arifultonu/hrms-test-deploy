package com.hrms.util;


import com.hrms.util.user.UserUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

@Component
public class SetAttributeUpdate {
    public static void setSysAttributeForCreateUpdate(Object entityInst, String activeOperation)  {

        if (activeOperation.equals("Create")) {
            try {
                entityInst.getClass().getMethod("setCreationUser", String.class).invoke(entityInst, UserUtil.getLoginUser());
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            try {
                entityInst.getClass().getMethod("setCreationDateTime", Date.class).invoke(entityInst, new Date());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }

        } else if (activeOperation.equals("Update")) {
            try {
                entityInst.getClass().getMethod("setLastUpdateUser", String.class).invoke(entityInst, UserUtil.getLoginUser());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                entityInst.getClass().getMethod("setLastUpdateDateTime", Date.class).invoke(entityInst, new Date());
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }


    }
}

package com.hrms.modules.selfservice.sim.entityListener;

import com.hrms.modules.selfservice.sim.entity.SimManagement;
import com.hrms.modules.selfservice.sim.entity.SimManagementLog;
import com.hrms.util.BeanUtil;

import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;
import java.util.logging.Logger;

import static javax.transaction.Transactional.TxType.MANDATORY;

public class SimManagementListener {

    Logger log = Logger.getLogger(SimManagementListener.class.getName());

    @PrePersist
    public void prePersist(SimManagement entity) {
        // Persistence logic
        log.info("SimManagementLog ...INSERT");
        perform(entity, "INSERT");
    }

    @PreUpdate
    public void preUpdate(SimManagement entity) {
        // Updating logic
        log.info("SimManagementLog ...UPDATE");
        perform(entity, "UPDATE");
    }

    @PreRemove
    public void preRemove(SimManagement entity) {
        // Removal logic
        log.info("SimManagementLog ...DELETE");
        perform(entity, "DELETE");
    }

    @Transactional(MANDATORY)
    void perform(SimManagement entity, String action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new SimManagementLog(entity, action));
    }


}

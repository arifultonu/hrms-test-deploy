package com.hrms.modules.payroll.entityListener;


import com.hrms.modules.payroll.assignment.PayrollElementAssignment;
import com.hrms.modules.payroll.assignment.PayrollElementAssignmentLog;
import com.hrms.util.BeanUtil;
import javax.persistence.EntityManager;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;
import java.util.logging.Logger;
import static javax.transaction.Transactional.TxType.MANDATORY;


public class PayrollElementAssignmentListener {

    Logger log = Logger.getLogger(PayrollElementAssignmentListener.class.getName());

    @PrePersist
    public void prePersist(PayrollElementAssignment entity) {
        // Persistence logic
        log.info("PayrollElementAssignmentLog ...INSERT");
        perform(entity, "INSERT");
    }

    @PreUpdate
    public void preUpdate(PayrollElementAssignment entity) {
        // Updation logic
        log.info("PayrollElementAssignmentLog ...UPDATE");
        perform(entity, "UPDATE");
    }

    @PreRemove
    public void preRemove(PayrollElementAssignment entity) {
        // Removal logic
        log.info("PayrollElementAssignmentLog ...DELETE");
        perform(entity, "DELETE");
    }

    @Transactional(MANDATORY)
    void perform(PayrollElementAssignment entity, String action) {
        EntityManager entityManager = BeanUtil.getBean(EntityManager.class);
        entityManager.persist(new PayrollElementAssignmentLog(entity, action));

    }
}

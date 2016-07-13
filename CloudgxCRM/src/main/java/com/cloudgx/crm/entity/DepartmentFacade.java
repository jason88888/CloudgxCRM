/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudgx.crm.entity;

import com.cloudgx.crm.entity.controller.DepartmentJpaController;
import com.cloudgx.crm.entity.controller.exceptions.RollbackFailureException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

/**
 *
 * @author moon
 */
@Stateless
public class DepartmentFacade extends AbstractFacade<Department> {

    @PersistenceContext(unitName = "com.cloudgx_CloudgxCRM_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    private EntityManagerFactory emf;
    @Resource
    private UserTransaction utx;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DepartmentFacade() {
        super(Department.class);
    }
    
    /**
     * 根据云之家部门同步本地部门
     * @param d
     * @return 
     */
    public Department synByOAuth(com.yunzhijia.open.Department d){
        Department dep=this.findByKey(Department_.fullname,d.getFullName());
        if(dep==null){
            dep = new Department();
        }
        dep.setUid(d.getUid());
        dep.setFullname(d.getFullName());
        dep.setName(d.getName());
        if(d.getParentId()!=null&&!"".equals(d.getParentId())){
            dep.setParent(this.findByKey(Department_.uid,d.getParentId()));
        }
        DepartmentJpaController jpa=new DepartmentJpaController(utx, emf);
        try {
            jpa.edit(dep);
        } catch (RollbackFailureException ex) {
            Logger.getLogger(DepartmentFacade.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(DepartmentFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
//        this.edit(dep);
        return dep;
    }
}

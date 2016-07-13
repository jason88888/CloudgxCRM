/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudgx.crm.entity.controller;

import com.cloudgx.crm.entity.controller.exceptions.NonexistentEntityException;
import com.cloudgx.crm.entity.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cloudgx.crm.entity.Contract;
import com.cloudgx.crm.entity.Employee;
import com.cloudgx.crm.entity.Linkman;
import com.cloudgx.crm.entity.ServiceRecord;
import com.cloudgx.crm.entity.Task;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author moon
 */
public class ServiceRecordJpaController implements Serializable {

    public ServiceRecordJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ServiceRecord serviceRecord) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Contract contractid = serviceRecord.getContractid();
            if (contractid != null) {
                contractid = em.getReference(contractid.getClass(), contractid.getId());
                serviceRecord.setContractid(contractid);
            }
            Employee employeeid = serviceRecord.getEmployeeid();
            if (employeeid != null) {
                employeeid = em.getReference(employeeid.getClass(), employeeid.getId());
                serviceRecord.setEmployeeid(employeeid);
            }
            Linkman linkmanid = serviceRecord.getLinkmanid();
            if (linkmanid != null) {
                linkmanid = em.getReference(linkmanid.getClass(), linkmanid.getId());
                serviceRecord.setLinkmanid(linkmanid);
            }
            Task taskid = serviceRecord.getTaskid();
            if (taskid != null) {
                taskid = em.getReference(taskid.getClass(), taskid.getId());
                serviceRecord.setTaskid(taskid);
            }
            em.persist(serviceRecord);
            if (contractid != null) {
                contractid.getServiceRecordList().add(serviceRecord);
                contractid = em.merge(contractid);
            }
            if (employeeid != null) {
                employeeid.getServiceRecordList().add(serviceRecord);
                employeeid = em.merge(employeeid);
            }
            if (linkmanid != null) {
                linkmanid.getServiceRecordList().add(serviceRecord);
                linkmanid = em.merge(linkmanid);
            }
            if (taskid != null) {
                taskid.getServiceRecordList().add(serviceRecord);
                taskid = em.merge(taskid);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ServiceRecord serviceRecord) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ServiceRecord persistentServiceRecord = em.find(ServiceRecord.class, serviceRecord.getId());
            Contract contractidOld = persistentServiceRecord.getContractid();
            Contract contractidNew = serviceRecord.getContractid();
            Employee employeeidOld = persistentServiceRecord.getEmployeeid();
            Employee employeeidNew = serviceRecord.getEmployeeid();
            Linkman linkmanidOld = persistentServiceRecord.getLinkmanid();
            Linkman linkmanidNew = serviceRecord.getLinkmanid();
            Task taskidOld = persistentServiceRecord.getTaskid();
            Task taskidNew = serviceRecord.getTaskid();
            if (contractidNew != null) {
                contractidNew = em.getReference(contractidNew.getClass(), contractidNew.getId());
                serviceRecord.setContractid(contractidNew);
            }
            if (employeeidNew != null) {
                employeeidNew = em.getReference(employeeidNew.getClass(), employeeidNew.getId());
                serviceRecord.setEmployeeid(employeeidNew);
            }
            if (linkmanidNew != null) {
                linkmanidNew = em.getReference(linkmanidNew.getClass(), linkmanidNew.getId());
                serviceRecord.setLinkmanid(linkmanidNew);
            }
            if (taskidNew != null) {
                taskidNew = em.getReference(taskidNew.getClass(), taskidNew.getId());
                serviceRecord.setTaskid(taskidNew);
            }
            serviceRecord = em.merge(serviceRecord);
            if (contractidOld != null && !contractidOld.equals(contractidNew)) {
                contractidOld.getServiceRecordList().remove(serviceRecord);
                contractidOld = em.merge(contractidOld);
            }
            if (contractidNew != null && !contractidNew.equals(contractidOld)) {
                contractidNew.getServiceRecordList().add(serviceRecord);
                contractidNew = em.merge(contractidNew);
            }
            if (employeeidOld != null && !employeeidOld.equals(employeeidNew)) {
                employeeidOld.getServiceRecordList().remove(serviceRecord);
                employeeidOld = em.merge(employeeidOld);
            }
            if (employeeidNew != null && !employeeidNew.equals(employeeidOld)) {
                employeeidNew.getServiceRecordList().add(serviceRecord);
                employeeidNew = em.merge(employeeidNew);
            }
            if (linkmanidOld != null && !linkmanidOld.equals(linkmanidNew)) {
                linkmanidOld.getServiceRecordList().remove(serviceRecord);
                linkmanidOld = em.merge(linkmanidOld);
            }
            if (linkmanidNew != null && !linkmanidNew.equals(linkmanidOld)) {
                linkmanidNew.getServiceRecordList().add(serviceRecord);
                linkmanidNew = em.merge(linkmanidNew);
            }
            if (taskidOld != null && !taskidOld.equals(taskidNew)) {
                taskidOld.getServiceRecordList().remove(serviceRecord);
                taskidOld = em.merge(taskidOld);
            }
            if (taskidNew != null && !taskidNew.equals(taskidOld)) {
                taskidNew.getServiceRecordList().add(serviceRecord);
                taskidNew = em.merge(taskidNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = serviceRecord.getId();
                if (findServiceRecord(id) == null) {
                    throw new NonexistentEntityException("The serviceRecord with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ServiceRecord serviceRecord;
            try {
                serviceRecord = em.getReference(ServiceRecord.class, id);
                serviceRecord.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The serviceRecord with id " + id + " no longer exists.", enfe);
            }
            Contract contractid = serviceRecord.getContractid();
            if (contractid != null) {
                contractid.getServiceRecordList().remove(serviceRecord);
                contractid = em.merge(contractid);
            }
            Employee employeeid = serviceRecord.getEmployeeid();
            if (employeeid != null) {
                employeeid.getServiceRecordList().remove(serviceRecord);
                employeeid = em.merge(employeeid);
            }
            Linkman linkmanid = serviceRecord.getLinkmanid();
            if (linkmanid != null) {
                linkmanid.getServiceRecordList().remove(serviceRecord);
                linkmanid = em.merge(linkmanid);
            }
            Task taskid = serviceRecord.getTaskid();
            if (taskid != null) {
                taskid.getServiceRecordList().remove(serviceRecord);
                taskid = em.merge(taskid);
            }
            em.remove(serviceRecord);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ServiceRecord> findServiceRecordEntities() {
        return findServiceRecordEntities(true, -1, -1);
    }

    public List<ServiceRecord> findServiceRecordEntities(int maxResults, int firstResult) {
        return findServiceRecordEntities(false, maxResults, firstResult);
    }

    private List<ServiceRecord> findServiceRecordEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ServiceRecord.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ServiceRecord findServiceRecord(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ServiceRecord.class, id);
        } finally {
            em.close();
        }
    }

    public int getServiceRecordCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ServiceRecord> rt = cq.from(ServiceRecord.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

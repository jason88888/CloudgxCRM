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
import com.cloudgx.crm.entity.Custom;
import com.cloudgx.crm.entity.Linkman;
import com.cloudgx.crm.entity.Task;
import java.util.ArrayList;
import java.util.List;
import com.cloudgx.crm.entity.ServiceRecord;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author moon
 */
public class LinkmanJpaController implements Serializable {

    public LinkmanJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Linkman linkman) throws RollbackFailureException, Exception {
        if (linkman.getTaskList() == null) {
            linkman.setTaskList(new ArrayList<Task>());
        }
        if (linkman.getServiceRecordList() == null) {
            linkman.setServiceRecordList(new ArrayList<ServiceRecord>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Custom customid = linkman.getCustomid();
            if (customid != null) {
                customid = em.getReference(customid.getClass(), customid.getId());
                linkman.setCustomid(customid);
            }
            List<Task> attachedTaskList = new ArrayList<Task>();
            for (Task taskListTaskToAttach : linkman.getTaskList()) {
                taskListTaskToAttach = em.getReference(taskListTaskToAttach.getClass(), taskListTaskToAttach.getId());
                attachedTaskList.add(taskListTaskToAttach);
            }
            linkman.setTaskList(attachedTaskList);
            List<ServiceRecord> attachedServiceRecordList = new ArrayList<ServiceRecord>();
            for (ServiceRecord serviceRecordListServiceRecordToAttach : linkman.getServiceRecordList()) {
                serviceRecordListServiceRecordToAttach = em.getReference(serviceRecordListServiceRecordToAttach.getClass(), serviceRecordListServiceRecordToAttach.getId());
                attachedServiceRecordList.add(serviceRecordListServiceRecordToAttach);
            }
            linkman.setServiceRecordList(attachedServiceRecordList);
            em.persist(linkman);
            if (customid != null) {
                customid.getLinkmanList().add(linkman);
                customid = em.merge(customid);
            }
            for (Task taskListTask : linkman.getTaskList()) {
                taskListTask.getLinkmanList().add(linkman);
                taskListTask = em.merge(taskListTask);
            }
            for (ServiceRecord serviceRecordListServiceRecord : linkman.getServiceRecordList()) {
                Linkman oldLinkmanidOfServiceRecordListServiceRecord = serviceRecordListServiceRecord.getLinkmanid();
                serviceRecordListServiceRecord.setLinkmanid(linkman);
                serviceRecordListServiceRecord = em.merge(serviceRecordListServiceRecord);
                if (oldLinkmanidOfServiceRecordListServiceRecord != null) {
                    oldLinkmanidOfServiceRecordListServiceRecord.getServiceRecordList().remove(serviceRecordListServiceRecord);
                    oldLinkmanidOfServiceRecordListServiceRecord = em.merge(oldLinkmanidOfServiceRecordListServiceRecord);
                }
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

    public void edit(Linkman linkman) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Linkman persistentLinkman = em.find(Linkman.class, linkman.getId());
            Custom customidOld = persistentLinkman.getCustomid();
            Custom customidNew = linkman.getCustomid();
            List<Task> taskListOld = persistentLinkman.getTaskList();
            List<Task> taskListNew = linkman.getTaskList();
            List<ServiceRecord> serviceRecordListOld = persistentLinkman.getServiceRecordList();
            List<ServiceRecord> serviceRecordListNew = linkman.getServiceRecordList();
            if (customidNew != null) {
                customidNew = em.getReference(customidNew.getClass(), customidNew.getId());
                linkman.setCustomid(customidNew);
            }
            List<Task> attachedTaskListNew = new ArrayList<Task>();
            for (Task taskListNewTaskToAttach : taskListNew) {
                taskListNewTaskToAttach = em.getReference(taskListNewTaskToAttach.getClass(), taskListNewTaskToAttach.getId());
                attachedTaskListNew.add(taskListNewTaskToAttach);
            }
            taskListNew = attachedTaskListNew;
            linkman.setTaskList(taskListNew);
            List<ServiceRecord> attachedServiceRecordListNew = new ArrayList<ServiceRecord>();
            for (ServiceRecord serviceRecordListNewServiceRecordToAttach : serviceRecordListNew) {
                serviceRecordListNewServiceRecordToAttach = em.getReference(serviceRecordListNewServiceRecordToAttach.getClass(), serviceRecordListNewServiceRecordToAttach.getId());
                attachedServiceRecordListNew.add(serviceRecordListNewServiceRecordToAttach);
            }
            serviceRecordListNew = attachedServiceRecordListNew;
            linkman.setServiceRecordList(serviceRecordListNew);
            linkman = em.merge(linkman);
            if (customidOld != null && !customidOld.equals(customidNew)) {
                customidOld.getLinkmanList().remove(linkman);
                customidOld = em.merge(customidOld);
            }
            if (customidNew != null && !customidNew.equals(customidOld)) {
                customidNew.getLinkmanList().add(linkman);
                customidNew = em.merge(customidNew);
            }
            for (Task taskListOldTask : taskListOld) {
                if (!taskListNew.contains(taskListOldTask)) {
                    taskListOldTask.getLinkmanList().remove(linkman);
                    taskListOldTask = em.merge(taskListOldTask);
                }
            }
            for (Task taskListNewTask : taskListNew) {
                if (!taskListOld.contains(taskListNewTask)) {
                    taskListNewTask.getLinkmanList().add(linkman);
                    taskListNewTask = em.merge(taskListNewTask);
                }
            }
            for (ServiceRecord serviceRecordListOldServiceRecord : serviceRecordListOld) {
                if (!serviceRecordListNew.contains(serviceRecordListOldServiceRecord)) {
                    serviceRecordListOldServiceRecord.setLinkmanid(null);
                    serviceRecordListOldServiceRecord = em.merge(serviceRecordListOldServiceRecord);
                }
            }
            for (ServiceRecord serviceRecordListNewServiceRecord : serviceRecordListNew) {
                if (!serviceRecordListOld.contains(serviceRecordListNewServiceRecord)) {
                    Linkman oldLinkmanidOfServiceRecordListNewServiceRecord = serviceRecordListNewServiceRecord.getLinkmanid();
                    serviceRecordListNewServiceRecord.setLinkmanid(linkman);
                    serviceRecordListNewServiceRecord = em.merge(serviceRecordListNewServiceRecord);
                    if (oldLinkmanidOfServiceRecordListNewServiceRecord != null && !oldLinkmanidOfServiceRecordListNewServiceRecord.equals(linkman)) {
                        oldLinkmanidOfServiceRecordListNewServiceRecord.getServiceRecordList().remove(serviceRecordListNewServiceRecord);
                        oldLinkmanidOfServiceRecordListNewServiceRecord = em.merge(oldLinkmanidOfServiceRecordListNewServiceRecord);
                    }
                }
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
                Integer id = linkman.getId();
                if (findLinkman(id) == null) {
                    throw new NonexistentEntityException("The linkman with id " + id + " no longer exists.");
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
            Linkman linkman;
            try {
                linkman = em.getReference(Linkman.class, id);
                linkman.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The linkman with id " + id + " no longer exists.", enfe);
            }
            Custom customid = linkman.getCustomid();
            if (customid != null) {
                customid.getLinkmanList().remove(linkman);
                customid = em.merge(customid);
            }
            List<Task> taskList = linkman.getTaskList();
            for (Task taskListTask : taskList) {
                taskListTask.getLinkmanList().remove(linkman);
                taskListTask = em.merge(taskListTask);
            }
            List<ServiceRecord> serviceRecordList = linkman.getServiceRecordList();
            for (ServiceRecord serviceRecordListServiceRecord : serviceRecordList) {
                serviceRecordListServiceRecord.setLinkmanid(null);
                serviceRecordListServiceRecord = em.merge(serviceRecordListServiceRecord);
            }
            em.remove(linkman);
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

    public List<Linkman> findLinkmanEntities() {
        return findLinkmanEntities(true, -1, -1);
    }

    public List<Linkman> findLinkmanEntities(int maxResults, int firstResult) {
        return findLinkmanEntities(false, maxResults, firstResult);
    }

    private List<Linkman> findLinkmanEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Linkman.class));
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

    public Linkman findLinkman(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Linkman.class, id);
        } finally {
            em.close();
        }
    }

    public int getLinkmanCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Linkman> rt = cq.from(Linkman.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

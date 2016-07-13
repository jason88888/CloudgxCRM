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
import com.cloudgx.crm.entity.Employee;
import com.cloudgx.crm.entity.Linkman;
import java.util.ArrayList;
import java.util.List;
import com.cloudgx.crm.entity.ServiceRecord;
import com.cloudgx.crm.entity.Task;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author moon
 */
public class TaskJpaController implements Serializable {

    public TaskJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Task task) throws RollbackFailureException, Exception {
        if (task.getLinkmanList() == null) {
            task.setLinkmanList(new ArrayList<Linkman>());
        }
        if (task.getServiceRecordList() == null) {
            task.setServiceRecordList(new ArrayList<ServiceRecord>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Employee accepter = task.getAccepter();
            if (accepter != null) {
                accepter = em.getReference(accepter.getClass(), accepter.getId());
                task.setAccepter(accepter);
            }
            Employee sender = task.getSender();
            if (sender != null) {
                sender = em.getReference(sender.getClass(), sender.getId());
                task.setSender(sender);
            }
            List<Linkman> attachedLinkmanList = new ArrayList<Linkman>();
            for (Linkman linkmanListLinkmanToAttach : task.getLinkmanList()) {
                linkmanListLinkmanToAttach = em.getReference(linkmanListLinkmanToAttach.getClass(), linkmanListLinkmanToAttach.getId());
                attachedLinkmanList.add(linkmanListLinkmanToAttach);
            }
            task.setLinkmanList(attachedLinkmanList);
            List<ServiceRecord> attachedServiceRecordList = new ArrayList<ServiceRecord>();
            for (ServiceRecord serviceRecordListServiceRecordToAttach : task.getServiceRecordList()) {
                serviceRecordListServiceRecordToAttach = em.getReference(serviceRecordListServiceRecordToAttach.getClass(), serviceRecordListServiceRecordToAttach.getId());
                attachedServiceRecordList.add(serviceRecordListServiceRecordToAttach);
            }
            task.setServiceRecordList(attachedServiceRecordList);
            em.persist(task);
            if (accepter != null) {
                accepter.getTaskList().add(task);
                accepter = em.merge(accepter);
            }
            if (sender != null) {
                sender.getTaskList().add(task);
                sender = em.merge(sender);
            }
            for (Linkman linkmanListLinkman : task.getLinkmanList()) {
                linkmanListLinkman.getTaskList().add(task);
                linkmanListLinkman = em.merge(linkmanListLinkman);
            }
            for (ServiceRecord serviceRecordListServiceRecord : task.getServiceRecordList()) {
                Task oldTaskidOfServiceRecordListServiceRecord = serviceRecordListServiceRecord.getTaskid();
                serviceRecordListServiceRecord.setTaskid(task);
                serviceRecordListServiceRecord = em.merge(serviceRecordListServiceRecord);
                if (oldTaskidOfServiceRecordListServiceRecord != null) {
                    oldTaskidOfServiceRecordListServiceRecord.getServiceRecordList().remove(serviceRecordListServiceRecord);
                    oldTaskidOfServiceRecordListServiceRecord = em.merge(oldTaskidOfServiceRecordListServiceRecord);
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

    public void edit(Task task) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Task persistentTask = em.find(Task.class, task.getId());
            Employee accepterOld = persistentTask.getAccepter();
            Employee accepterNew = task.getAccepter();
            Employee senderOld = persistentTask.getSender();
            Employee senderNew = task.getSender();
            List<Linkman> linkmanListOld = persistentTask.getLinkmanList();
            List<Linkman> linkmanListNew = task.getLinkmanList();
            List<ServiceRecord> serviceRecordListOld = persistentTask.getServiceRecordList();
            List<ServiceRecord> serviceRecordListNew = task.getServiceRecordList();
            if (accepterNew != null) {
                accepterNew = em.getReference(accepterNew.getClass(), accepterNew.getId());
                task.setAccepter(accepterNew);
            }
            if (senderNew != null) {
                senderNew = em.getReference(senderNew.getClass(), senderNew.getId());
                task.setSender(senderNew);
            }
            List<Linkman> attachedLinkmanListNew = new ArrayList<Linkman>();
            for (Linkman linkmanListNewLinkmanToAttach : linkmanListNew) {
                linkmanListNewLinkmanToAttach = em.getReference(linkmanListNewLinkmanToAttach.getClass(), linkmanListNewLinkmanToAttach.getId());
                attachedLinkmanListNew.add(linkmanListNewLinkmanToAttach);
            }
            linkmanListNew = attachedLinkmanListNew;
            task.setLinkmanList(linkmanListNew);
            List<ServiceRecord> attachedServiceRecordListNew = new ArrayList<ServiceRecord>();
            for (ServiceRecord serviceRecordListNewServiceRecordToAttach : serviceRecordListNew) {
                serviceRecordListNewServiceRecordToAttach = em.getReference(serviceRecordListNewServiceRecordToAttach.getClass(), serviceRecordListNewServiceRecordToAttach.getId());
                attachedServiceRecordListNew.add(serviceRecordListNewServiceRecordToAttach);
            }
            serviceRecordListNew = attachedServiceRecordListNew;
            task.setServiceRecordList(serviceRecordListNew);
            task = em.merge(task);
            if (accepterOld != null && !accepterOld.equals(accepterNew)) {
                accepterOld.getTaskList().remove(task);
                accepterOld = em.merge(accepterOld);
            }
            if (accepterNew != null && !accepterNew.equals(accepterOld)) {
                accepterNew.getTaskList().add(task);
                accepterNew = em.merge(accepterNew);
            }
            if (senderOld != null && !senderOld.equals(senderNew)) {
                senderOld.getTaskList().remove(task);
                senderOld = em.merge(senderOld);
            }
            if (senderNew != null && !senderNew.equals(senderOld)) {
                senderNew.getTaskList().add(task);
                senderNew = em.merge(senderNew);
            }
            for (Linkman linkmanListOldLinkman : linkmanListOld) {
                if (!linkmanListNew.contains(linkmanListOldLinkman)) {
                    linkmanListOldLinkman.getTaskList().remove(task);
                    linkmanListOldLinkman = em.merge(linkmanListOldLinkman);
                }
            }
            for (Linkman linkmanListNewLinkman : linkmanListNew) {
                if (!linkmanListOld.contains(linkmanListNewLinkman)) {
                    linkmanListNewLinkman.getTaskList().add(task);
                    linkmanListNewLinkman = em.merge(linkmanListNewLinkman);
                }
            }
            for (ServiceRecord serviceRecordListOldServiceRecord : serviceRecordListOld) {
                if (!serviceRecordListNew.contains(serviceRecordListOldServiceRecord)) {
                    serviceRecordListOldServiceRecord.setTaskid(null);
                    serviceRecordListOldServiceRecord = em.merge(serviceRecordListOldServiceRecord);
                }
            }
            for (ServiceRecord serviceRecordListNewServiceRecord : serviceRecordListNew) {
                if (!serviceRecordListOld.contains(serviceRecordListNewServiceRecord)) {
                    Task oldTaskidOfServiceRecordListNewServiceRecord = serviceRecordListNewServiceRecord.getTaskid();
                    serviceRecordListNewServiceRecord.setTaskid(task);
                    serviceRecordListNewServiceRecord = em.merge(serviceRecordListNewServiceRecord);
                    if (oldTaskidOfServiceRecordListNewServiceRecord != null && !oldTaskidOfServiceRecordListNewServiceRecord.equals(task)) {
                        oldTaskidOfServiceRecordListNewServiceRecord.getServiceRecordList().remove(serviceRecordListNewServiceRecord);
                        oldTaskidOfServiceRecordListNewServiceRecord = em.merge(oldTaskidOfServiceRecordListNewServiceRecord);
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
                Integer id = task.getId();
                if (findTask(id) == null) {
                    throw new NonexistentEntityException("The task with id " + id + " no longer exists.");
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
            Task task;
            try {
                task = em.getReference(Task.class, id);
                task.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The task with id " + id + " no longer exists.", enfe);
            }
            Employee accepter = task.getAccepter();
            if (accepter != null) {
                accepter.getTaskList().remove(task);
                accepter = em.merge(accepter);
            }
            Employee sender = task.getSender();
            if (sender != null) {
                sender.getTaskList().remove(task);
                sender = em.merge(sender);
            }
            List<Linkman> linkmanList = task.getLinkmanList();
            for (Linkman linkmanListLinkman : linkmanList) {
                linkmanListLinkman.getTaskList().remove(task);
                linkmanListLinkman = em.merge(linkmanListLinkman);
            }
            List<ServiceRecord> serviceRecordList = task.getServiceRecordList();
            for (ServiceRecord serviceRecordListServiceRecord : serviceRecordList) {
                serviceRecordListServiceRecord.setTaskid(null);
                serviceRecordListServiceRecord = em.merge(serviceRecordListServiceRecord);
            }
            em.remove(task);
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

    public List<Task> findTaskEntities() {
        return findTaskEntities(true, -1, -1);
    }

    public List<Task> findTaskEntities(int maxResults, int firstResult) {
        return findTaskEntities(false, maxResults, firstResult);
    }

    private List<Task> findTaskEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Task.class));
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

    public Task findTask(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Task.class, id);
        } finally {
            em.close();
        }
    }

    public int getTaskCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Task> rt = cq.from(Task.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

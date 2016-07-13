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
import com.cloudgx.crm.entity.Department;
import com.cloudgx.crm.entity.Employee;
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
public class EmployeeJpaController implements Serializable {

    public EmployeeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Employee employee) throws RollbackFailureException, Exception {
        if (employee.getTaskList() == null) {
            employee.setTaskList(new ArrayList<Task>());
        }
        if (employee.getTaskList1() == null) {
            employee.setTaskList1(new ArrayList<Task>());
        }
        if (employee.getServiceRecordList() == null) {
            employee.setServiceRecordList(new ArrayList<ServiceRecord>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Department departmentid = employee.getDepartmentid();
            if (departmentid != null) {
                departmentid = em.getReference(departmentid.getClass(), departmentid.getId());
                employee.setDepartmentid(departmentid);
            }
            List<Task> attachedTaskList = new ArrayList<Task>();
            for (Task taskListTaskToAttach : employee.getTaskList()) {
                taskListTaskToAttach = em.getReference(taskListTaskToAttach.getClass(), taskListTaskToAttach.getId());
                attachedTaskList.add(taskListTaskToAttach);
            }
            employee.setTaskList(attachedTaskList);
            List<Task> attachedTaskList1 = new ArrayList<Task>();
            for (Task taskList1TaskToAttach : employee.getTaskList1()) {
                taskList1TaskToAttach = em.getReference(taskList1TaskToAttach.getClass(), taskList1TaskToAttach.getId());
                attachedTaskList1.add(taskList1TaskToAttach);
            }
            employee.setTaskList1(attachedTaskList1);
            List<ServiceRecord> attachedServiceRecordList = new ArrayList<ServiceRecord>();
            for (ServiceRecord serviceRecordListServiceRecordToAttach : employee.getServiceRecordList()) {
                serviceRecordListServiceRecordToAttach = em.getReference(serviceRecordListServiceRecordToAttach.getClass(), serviceRecordListServiceRecordToAttach.getId());
                attachedServiceRecordList.add(serviceRecordListServiceRecordToAttach);
            }
            employee.setServiceRecordList(attachedServiceRecordList);
            em.persist(employee);
            if (departmentid != null) {
                departmentid.getEmployeeList().add(employee);
                departmentid = em.merge(departmentid);
            }
            for (Task taskListTask : employee.getTaskList()) {
                Employee oldAccepterOfTaskListTask = taskListTask.getAccepter();
                taskListTask.setAccepter(employee);
                taskListTask = em.merge(taskListTask);
                if (oldAccepterOfTaskListTask != null) {
                    oldAccepterOfTaskListTask.getTaskList().remove(taskListTask);
                    oldAccepterOfTaskListTask = em.merge(oldAccepterOfTaskListTask);
                }
            }
            for (Task taskList1Task : employee.getTaskList1()) {
                Employee oldSenderOfTaskList1Task = taskList1Task.getSender();
                taskList1Task.setSender(employee);
                taskList1Task = em.merge(taskList1Task);
                if (oldSenderOfTaskList1Task != null) {
                    oldSenderOfTaskList1Task.getTaskList1().remove(taskList1Task);
                    oldSenderOfTaskList1Task = em.merge(oldSenderOfTaskList1Task);
                }
            }
            for (ServiceRecord serviceRecordListServiceRecord : employee.getServiceRecordList()) {
                Employee oldEmployeeidOfServiceRecordListServiceRecord = serviceRecordListServiceRecord.getEmployeeid();
                serviceRecordListServiceRecord.setEmployeeid(employee);
                serviceRecordListServiceRecord = em.merge(serviceRecordListServiceRecord);
                if (oldEmployeeidOfServiceRecordListServiceRecord != null) {
                    oldEmployeeidOfServiceRecordListServiceRecord.getServiceRecordList().remove(serviceRecordListServiceRecord);
                    oldEmployeeidOfServiceRecordListServiceRecord = em.merge(oldEmployeeidOfServiceRecordListServiceRecord);
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

    public void edit(Employee employee) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Employee persistentEmployee = em.find(Employee.class, employee.getId());
            Department departmentidOld = persistentEmployee.getDepartmentid();
            Department departmentidNew = employee.getDepartmentid();
            List<Task> taskListOld = persistentEmployee.getTaskList();
            List<Task> taskListNew = employee.getTaskList();
            List<Task> taskList1Old = persistentEmployee.getTaskList1();
            List<Task> taskList1New = employee.getTaskList1();
            List<ServiceRecord> serviceRecordListOld = persistentEmployee.getServiceRecordList();
            List<ServiceRecord> serviceRecordListNew = employee.getServiceRecordList();
            if (departmentidNew != null) {
                departmentidNew = em.getReference(departmentidNew.getClass(), departmentidNew.getId());
                employee.setDepartmentid(departmentidNew);
            }
            List<Task> attachedTaskListNew = new ArrayList<Task>();
            for (Task taskListNewTaskToAttach : taskListNew) {
                taskListNewTaskToAttach = em.getReference(taskListNewTaskToAttach.getClass(), taskListNewTaskToAttach.getId());
                attachedTaskListNew.add(taskListNewTaskToAttach);
            }
            taskListNew = attachedTaskListNew;
            employee.setTaskList(taskListNew);
            List<Task> attachedTaskList1New = new ArrayList<Task>();
            for (Task taskList1NewTaskToAttach : taskList1New) {
                taskList1NewTaskToAttach = em.getReference(taskList1NewTaskToAttach.getClass(), taskList1NewTaskToAttach.getId());
                attachedTaskList1New.add(taskList1NewTaskToAttach);
            }
            taskList1New = attachedTaskList1New;
            employee.setTaskList1(taskList1New);
            List<ServiceRecord> attachedServiceRecordListNew = new ArrayList<ServiceRecord>();
            for (ServiceRecord serviceRecordListNewServiceRecordToAttach : serviceRecordListNew) {
                serviceRecordListNewServiceRecordToAttach = em.getReference(serviceRecordListNewServiceRecordToAttach.getClass(), serviceRecordListNewServiceRecordToAttach.getId());
                attachedServiceRecordListNew.add(serviceRecordListNewServiceRecordToAttach);
            }
            serviceRecordListNew = attachedServiceRecordListNew;
            employee.setServiceRecordList(serviceRecordListNew);
            employee = em.merge(employee);
            if (departmentidOld != null && !departmentidOld.equals(departmentidNew)) {
                departmentidOld.getEmployeeList().remove(employee);
                departmentidOld = em.merge(departmentidOld);
            }
            if (departmentidNew != null && !departmentidNew.equals(departmentidOld)) {
                departmentidNew.getEmployeeList().add(employee);
                departmentidNew = em.merge(departmentidNew);
            }
            for (Task taskListOldTask : taskListOld) {
                if (!taskListNew.contains(taskListOldTask)) {
                    taskListOldTask.setAccepter(null);
                    taskListOldTask = em.merge(taskListOldTask);
                }
            }
            for (Task taskListNewTask : taskListNew) {
                if (!taskListOld.contains(taskListNewTask)) {
                    Employee oldAccepterOfTaskListNewTask = taskListNewTask.getAccepter();
                    taskListNewTask.setAccepter(employee);
                    taskListNewTask = em.merge(taskListNewTask);
                    if (oldAccepterOfTaskListNewTask != null && !oldAccepterOfTaskListNewTask.equals(employee)) {
                        oldAccepterOfTaskListNewTask.getTaskList().remove(taskListNewTask);
                        oldAccepterOfTaskListNewTask = em.merge(oldAccepterOfTaskListNewTask);
                    }
                }
            }
            for (Task taskList1OldTask : taskList1Old) {
                if (!taskList1New.contains(taskList1OldTask)) {
                    taskList1OldTask.setSender(null);
                    taskList1OldTask = em.merge(taskList1OldTask);
                }
            }
            for (Task taskList1NewTask : taskList1New) {
                if (!taskList1Old.contains(taskList1NewTask)) {
                    Employee oldSenderOfTaskList1NewTask = taskList1NewTask.getSender();
                    taskList1NewTask.setSender(employee);
                    taskList1NewTask = em.merge(taskList1NewTask);
                    if (oldSenderOfTaskList1NewTask != null && !oldSenderOfTaskList1NewTask.equals(employee)) {
                        oldSenderOfTaskList1NewTask.getTaskList1().remove(taskList1NewTask);
                        oldSenderOfTaskList1NewTask = em.merge(oldSenderOfTaskList1NewTask);
                    }
                }
            }
            for (ServiceRecord serviceRecordListOldServiceRecord : serviceRecordListOld) {
                if (!serviceRecordListNew.contains(serviceRecordListOldServiceRecord)) {
                    serviceRecordListOldServiceRecord.setEmployeeid(null);
                    serviceRecordListOldServiceRecord = em.merge(serviceRecordListOldServiceRecord);
                }
            }
            for (ServiceRecord serviceRecordListNewServiceRecord : serviceRecordListNew) {
                if (!serviceRecordListOld.contains(serviceRecordListNewServiceRecord)) {
                    Employee oldEmployeeidOfServiceRecordListNewServiceRecord = serviceRecordListNewServiceRecord.getEmployeeid();
                    serviceRecordListNewServiceRecord.setEmployeeid(employee);
                    serviceRecordListNewServiceRecord = em.merge(serviceRecordListNewServiceRecord);
                    if (oldEmployeeidOfServiceRecordListNewServiceRecord != null && !oldEmployeeidOfServiceRecordListNewServiceRecord.equals(employee)) {
                        oldEmployeeidOfServiceRecordListNewServiceRecord.getServiceRecordList().remove(serviceRecordListNewServiceRecord);
                        oldEmployeeidOfServiceRecordListNewServiceRecord = em.merge(oldEmployeeidOfServiceRecordListNewServiceRecord);
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
                Integer id = employee.getId();
                if (findEmployee(id) == null) {
                    throw new NonexistentEntityException("The employee with id " + id + " no longer exists.");
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
            Employee employee;
            try {
                employee = em.getReference(Employee.class, id);
                employee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The employee with id " + id + " no longer exists.", enfe);
            }
            Department departmentid = employee.getDepartmentid();
            if (departmentid != null) {
                departmentid.getEmployeeList().remove(employee);
                departmentid = em.merge(departmentid);
            }
            List<Task> taskList = employee.getTaskList();
            for (Task taskListTask : taskList) {
                taskListTask.setAccepter(null);
                taskListTask = em.merge(taskListTask);
            }
            List<Task> taskList1 = employee.getTaskList1();
            for (Task taskList1Task : taskList1) {
                taskList1Task.setSender(null);
                taskList1Task = em.merge(taskList1Task);
            }
            List<ServiceRecord> serviceRecordList = employee.getServiceRecordList();
            for (ServiceRecord serviceRecordListServiceRecord : serviceRecordList) {
                serviceRecordListServiceRecord.setEmployeeid(null);
                serviceRecordListServiceRecord = em.merge(serviceRecordListServiceRecord);
            }
            em.remove(employee);
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

    public List<Employee> findEmployeeEntities() {
        return findEmployeeEntities(true, -1, -1);
    }

    public List<Employee> findEmployeeEntities(int maxResults, int firstResult) {
        return findEmployeeEntities(false, maxResults, firstResult);
    }

    private List<Employee> findEmployeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Employee.class));
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

    public Employee findEmployee(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmployeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Employee> rt = cq.from(Employee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

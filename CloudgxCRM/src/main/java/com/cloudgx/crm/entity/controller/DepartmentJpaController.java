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
import java.util.ArrayList;
import java.util.List;
import com.cloudgx.crm.entity.Employee;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author moon
 */
public class DepartmentJpaController implements Serializable {

    public DepartmentJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Department department) throws RollbackFailureException, Exception {
        if (department.getDepartmentList() == null) {
            department.setDepartmentList(new ArrayList<Department>());
        }
        if (department.getEmployeeList() == null) {
            department.setEmployeeList(new ArrayList<Employee>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Department parent = department.getParent();
            if (parent != null) {
                parent = em.getReference(parent.getClass(), parent.getId());
                department.setParent(parent);
            }
            List<Department> attachedDepartmentList = new ArrayList<Department>();
            for (Department departmentListDepartmentToAttach : department.getDepartmentList()) {
                departmentListDepartmentToAttach = em.getReference(departmentListDepartmentToAttach.getClass(), departmentListDepartmentToAttach.getId());
                attachedDepartmentList.add(departmentListDepartmentToAttach);
            }
            department.setDepartmentList(attachedDepartmentList);
            List<Employee> attachedEmployeeList = new ArrayList<Employee>();
            for (Employee employeeListEmployeeToAttach : department.getEmployeeList()) {
                employeeListEmployeeToAttach = em.getReference(employeeListEmployeeToAttach.getClass(), employeeListEmployeeToAttach.getId());
                attachedEmployeeList.add(employeeListEmployeeToAttach);
            }
            department.setEmployeeList(attachedEmployeeList);
            em.persist(department);
            if (parent != null) {
                parent.getDepartmentList().add(department);
                parent = em.merge(parent);
            }
            for (Department departmentListDepartment : department.getDepartmentList()) {
                Department oldParentOfDepartmentListDepartment = departmentListDepartment.getParent();
                departmentListDepartment.setParent(department);
                departmentListDepartment = em.merge(departmentListDepartment);
                if (oldParentOfDepartmentListDepartment != null) {
                    oldParentOfDepartmentListDepartment.getDepartmentList().remove(departmentListDepartment);
                    oldParentOfDepartmentListDepartment = em.merge(oldParentOfDepartmentListDepartment);
                }
            }
            for (Employee employeeListEmployee : department.getEmployeeList()) {
                Department oldDepartmentidOfEmployeeListEmployee = employeeListEmployee.getDepartmentid();
                employeeListEmployee.setDepartmentid(department);
                employeeListEmployee = em.merge(employeeListEmployee);
                if (oldDepartmentidOfEmployeeListEmployee != null) {
                    oldDepartmentidOfEmployeeListEmployee.getEmployeeList().remove(employeeListEmployee);
                    oldDepartmentidOfEmployeeListEmployee = em.merge(oldDepartmentidOfEmployeeListEmployee);
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

    public void edit(Department department) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Department persistentDepartment = em.find(Department.class, department.getId());
            Department parentOld = persistentDepartment.getParent();
            Department parentNew = department.getParent();
            List<Department> departmentListOld = persistentDepartment.getDepartmentList();
            List<Department> departmentListNew = department.getDepartmentList();
            List<Employee> employeeListOld = persistentDepartment.getEmployeeList();
            List<Employee> employeeListNew = department.getEmployeeList();
            if (parentNew != null) {
                parentNew = em.getReference(parentNew.getClass(), parentNew.getId());
                department.setParent(parentNew);
            }
            List<Department> attachedDepartmentListNew = new ArrayList<Department>();
            for (Department departmentListNewDepartmentToAttach : departmentListNew) {
                departmentListNewDepartmentToAttach = em.getReference(departmentListNewDepartmentToAttach.getClass(), departmentListNewDepartmentToAttach.getId());
                attachedDepartmentListNew.add(departmentListNewDepartmentToAttach);
            }
            departmentListNew = attachedDepartmentListNew;
            department.setDepartmentList(departmentListNew);
            List<Employee> attachedEmployeeListNew = new ArrayList<Employee>();
            for (Employee employeeListNewEmployeeToAttach : employeeListNew) {
                employeeListNewEmployeeToAttach = em.getReference(employeeListNewEmployeeToAttach.getClass(), employeeListNewEmployeeToAttach.getId());
                attachedEmployeeListNew.add(employeeListNewEmployeeToAttach);
            }
            employeeListNew = attachedEmployeeListNew;
            department.setEmployeeList(employeeListNew);
            department = em.merge(department);
            if (parentOld != null && !parentOld.equals(parentNew)) {
                parentOld.getDepartmentList().remove(department);
                parentOld = em.merge(parentOld);
            }
            if (parentNew != null && !parentNew.equals(parentOld)) {
                parentNew.getDepartmentList().add(department);
                parentNew = em.merge(parentNew);
            }
            for (Department departmentListOldDepartment : departmentListOld) {
                if (!departmentListNew.contains(departmentListOldDepartment)) {
                    departmentListOldDepartment.setParent(null);
                    departmentListOldDepartment = em.merge(departmentListOldDepartment);
                }
            }
            for (Department departmentListNewDepartment : departmentListNew) {
                if (!departmentListOld.contains(departmentListNewDepartment)) {
                    Department oldParentOfDepartmentListNewDepartment = departmentListNewDepartment.getParent();
                    departmentListNewDepartment.setParent(department);
                    departmentListNewDepartment = em.merge(departmentListNewDepartment);
                    if (oldParentOfDepartmentListNewDepartment != null && !oldParentOfDepartmentListNewDepartment.equals(department)) {
                        oldParentOfDepartmentListNewDepartment.getDepartmentList().remove(departmentListNewDepartment);
                        oldParentOfDepartmentListNewDepartment = em.merge(oldParentOfDepartmentListNewDepartment);
                    }
                }
            }
            for (Employee employeeListOldEmployee : employeeListOld) {
                if (!employeeListNew.contains(employeeListOldEmployee)) {
                    employeeListOldEmployee.setDepartmentid(null);
                    employeeListOldEmployee = em.merge(employeeListOldEmployee);
                }
            }
            for (Employee employeeListNewEmployee : employeeListNew) {
                if (!employeeListOld.contains(employeeListNewEmployee)) {
                    Department oldDepartmentidOfEmployeeListNewEmployee = employeeListNewEmployee.getDepartmentid();
                    employeeListNewEmployee.setDepartmentid(department);
                    employeeListNewEmployee = em.merge(employeeListNewEmployee);
                    if (oldDepartmentidOfEmployeeListNewEmployee != null && !oldDepartmentidOfEmployeeListNewEmployee.equals(department)) {
                        oldDepartmentidOfEmployeeListNewEmployee.getEmployeeList().remove(employeeListNewEmployee);
                        oldDepartmentidOfEmployeeListNewEmployee = em.merge(oldDepartmentidOfEmployeeListNewEmployee);
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
                Integer id = department.getId();
                if (findDepartment(id) == null) {
                    throw new NonexistentEntityException("The department with id " + id + " no longer exists.");
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
            Department department;
            try {
                department = em.getReference(Department.class, id);
                department.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The department with id " + id + " no longer exists.", enfe);
            }
            Department parent = department.getParent();
            if (parent != null) {
                parent.getDepartmentList().remove(department);
                parent = em.merge(parent);
            }
            List<Department> departmentList = department.getDepartmentList();
            for (Department departmentListDepartment : departmentList) {
                departmentListDepartment.setParent(null);
                departmentListDepartment = em.merge(departmentListDepartment);
            }
            List<Employee> employeeList = department.getEmployeeList();
            for (Employee employeeListEmployee : employeeList) {
                employeeListEmployee.setDepartmentid(null);
                employeeListEmployee = em.merge(employeeListEmployee);
            }
            em.remove(department);
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

    public List<Department> findDepartmentEntities() {
        return findDepartmentEntities(true, -1, -1);
    }

    public List<Department> findDepartmentEntities(int maxResults, int firstResult) {
        return findDepartmentEntities(false, maxResults, firstResult);
    }

    private List<Department> findDepartmentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Department.class));
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

    public Department findDepartment(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Department.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartmentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Department> rt = cq.from(Department.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

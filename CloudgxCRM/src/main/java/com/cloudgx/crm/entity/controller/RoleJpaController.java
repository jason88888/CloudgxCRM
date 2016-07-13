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
import com.cloudgx.crm.entity.Module;
import com.cloudgx.crm.entity.Role;
import java.util.ArrayList;
import java.util.List;
import com.cloudgx.crm.entity.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author moon
 */
public class RoleJpaController implements Serializable {

    public RoleJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Role role) throws RollbackFailureException, Exception {
        if (role.getModuleList() == null) {
            role.setModuleList(new ArrayList<Module>());
        }
        if (role.getUserList() == null) {
            role.setUserList(new ArrayList<User>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Module> attachedModuleList = new ArrayList<Module>();
            for (Module moduleListModuleToAttach : role.getModuleList()) {
                moduleListModuleToAttach = em.getReference(moduleListModuleToAttach.getClass(), moduleListModuleToAttach.getId());
                attachedModuleList.add(moduleListModuleToAttach);
            }
            role.setModuleList(attachedModuleList);
            List<User> attachedUserList = new ArrayList<User>();
            for (User userListUserToAttach : role.getUserList()) {
                userListUserToAttach = em.getReference(userListUserToAttach.getClass(), userListUserToAttach.getId());
                attachedUserList.add(userListUserToAttach);
            }
            role.setUserList(attachedUserList);
            em.persist(role);
            for (Module moduleListModule : role.getModuleList()) {
                moduleListModule.getRoleList().add(role);
                moduleListModule = em.merge(moduleListModule);
            }
            for (User userListUser : role.getUserList()) {
                userListUser.getRoleList().add(role);
                userListUser = em.merge(userListUser);
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

    public void edit(Role role) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Role persistentRole = em.find(Role.class, role.getId());
            List<Module> moduleListOld = persistentRole.getModuleList();
            List<Module> moduleListNew = role.getModuleList();
            List<User> userListOld = persistentRole.getUserList();
            List<User> userListNew = role.getUserList();
            List<Module> attachedModuleListNew = new ArrayList<Module>();
            for (Module moduleListNewModuleToAttach : moduleListNew) {
                moduleListNewModuleToAttach = em.getReference(moduleListNewModuleToAttach.getClass(), moduleListNewModuleToAttach.getId());
                attachedModuleListNew.add(moduleListNewModuleToAttach);
            }
            moduleListNew = attachedModuleListNew;
            role.setModuleList(moduleListNew);
            List<User> attachedUserListNew = new ArrayList<User>();
            for (User userListNewUserToAttach : userListNew) {
                userListNewUserToAttach = em.getReference(userListNewUserToAttach.getClass(), userListNewUserToAttach.getId());
                attachedUserListNew.add(userListNewUserToAttach);
            }
            userListNew = attachedUserListNew;
            role.setUserList(userListNew);
            role = em.merge(role);
            for (Module moduleListOldModule : moduleListOld) {
                if (!moduleListNew.contains(moduleListOldModule)) {
                    moduleListOldModule.getRoleList().remove(role);
                    moduleListOldModule = em.merge(moduleListOldModule);
                }
            }
            for (Module moduleListNewModule : moduleListNew) {
                if (!moduleListOld.contains(moduleListNewModule)) {
                    moduleListNewModule.getRoleList().add(role);
                    moduleListNewModule = em.merge(moduleListNewModule);
                }
            }
            for (User userListOldUser : userListOld) {
                if (!userListNew.contains(userListOldUser)) {
                    userListOldUser.getRoleList().remove(role);
                    userListOldUser = em.merge(userListOldUser);
                }
            }
            for (User userListNewUser : userListNew) {
                if (!userListOld.contains(userListNewUser)) {
                    userListNewUser.getRoleList().add(role);
                    userListNewUser = em.merge(userListNewUser);
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
                Integer id = role.getId();
                if (findRole(id) == null) {
                    throw new NonexistentEntityException("The role with id " + id + " no longer exists.");
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
            Role role;
            try {
                role = em.getReference(Role.class, id);
                role.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The role with id " + id + " no longer exists.", enfe);
            }
            List<Module> moduleList = role.getModuleList();
            for (Module moduleListModule : moduleList) {
                moduleListModule.getRoleList().remove(role);
                moduleListModule = em.merge(moduleListModule);
            }
            List<User> userList = role.getUserList();
            for (User userListUser : userList) {
                userListUser.getRoleList().remove(role);
                userListUser = em.merge(userListUser);
            }
            em.remove(role);
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

    public List<Role> findRoleEntities() {
        return findRoleEntities(true, -1, -1);
    }

    public List<Role> findRoleEntities(int maxResults, int firstResult) {
        return findRoleEntities(false, maxResults, firstResult);
    }

    private List<Role> findRoleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Role.class));
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

    public Role findRole(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Role.class, id);
        } finally {
            em.close();
        }
    }

    public int getRoleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Role> rt = cq.from(Role.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudgx.crm.entity.controller;

import com.cloudgx.crm.entity.controller.exceptions.NonexistentEntityException;
import com.cloudgx.crm.entity.controller.exceptions.RollbackFailureException;
import com.cloudgx.crm.entity.Module;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cloudgx.crm.entity.Role;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author moon
 */
public class ModuleJpaController implements Serializable {

    public ModuleJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Module module) throws RollbackFailureException, Exception {
        if (module.getRoleList() == null) {
            module.setRoleList(new ArrayList<Role>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Role> attachedRoleList = new ArrayList<Role>();
            for (Role roleListRoleToAttach : module.getRoleList()) {
                roleListRoleToAttach = em.getReference(roleListRoleToAttach.getClass(), roleListRoleToAttach.getId());
                attachedRoleList.add(roleListRoleToAttach);
            }
            module.setRoleList(attachedRoleList);
            em.persist(module);
            for (Role roleListRole : module.getRoleList()) {
                roleListRole.getModuleList().add(module);
                roleListRole = em.merge(roleListRole);
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

    public void edit(Module module) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Module persistentModule = em.find(Module.class, module.getId());
            List<Role> roleListOld = persistentModule.getRoleList();
            List<Role> roleListNew = module.getRoleList();
            List<Role> attachedRoleListNew = new ArrayList<Role>();
            for (Role roleListNewRoleToAttach : roleListNew) {
                roleListNewRoleToAttach = em.getReference(roleListNewRoleToAttach.getClass(), roleListNewRoleToAttach.getId());
                attachedRoleListNew.add(roleListNewRoleToAttach);
            }
            roleListNew = attachedRoleListNew;
            module.setRoleList(roleListNew);
            module = em.merge(module);
            for (Role roleListOldRole : roleListOld) {
                if (!roleListNew.contains(roleListOldRole)) {
                    roleListOldRole.getModuleList().remove(module);
                    roleListOldRole = em.merge(roleListOldRole);
                }
            }
            for (Role roleListNewRole : roleListNew) {
                if (!roleListOld.contains(roleListNewRole)) {
                    roleListNewRole.getModuleList().add(module);
                    roleListNewRole = em.merge(roleListNewRole);
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
                Integer id = module.getId();
                if (findModule(id) == null) {
                    throw new NonexistentEntityException("The module with id " + id + " no longer exists.");
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
            Module module;
            try {
                module = em.getReference(Module.class, id);
                module.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The module with id " + id + " no longer exists.", enfe);
            }
            List<Role> roleList = module.getRoleList();
            for (Role roleListRole : roleList) {
                roleListRole.getModuleList().remove(module);
                roleListRole = em.merge(roleListRole);
            }
            em.remove(module);
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

    public List<Module> findModuleEntities() {
        return findModuleEntities(true, -1, -1);
    }

    public List<Module> findModuleEntities(int maxResults, int firstResult) {
        return findModuleEntities(false, maxResults, firstResult);
    }

    private List<Module> findModuleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Module.class));
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

    public Module findModule(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Module.class, id);
        } finally {
            em.close();
        }
    }

    public int getModuleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Module> rt = cq.from(Module.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudgx.crm.entity.controller;

import com.cloudgx.crm.entity.controller.exceptions.NonexistentEntityException;
import com.cloudgx.crm.entity.controller.exceptions.PreexistingEntityException;
import com.cloudgx.crm.entity.controller.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cloudgx.crm.entity.Contract;
import com.cloudgx.crm.entity.Produnct;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author moon
 */
public class ProdunctJpaController implements Serializable {

    public ProdunctJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Produnct produnct) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Contract contractid = produnct.getContractid();
            if (contractid != null) {
                contractid = em.getReference(contractid.getClass(), contractid.getId());
                produnct.setContractid(contractid);
            }
            em.persist(produnct);
            if (contractid != null) {
                contractid.getProdunctList().add(produnct);
                contractid = em.merge(contractid);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProdunct(produnct.getId()) != null) {
                throw new PreexistingEntityException("Produnct " + produnct + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Produnct produnct) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Produnct persistentProdunct = em.find(Produnct.class, produnct.getId());
            Contract contractidOld = persistentProdunct.getContractid();
            Contract contractidNew = produnct.getContractid();
            if (contractidNew != null) {
                contractidNew = em.getReference(contractidNew.getClass(), contractidNew.getId());
                produnct.setContractid(contractidNew);
            }
            produnct = em.merge(produnct);
            if (contractidOld != null && !contractidOld.equals(contractidNew)) {
                contractidOld.getProdunctList().remove(produnct);
                contractidOld = em.merge(contractidOld);
            }
            if (contractidNew != null && !contractidNew.equals(contractidOld)) {
                contractidNew.getProdunctList().add(produnct);
                contractidNew = em.merge(contractidNew);
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
                Integer id = produnct.getId();
                if (findProdunct(id) == null) {
                    throw new NonexistentEntityException("The produnct with id " + id + " no longer exists.");
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
            Produnct produnct;
            try {
                produnct = em.getReference(Produnct.class, id);
                produnct.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The produnct with id " + id + " no longer exists.", enfe);
            }
            Contract contractid = produnct.getContractid();
            if (contractid != null) {
                contractid.getProdunctList().remove(produnct);
                contractid = em.merge(contractid);
            }
            em.remove(produnct);
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

    public List<Produnct> findProdunctEntities() {
        return findProdunctEntities(true, -1, -1);
    }

    public List<Produnct> findProdunctEntities(int maxResults, int firstResult) {
        return findProdunctEntities(false, maxResults, firstResult);
    }

    private List<Produnct> findProdunctEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Produnct.class));
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

    public Produnct findProdunct(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Produnct.class, id);
        } finally {
            em.close();
        }
    }

    public int getProdunctCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Produnct> rt = cq.from(Produnct.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

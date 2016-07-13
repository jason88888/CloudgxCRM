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
import com.cloudgx.crm.entity.Linkman;
import java.util.ArrayList;
import java.util.List;
import com.cloudgx.crm.entity.Contract;
import com.cloudgx.crm.entity.Custom;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author moon
 */
public class CustomJpaController implements Serializable {

    public CustomJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Custom custom) throws RollbackFailureException, Exception {
        if (custom.getLinkmanList() == null) {
            custom.setLinkmanList(new ArrayList<Linkman>());
        }
        if (custom.getContractList() == null) {
            custom.setContractList(new ArrayList<Contract>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<Linkman> attachedLinkmanList = new ArrayList<Linkman>();
            for (Linkman linkmanListLinkmanToAttach : custom.getLinkmanList()) {
                linkmanListLinkmanToAttach = em.getReference(linkmanListLinkmanToAttach.getClass(), linkmanListLinkmanToAttach.getId());
                attachedLinkmanList.add(linkmanListLinkmanToAttach);
            }
            custom.setLinkmanList(attachedLinkmanList);
            List<Contract> attachedContractList = new ArrayList<Contract>();
            for (Contract contractListContractToAttach : custom.getContractList()) {
                contractListContractToAttach = em.getReference(contractListContractToAttach.getClass(), contractListContractToAttach.getId());
                attachedContractList.add(contractListContractToAttach);
            }
            custom.setContractList(attachedContractList);
            em.persist(custom);
            for (Linkman linkmanListLinkman : custom.getLinkmanList()) {
                Custom oldCustomidOfLinkmanListLinkman = linkmanListLinkman.getCustomid();
                linkmanListLinkman.setCustomid(custom);
                linkmanListLinkman = em.merge(linkmanListLinkman);
                if (oldCustomidOfLinkmanListLinkman != null) {
                    oldCustomidOfLinkmanListLinkman.getLinkmanList().remove(linkmanListLinkman);
                    oldCustomidOfLinkmanListLinkman = em.merge(oldCustomidOfLinkmanListLinkman);
                }
            }
            for (Contract contractListContract : custom.getContractList()) {
                Custom oldCustomidOfContractListContract = contractListContract.getCustomid();
                contractListContract.setCustomid(custom);
                contractListContract = em.merge(contractListContract);
                if (oldCustomidOfContractListContract != null) {
                    oldCustomidOfContractListContract.getContractList().remove(contractListContract);
                    oldCustomidOfContractListContract = em.merge(oldCustomidOfContractListContract);
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

    public void edit(Custom custom) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Custom persistentCustom = em.find(Custom.class, custom.getId());
            List<Linkman> linkmanListOld = persistentCustom.getLinkmanList();
            List<Linkman> linkmanListNew = custom.getLinkmanList();
            List<Contract> contractListOld = persistentCustom.getContractList();
            List<Contract> contractListNew = custom.getContractList();
            List<Linkman> attachedLinkmanListNew = new ArrayList<Linkman>();
            for (Linkman linkmanListNewLinkmanToAttach : linkmanListNew) {
                linkmanListNewLinkmanToAttach = em.getReference(linkmanListNewLinkmanToAttach.getClass(), linkmanListNewLinkmanToAttach.getId());
                attachedLinkmanListNew.add(linkmanListNewLinkmanToAttach);
            }
            linkmanListNew = attachedLinkmanListNew;
            custom.setLinkmanList(linkmanListNew);
            List<Contract> attachedContractListNew = new ArrayList<Contract>();
            for (Contract contractListNewContractToAttach : contractListNew) {
                contractListNewContractToAttach = em.getReference(contractListNewContractToAttach.getClass(), contractListNewContractToAttach.getId());
                attachedContractListNew.add(contractListNewContractToAttach);
            }
            contractListNew = attachedContractListNew;
            custom.setContractList(contractListNew);
            custom = em.merge(custom);
            for (Linkman linkmanListOldLinkman : linkmanListOld) {
                if (!linkmanListNew.contains(linkmanListOldLinkman)) {
                    linkmanListOldLinkman.setCustomid(null);
                    linkmanListOldLinkman = em.merge(linkmanListOldLinkman);
                }
            }
            for (Linkman linkmanListNewLinkman : linkmanListNew) {
                if (!linkmanListOld.contains(linkmanListNewLinkman)) {
                    Custom oldCustomidOfLinkmanListNewLinkman = linkmanListNewLinkman.getCustomid();
                    linkmanListNewLinkman.setCustomid(custom);
                    linkmanListNewLinkman = em.merge(linkmanListNewLinkman);
                    if (oldCustomidOfLinkmanListNewLinkman != null && !oldCustomidOfLinkmanListNewLinkman.equals(custom)) {
                        oldCustomidOfLinkmanListNewLinkman.getLinkmanList().remove(linkmanListNewLinkman);
                        oldCustomidOfLinkmanListNewLinkman = em.merge(oldCustomidOfLinkmanListNewLinkman);
                    }
                }
            }
            for (Contract contractListOldContract : contractListOld) {
                if (!contractListNew.contains(contractListOldContract)) {
                    contractListOldContract.setCustomid(null);
                    contractListOldContract = em.merge(contractListOldContract);
                }
            }
            for (Contract contractListNewContract : contractListNew) {
                if (!contractListOld.contains(contractListNewContract)) {
                    Custom oldCustomidOfContractListNewContract = contractListNewContract.getCustomid();
                    contractListNewContract.setCustomid(custom);
                    contractListNewContract = em.merge(contractListNewContract);
                    if (oldCustomidOfContractListNewContract != null && !oldCustomidOfContractListNewContract.equals(custom)) {
                        oldCustomidOfContractListNewContract.getContractList().remove(contractListNewContract);
                        oldCustomidOfContractListNewContract = em.merge(oldCustomidOfContractListNewContract);
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
                Integer id = custom.getId();
                if (findCustom(id) == null) {
                    throw new NonexistentEntityException("The custom with id " + id + " no longer exists.");
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
            Custom custom;
            try {
                custom = em.getReference(Custom.class, id);
                custom.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The custom with id " + id + " no longer exists.", enfe);
            }
            List<Linkman> linkmanList = custom.getLinkmanList();
            for (Linkman linkmanListLinkman : linkmanList) {
                linkmanListLinkman.setCustomid(null);
                linkmanListLinkman = em.merge(linkmanListLinkman);
            }
            List<Contract> contractList = custom.getContractList();
            for (Contract contractListContract : contractList) {
                contractListContract.setCustomid(null);
                contractListContract = em.merge(contractListContract);
            }
            em.remove(custom);
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

    public List<Custom> findCustomEntities() {
        return findCustomEntities(true, -1, -1);
    }

    public List<Custom> findCustomEntities(int maxResults, int firstResult) {
        return findCustomEntities(false, maxResults, firstResult);
    }

    private List<Custom> findCustomEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Custom.class));
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

    public Custom findCustom(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Custom.class, id);
        } finally {
            em.close();
        }
    }

    public int getCustomCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Custom> rt = cq.from(Custom.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

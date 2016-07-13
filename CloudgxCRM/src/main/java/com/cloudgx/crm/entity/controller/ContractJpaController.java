
package com.cloudgx.crm.entity.controller;

import com.cloudgx.crm.entity.controller.exceptions.NonexistentEntityException;
import com.cloudgx.crm.entity.controller.exceptions.RollbackFailureException;
import com.cloudgx.crm.entity.Contract;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.cloudgx.crm.entity.Custom;
import com.cloudgx.crm.entity.Produnct;
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
public class ContractJpaController implements Serializable {

    public ContractJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Contract contract) throws RollbackFailureException, Exception {
        if (contract.getProdunctList() == null) {
            contract.setProdunctList(new ArrayList<Produnct>());
        }
        if (contract.getServiceRecordList() == null) {
            contract.setServiceRecordList(new ArrayList<ServiceRecord>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Custom customid = contract.getCustomid();
            if (customid != null) {
                customid = em.getReference(customid.getClass(), customid.getId());
                contract.setCustomid(customid);
            }
            List<Produnct> attachedProdunctList = new ArrayList<Produnct>();
            for (Produnct produnctListProdunctToAttach : contract.getProdunctList()) {
                produnctListProdunctToAttach = em.getReference(produnctListProdunctToAttach.getClass(), produnctListProdunctToAttach.getId());
                attachedProdunctList.add(produnctListProdunctToAttach);
            }
            contract.setProdunctList(attachedProdunctList);
            List<ServiceRecord> attachedServiceRecordList = new ArrayList<ServiceRecord>();
            for (ServiceRecord serviceRecordListServiceRecordToAttach : contract.getServiceRecordList()) {
                serviceRecordListServiceRecordToAttach = em.getReference(serviceRecordListServiceRecordToAttach.getClass(), serviceRecordListServiceRecordToAttach.getId());
                attachedServiceRecordList.add(serviceRecordListServiceRecordToAttach);
            }
            contract.setServiceRecordList(attachedServiceRecordList);
            em.persist(contract);
            if (customid != null) {
                customid.getContractList().add(contract);
                customid = em.merge(customid);
            }
            for (Produnct produnctListProdunct : contract.getProdunctList()) {
                Contract oldContractidOfProdunctListProdunct = produnctListProdunct.getContractid();
                produnctListProdunct.setContractid(contract);
                produnctListProdunct = em.merge(produnctListProdunct);
                if (oldContractidOfProdunctListProdunct != null) {
                    oldContractidOfProdunctListProdunct.getProdunctList().remove(produnctListProdunct);
                    oldContractidOfProdunctListProdunct = em.merge(oldContractidOfProdunctListProdunct);
                }
            }
            for (ServiceRecord serviceRecordListServiceRecord : contract.getServiceRecordList()) {
                Contract oldContractidOfServiceRecordListServiceRecord = serviceRecordListServiceRecord.getContractid();
                serviceRecordListServiceRecord.setContractid(contract);
                serviceRecordListServiceRecord = em.merge(serviceRecordListServiceRecord);
                if (oldContractidOfServiceRecordListServiceRecord != null) {
                    oldContractidOfServiceRecordListServiceRecord.getServiceRecordList().remove(serviceRecordListServiceRecord);
                    oldContractidOfServiceRecordListServiceRecord = em.merge(oldContractidOfServiceRecordListServiceRecord);
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

    public void edit(Contract contract) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Contract persistentContract = em.find(Contract.class, contract.getId());
            Custom customidOld = persistentContract.getCustomid();
            Custom customidNew = contract.getCustomid();
            List<Produnct> produnctListOld = persistentContract.getProdunctList();
            List<Produnct> produnctListNew = contract.getProdunctList();
            List<ServiceRecord> serviceRecordListOld = persistentContract.getServiceRecordList();
            List<ServiceRecord> serviceRecordListNew = contract.getServiceRecordList();
            if (customidNew != null) {
                customidNew = em.getReference(customidNew.getClass(), customidNew.getId());
                contract.setCustomid(customidNew);
            }
            List<Produnct> attachedProdunctListNew = new ArrayList<Produnct>();
            for (Produnct produnctListNewProdunctToAttach : produnctListNew) {
                produnctListNewProdunctToAttach = em.getReference(produnctListNewProdunctToAttach.getClass(), produnctListNewProdunctToAttach.getId());
                attachedProdunctListNew.add(produnctListNewProdunctToAttach);
            }
            produnctListNew = attachedProdunctListNew;
            contract.setProdunctList(produnctListNew);
            List<ServiceRecord> attachedServiceRecordListNew = new ArrayList<ServiceRecord>();
            for (ServiceRecord serviceRecordListNewServiceRecordToAttach : serviceRecordListNew) {
                serviceRecordListNewServiceRecordToAttach = em.getReference(serviceRecordListNewServiceRecordToAttach.getClass(), serviceRecordListNewServiceRecordToAttach.getId());
                attachedServiceRecordListNew.add(serviceRecordListNewServiceRecordToAttach);
            }
            serviceRecordListNew = attachedServiceRecordListNew;
            contract.setServiceRecordList(serviceRecordListNew);
            contract = em.merge(contract);
            if (customidOld != null && !customidOld.equals(customidNew)) {
                customidOld.getContractList().remove(contract);
                customidOld = em.merge(customidOld);
            }
            if (customidNew != null && !customidNew.equals(customidOld)) {
                customidNew.getContractList().add(contract);
                customidNew = em.merge(customidNew);
            }
            for (Produnct produnctListOldProdunct : produnctListOld) {
                if (!produnctListNew.contains(produnctListOldProdunct)) {
                    produnctListOldProdunct.setContractid(null);
                    produnctListOldProdunct = em.merge(produnctListOldProdunct);
                }
            }
            for (Produnct produnctListNewProdunct : produnctListNew) {
                if (!produnctListOld.contains(produnctListNewProdunct)) {
                    Contract oldContractidOfProdunctListNewProdunct = produnctListNewProdunct.getContractid();
                    produnctListNewProdunct.setContractid(contract);
                    produnctListNewProdunct = em.merge(produnctListNewProdunct);
                    if (oldContractidOfProdunctListNewProdunct != null && !oldContractidOfProdunctListNewProdunct.equals(contract)) {
                        oldContractidOfProdunctListNewProdunct.getProdunctList().remove(produnctListNewProdunct);
                        oldContractidOfProdunctListNewProdunct = em.merge(oldContractidOfProdunctListNewProdunct);
                    }
                }
            }
            for (ServiceRecord serviceRecordListOldServiceRecord : serviceRecordListOld) {
                if (!serviceRecordListNew.contains(serviceRecordListOldServiceRecord)) {
                    serviceRecordListOldServiceRecord.setContractid(null);
                    serviceRecordListOldServiceRecord = em.merge(serviceRecordListOldServiceRecord);
                }
            }
            for (ServiceRecord serviceRecordListNewServiceRecord : serviceRecordListNew) {
                if (!serviceRecordListOld.contains(serviceRecordListNewServiceRecord)) {
                    Contract oldContractidOfServiceRecordListNewServiceRecord = serviceRecordListNewServiceRecord.getContractid();
                    serviceRecordListNewServiceRecord.setContractid(contract);
                    serviceRecordListNewServiceRecord = em.merge(serviceRecordListNewServiceRecord);
                    if (oldContractidOfServiceRecordListNewServiceRecord != null && !oldContractidOfServiceRecordListNewServiceRecord.equals(contract)) {
                        oldContractidOfServiceRecordListNewServiceRecord.getServiceRecordList().remove(serviceRecordListNewServiceRecord);
                        oldContractidOfServiceRecordListNewServiceRecord = em.merge(oldContractidOfServiceRecordListNewServiceRecord);
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
                Integer id = contract.getId();
                if (findContract(id) == null) {
                    throw new NonexistentEntityException("The contract with id " + id + " no longer exists.");
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
            Contract contract;
            try {
                contract = em.getReference(Contract.class, id);
                contract.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The contract with id " + id + " no longer exists.", enfe);
            }
            Custom customid = contract.getCustomid();
            if (customid != null) {
                customid.getContractList().remove(contract);
                customid = em.merge(customid);
            }
            List<Produnct> produnctList = contract.getProdunctList();
            for (Produnct produnctListProdunct : produnctList) {
                produnctListProdunct.setContractid(null);
                produnctListProdunct = em.merge(produnctListProdunct);
            }
            List<ServiceRecord> serviceRecordList = contract.getServiceRecordList();
            for (ServiceRecord serviceRecordListServiceRecord : serviceRecordList) {
                serviceRecordListServiceRecord.setContractid(null);
                serviceRecordListServiceRecord = em.merge(serviceRecordListServiceRecord);
            }
            em.remove(contract);
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

    public List<Contract> findContractEntities() {
        return findContractEntities(true, -1, -1);
    }

    public List<Contract> findContractEntities(int maxResults, int firstResult) {
        return findContractEntities(false, maxResults, firstResult);
    }

    private List<Contract> findContractEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Contract.class));
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

    public Contract findContract(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Contract.class, id);
        } finally {
            em.close();
        }
    }

    public int getContractCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Contract> rt = cq.from(Contract.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}

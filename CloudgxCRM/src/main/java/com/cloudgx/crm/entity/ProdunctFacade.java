/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudgx.crm.entity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author moon
 */
@Stateless
public class ProdunctFacade extends AbstractFacade<Produnct> {

    @PersistenceContext(unitName = "com.cloudgx_CloudgxCRM_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProdunctFacade() {
        super(Produnct.class);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudgx.crm.bean;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author moon
 */
@Named(value = "menu")
@SessionScoped
public class MenuManagedBean implements Serializable {

    /**
     * Creates a new instance of MenuManagedBean
     */
    public MenuManagedBean() {
    }
    
}

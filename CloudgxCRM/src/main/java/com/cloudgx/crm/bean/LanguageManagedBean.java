/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudgx.crm.bean;

import java.util.Locale;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * 语言管理
 * @author moon
 */
//@ManagedBean(name="language")
@Named("language")
public class LanguageManagedBean {
    private String locale = "zh";
    private String message = "";

    /**
     * 切换语言
     * @param locale
     */
    public void changeLocale(String locale){
        // Change the locale attribute
        this.locale = locale;
        // 改变页面地区环境
        System.out.println(locale);
//        return "";
        Locale l = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        System.out.println(l);
        Locale t = new Locale(this.locale);
        System.out.println(t);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(t);
        l = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        System.out.println(l);
    }
    
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

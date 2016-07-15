/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudgx.crm.bean;

import com.cloudgx.crm.ejb.DepartmentFacade;
import com.cloudgx.crm.ejb.EmployeeFacade;
import com.cloudgx.crm.entity.Department;
import com.cloudgx.crm.entity.Department_;
import com.cloudgx.crm.entity.Employee;
import com.yunzhijia.open.OAuth;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.http.Part;

/**
 *
 * @author moon
 */
@Named(value = "yzjOAuth")
@SessionScoped
public class OAuthManagedBean implements Serializable {

    private ArrayList<Employee> persons;
    private String eid;
    private Part key;
    private File keyFile;
    @EJB
    private DepartmentFacade departmentFacade;
    @EJB
    private EmployeeFacade employeeFacade;
    /**
     * 同步云之家部门、人员
     */
    public OAuthManagedBean() {
    }
    
    /**
     * 获取部门列表
     * @return 
     */    
    public List<Department> getDepartments() {
        return departmentFacade.findAll();
    }

    public List<Employee> getPersons() {
        return employeeFacade.findAll();
    }

    public String getEid() {
        return eid;
    }

    /**
     * 云之家企业ID
     * @param eid 
     */
    public void setEid(String eid) {
        this.eid = eid;
    }

    /**
     * 获取云之家Key文件的InputFile组件对象
     * @param key 
     */
    public void setKey(Part key) {
        this.key = key;
    }

    public Part getKey() {
        return key;
    }
    
    /**
     * 上传云之家企业Key文件，从云之家获取部门列表，同步本地数据库 
     * @return 
     */
    public String uploadKey(){
        try (InputStream input = key.getInputStream()) {
            keyFile=new File(key.getSubmittedFileName());
            if(keyFile.exists()){
                keyFile.delete();
            }
            Files.copy(input, keyFile.toPath());
            System.out.println(eid);
            if(eid!=null&&keyFile.exists()){
                ArrayList<com.yunzhijia.open.Department> ds = OAuth.getDepartment(this.eid,this.keyFile);
                for (com.yunzhijia.open.Department d : ds) {
                    this.synByOAuth(d);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(OAuthManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "deps";
    }
    
    /**
     * 根据云之家部门同步本地部门
     * @param d
     * @return 
     */
    private Department synByOAuth(com.yunzhijia.open.Department d){
        Department dep=departmentFacade.findByKey(Department_.fullname,d.getFullName());
        if(dep==null){
            dep = new Department();
        }
        dep.setUid(d.getUid());
        dep.setFullname(d.getFullName());
        dep.setName(d.getName());
        if(d.getParentId()!=null&&!"".equals(d.getParentId())){
            dep.setParent(departmentFacade.findByKey(Department_.uid,d.getParentId()));
        }
        departmentFacade.edit(dep);
        return dep;
    }
}

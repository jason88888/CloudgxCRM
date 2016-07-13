package com.cloudgx.crm.bean;

import com.cloudgx.crm.entity.Department;
import com.cloudgx.crm.entity.DepartmentFacade;
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
    private List<Department> departments;
    private ArrayList<Employee> persons;
    private String eid;
    private String fullname;
    private Part key;
    private File keyFile;
    @EJB
    private DepartmentFacade departmentFacade;
    /**
     * 同步云之家部门、人员
     */
    public OAuthManagedBean() {
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    
    public Department getDepartment(){
        Department d;
        if(this.fullname!=null){
            d=departmentFacade.findByKey(Department_.fullname, fullname);
        }else{
            d=new Department();
        }
        return d;
    }
    
    /**
     * 获取部门列表
     * @return 
     */    
    public List<Department> getDepartments() {
        return departmentFacade.findAll();
    }

    public void setDepartments(ArrayList<Department> departments) {
        this.departments = departments;
    }

    public ArrayList<Employee> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Employee> persons) {
        this.persons = persons;
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

    public Part getKey() {
        return key;
    }

    /**
     * 获取云之家Key文件的InputFile组件对象
     * @param key 
     */
    public void setKey(Part key) {
        this.key = key;
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
            if(this.eid!=null&&keyFile.exists()){
                ArrayList<com.yunzhijia.open.Department> ds = OAuth.getDepartment(this.eid,this.keyFile);
                for (com.yunzhijia.open.Department d : ds) {
                    departmentFacade.synByOAuth(d);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(OAuthManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "deps";
    }
}

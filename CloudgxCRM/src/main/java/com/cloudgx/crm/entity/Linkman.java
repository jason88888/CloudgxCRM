/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudgx.crm.entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author moon
 */
@Entity
@Table(name = "Linkman")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Linkman.findAll", query = "SELECT l FROM Linkman l"),
    @NamedQuery(name = "Linkman.findById", query = "SELECT l FROM Linkman l WHERE l.id = :id"),
    @NamedQuery(name = "Linkman.findByEmail", query = "SELECT l FROM Linkman l WHERE l.email = :email"),
    @NamedQuery(name = "Linkman.findByIsdefault", query = "SELECT l FROM Linkman l WHERE l.isdefault = :isdefault"),
    @NamedQuery(name = "Linkman.findByName", query = "SELECT l FROM Linkman l WHERE l.name = :name"),
    @NamedQuery(name = "Linkman.findByPhone", query = "SELECT l FROM Linkman l WHERE l.phone = :phone"),
    @NamedQuery(name = "Linkman.findByQq", query = "SELECT l FROM Linkman l WHERE l.qq = :qq"),
    @NamedQuery(name = "Linkman.findByWeixin", query = "SELECT l FROM Linkman l WHERE l.weixin = :weixin")})
public class Linkman implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="电子邮件无效")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Column(name = "isdefault")
    private Boolean isdefault;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="电话/传真格式无效, 应为 xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "phone")
    private String phone;
    @Size(max = 255)
    @Column(name = "qq")
    private String qq;
    @Size(max = 255)
    @Column(name = "weixin")
    private String weixin;
    @JoinTable(name = "Task_has_Linkman", joinColumns = {
        @JoinColumn(name = "Linkman_id", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "Task_id", referencedColumnName = "id")})
    @ManyToMany
    private List<Task> taskList;
    @JoinColumn(name = "Custom_id", referencedColumnName = "id")
    @ManyToOne
    private Custom customid;
    @OneToMany(mappedBy = "linkmanid")
    private List<ServiceRecord> serviceRecordList;

    public Linkman() {
    }

    public Linkman(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(Boolean isdefault) {
        this.isdefault = isdefault;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    @XmlTransient
    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public Custom getCustomid() {
        return customid;
    }

    public void setCustomid(Custom customid) {
        this.customid = customid;
    }

    @XmlTransient
    public List<ServiceRecord> getServiceRecordList() {
        return serviceRecordList;
    }

    public void setServiceRecordList(List<ServiceRecord> serviceRecordList) {
        this.serviceRecordList = serviceRecordList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Linkman)) {
            return false;
        }
        Linkman other = (Linkman) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cloudgx.crm.entity.Linkman[ id=" + id + " ]";
    }
    
}

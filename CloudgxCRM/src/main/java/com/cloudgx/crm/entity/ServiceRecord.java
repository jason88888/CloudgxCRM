/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudgx.crm.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author moon
 */
@Entity
@Table(name = "ServiceRecord")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ServiceRecord.findAll", query = "SELECT s FROM ServiceRecord s"),
    @NamedQuery(name = "ServiceRecord.findById", query = "SELECT s FROM ServiceRecord s WHERE s.id = :id"),
    @NamedQuery(name = "ServiceRecord.findByMode", query = "SELECT s FROM ServiceRecord s WHERE s.mode = :mode"),
    @NamedQuery(name = "ServiceRecord.findByTime", query = "SELECT s FROM ServiceRecord s WHERE s.time = :time")})
public class ServiceRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "mode")
    private Integer mode;
    @Size(max = 255)
    @Column(name = "time")
    private String time;
    @JoinColumn(name = "Contract_id", referencedColumnName = "id")
    @ManyToOne
    private Contract contractid;
    @JoinColumn(name = "Employee_id", referencedColumnName = "id")
    @ManyToOne
    private Employee employeeid;
    @JoinColumn(name = "Linkman_id", referencedColumnName = "id")
    @ManyToOne
    private Linkman linkmanid;
    @JoinColumn(name = "Task_id", referencedColumnName = "id")
    @ManyToOne
    private Task taskid;

    public ServiceRecord() {
    }

    public ServiceRecord(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Contract getContractid() {
        return contractid;
    }

    public void setContractid(Contract contractid) {
        this.contractid = contractid;
    }

    public Employee getEmployeeid() {
        return employeeid;
    }

    public void setEmployeeid(Employee employeeid) {
        this.employeeid = employeeid;
    }

    public Linkman getLinkmanid() {
        return linkmanid;
    }

    public void setLinkmanid(Linkman linkmanid) {
        this.linkmanid = linkmanid;
    }

    public Task getTaskid() {
        return taskid;
    }

    public void setTaskid(Task taskid) {
        this.taskid = taskid;
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
        if (!(object instanceof ServiceRecord)) {
            return false;
        }
        ServiceRecord other = (ServiceRecord) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cloudgx.crm.entity.ServiceRecord[ id=" + id + " ]";
    }
    
}

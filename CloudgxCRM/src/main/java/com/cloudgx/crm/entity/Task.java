/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudgx.crm.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author moon
 */
@Entity
@Table(name = "Task")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Task.findAll", query = "SELECT t FROM Task t"),
    @NamedQuery(name = "Task.findById", query = "SELECT t FROM Task t WHERE t.id = :id"),
    @NamedQuery(name = "Task.findByContent", query = "SELECT t FROM Task t WHERE t.content = :content"),
    @NamedQuery(name = "Task.findByFinishtime", query = "SELECT t FROM Task t WHERE t.finishtime = :finishtime"),
    @NamedQuery(name = "Task.findByReminders", query = "SELECT t FROM Task t WHERE t.reminders = :reminders"),
    @NamedQuery(name = "Task.findByResult", query = "SELECT t FROM Task t WHERE t.result = :result"),
    @NamedQuery(name = "Task.findByStarttime", query = "SELECT t FROM Task t WHERE t.starttime = :starttime"),
    @NamedQuery(name = "Task.findByState", query = "SELECT t FROM Task t WHERE t.state = :state"),
    @NamedQuery(name = "Task.findByTitle", query = "SELECT t FROM Task t WHERE t.title = :title")})
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "content")
    private String content;
    @Column(name = "finishtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishtime;
    @Column(name = "reminders")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reminders;
    @Size(max = 255)
    @Column(name = "result")
    private String result;
    @Column(name = "starttime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date starttime;
    @Column(name = "state")
    private Integer state;
    @Size(max = 255)
    @Column(name = "title")
    private String title;
    @ManyToMany(mappedBy = "taskList")
    private List<Linkman> linkmanList;
    @JoinColumn(name = "accepter", referencedColumnName = "id")
    @ManyToOne
    private Employee accepter;
    @JoinColumn(name = "sender", referencedColumnName = "id")
    @ManyToOne
    private Employee sender;
    @OneToMany(mappedBy = "taskid")
    private List<ServiceRecord> serviceRecordList;

    public Task() {
    }

    public Task(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getFinishtime() {
        return finishtime;
    }

    public void setFinishtime(Date finishtime) {
        this.finishtime = finishtime;
    }

    public Date getReminders() {
        return reminders;
    }

    public void setReminders(Date reminders) {
        this.reminders = reminders;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlTransient
    public List<Linkman> getLinkmanList() {
        return linkmanList;
    }

    public void setLinkmanList(List<Linkman> linkmanList) {
        this.linkmanList = linkmanList;
    }

    public Employee getAccepter() {
        return accepter;
    }

    public void setAccepter(Employee accepter) {
        this.accepter = accepter;
    }

    public Employee getSender() {
        return sender;
    }

    public void setSender(Employee sender) {
        this.sender = sender;
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
        if (!(object instanceof Task)) {
            return false;
        }
        Task other = (Task) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cloudgx.crm.entity.Task[ id=" + id + " ]";
    }
    
}

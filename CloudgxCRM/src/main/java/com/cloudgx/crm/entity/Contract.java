/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cloudgx.crm.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
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
@Table(name = "Contract")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Contract.findAll", query = "SELECT c FROM Contract c"),
    @NamedQuery(name = "Contract.findById", query = "SELECT c FROM Contract c WHERE c.id = :id"),
    @NamedQuery(name = "Contract.findByAmount", query = "SELECT c FROM Contract c WHERE c.amount = :amount"),
    @NamedQuery(name = "Contract.findByContent", query = "SELECT c FROM Contract c WHERE c.content = :content"),
    @NamedQuery(name = "Contract.findByDate", query = "SELECT c FROM Contract c WHERE c.date = :date"),
    @NamedQuery(name = "Contract.findByMaturity", query = "SELECT c FROM Contract c WHERE c.maturity = :maturity"),
    @NamedQuery(name = "Contract.findByType", query = "SELECT c FROM Contract c WHERE c.type = :type")})
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "amount")
    private BigInteger amount;
    @Size(max = 255)
    @Column(name = "content")
    private String content;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Column(name = "maturity")
    @Temporal(TemporalType.DATE)
    private Date maturity;
    @Column(name = "type")
    private Integer type;
    @OneToMany(mappedBy = "contractid")
    private List<Produnct> produnctList;
    @JoinColumn(name = "Custom_id", referencedColumnName = "id")
    @ManyToOne
    private Custom customid;
    @OneToMany(mappedBy = "contractid")
    private List<ServiceRecord> serviceRecordList;

    public Contract() {
    }

    public Contract(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getMaturity() {
        return maturity;
    }

    public void setMaturity(Date maturity) {
        this.maturity = maturity;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @XmlTransient
    public List<Produnct> getProdunctList() {
        return produnctList;
    }

    public void setProdunctList(List<Produnct> produnctList) {
        this.produnctList = produnctList;
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
        if (!(object instanceof Contract)) {
            return false;
        }
        Contract other = (Contract) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cloudgx.crm.entity.Contract[ id=" + id + " ]";
    }
    
}

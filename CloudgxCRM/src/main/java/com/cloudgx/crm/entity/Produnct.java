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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author moon
 */
@Entity
@Table(name = "produnct")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Produnct.findAll", query = "SELECT p FROM Produnct p"),
    @NamedQuery(name = "Produnct.findById", query = "SELECT p FROM Produnct p WHERE p.id = :id"),
    @NamedQuery(name = "Produnct.findByCdkey", query = "SELECT p FROM Produnct p WHERE p.cdkey = :cdkey"),
    @NamedQuery(name = "Produnct.findByName", query = "SELECT p FROM Produnct p WHERE p.name = :name"),
    @NamedQuery(name = "Produnct.findBySid", query = "SELECT p FROM Produnct p WHERE p.sid = :sid"),
    @NamedQuery(name = "Produnct.findBySidpwd", query = "SELECT p FROM Produnct p WHERE p.sidpwd = :sidpwd"),
    @NamedQuery(name = "Produnct.findBySn", query = "SELECT p FROM Produnct p WHERE p.sn = :sn"),
    @NamedQuery(name = "Produnct.findByVersion", query = "SELECT p FROM Produnct p WHERE p.version = :version")})
public class Produnct implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "cdkey")
    private String cdkey;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Size(max = 255)
    @Column(name = "sid")
    private String sid;
    @Size(max = 255)
    @Column(name = "sidpwd")
    private String sidpwd;
    @Size(max = 255)
    @Column(name = "sn")
    private String sn;
    @Size(max = 255)
    @Column(name = "version")
    private String version;
    @JoinColumn(name = "Contract_id", referencedColumnName = "id")
    @ManyToOne
    private Contract contractid;

    public Produnct() {
    }

    public Produnct(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCdkey() {
        return cdkey;
    }

    public void setCdkey(String cdkey) {
        this.cdkey = cdkey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSidpwd() {
        return sidpwd;
    }

    public void setSidpwd(String sidpwd) {
        this.sidpwd = sidpwd;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Contract getContractid() {
        return contractid;
    }

    public void setContractid(Contract contractid) {
        this.contractid = contractid;
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
        if (!(object instanceof Produnct)) {
            return false;
        }
        Produnct other = (Produnct) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cloudgx.crm.entity.Produnct[ id=" + id + " ]";
    }
    
}

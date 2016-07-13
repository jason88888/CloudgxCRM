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
@Table(name = "Custom")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Custom.findAll", query = "SELECT c FROM Custom c"),
    @NamedQuery(name = "Custom.findById", query = "SELECT c FROM Custom c WHERE c.id = :id"),
    @NamedQuery(name = "Custom.findByAddress", query = "SELECT c FROM Custom c WHERE c.address = :address"),
    @NamedQuery(name = "Custom.findByFax", query = "SELECT c FROM Custom c WHERE c.fax = :fax"),
    @NamedQuery(name = "Custom.findByName", query = "SELECT c FROM Custom c WHERE c.name = :name"),
    @NamedQuery(name = "Custom.findByTelephone", query = "SELECT c FROM Custom c WHERE c.telephone = :telephone")})
public class Custom implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "address")
    private String address;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="电话/传真格式无效, 应为 xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 255)
    @Column(name = "fax")
    private String fax;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Size(max = 255)
    @Column(name = "telephone")
    private String telephone;
    @OneToMany(mappedBy = "customid")
    private List<Linkman> linkmanList;
    @OneToMany(mappedBy = "customid")
    private List<Contract> contractList;

    public Custom() {
    }

    public Custom(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @XmlTransient
    public List<Linkman> getLinkmanList() {
        return linkmanList;
    }

    public void setLinkmanList(List<Linkman> linkmanList) {
        this.linkmanList = linkmanList;
    }

    @XmlTransient
    public List<Contract> getContractList() {
        return contractList;
    }

    public void setContractList(List<Contract> contractList) {
        this.contractList = contractList;
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
        if (!(object instanceof Custom)) {
            return false;
        }
        Custom other = (Custom) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.cloudgx.crm.entity.Custom[ id=" + id + " ]";
    }
    
}

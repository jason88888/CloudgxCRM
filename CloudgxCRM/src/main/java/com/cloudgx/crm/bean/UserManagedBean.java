package com.cloudgx.crm.bean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.cloudgx.crm.entity.User;
import com.cloudgx.crm.entity.UserFacade;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author moon
 */
@Named("user")
@RequestScoped
public class UserManagedBean implements Validator {

    @EJB
    private UserFacade userFacade;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Creates a new instance of LoginBean
     */
    public UserManagedBean() {
    }

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();

        User user = new User();
        user.setUsername(this.username);
        user.setPassword(this.password);
        List<User> users = this.userFacade.findAll();
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(user.getUsername())) {
                if (u.getPassword().equals(user.getPassword())) {
                    HttpSession session = (HttpSession) extContext.getSession(true);
                    session.setAttribute("user", user);
                    return "index";
                } else {
                    return "login";
                }
            }
        }
        return "register";
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext extContext = context.getExternalContext();
        HttpSession session = (HttpSession) extContext.getSession(true);
        User user = (User) session.getAttribute("user");
        if (user != null) {
            session.removeAttribute("user");
            return "index";
        } else {
            return "login";
        }
    }

    public String createUser() {
        if (username != null && password != null) {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            userFacade.create(user);
        }
        return "login";
    }

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        User user = new User();
        user.setUsername(this.username);
        user.setPassword(this.password);
        List<User> users = this.userFacade.findAll();
        for (User u : users) {
            if (u.getUsername().equalsIgnoreCase(user.getUsername())) {
                if (u.getPassword().equals(user.getPassword())) {
                    ((UIInput) component).setValid(true);
                } else {
                    ((UIInput) component).setValid(false);
                    FacesMessage message = new FacesMessage("密码错误！");
                    context.addMessage(component.getClientId(context), message);
                }
            }
        }
    }
}

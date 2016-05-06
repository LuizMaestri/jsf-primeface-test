package user.beans;

import exception.ConnectionException;
import user.UserDao;
import user.domain.User;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author luiz
 * @version 1
 * @since 03/05/16
 */

@ManagedBean(name = "UserBean")
@SessionScoped
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(String.valueOf(UserBean.class));

    private List<User> users;
    private User logged = new User();
    private User detail;
    private User edit;
    private User newUser;
    private boolean isLogged = false;
    private String password;

    public UserBean() {
    }

    public List<User> getUsers() {
        List<User> older = users != null? users : new ArrayList<>();
        isLogged = false;
        try {
            users = UserDao.getUsers();
        } catch (ConnectionException e) {
            users = older;
            logger.log(Level.WARNING, "Problemas de conexão!!");
        }
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getLogged() {
        return logged;
    }

    public void setLogged(User logged) {
        this.logged = logged;
    }

    public User getDetail() {
        return detail;
    }

    public void setDetail(User detail) {
        this.detail = detail;
    }

    public User getEdit() {
        return edit;
    }

    public void setEdit(User edit) {
        this.edit = edit;
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login(){
        try {
            logged = UserDao.auth(logged.getLogin(), logged.getPassword());
        } catch (ConnectionException e) {
            logger.log(Level.WARNING, "Problemas de conexão!!");
        }
        try {
            users = logged != null && logged.isAdmin() ? UserDao.getUsers(): new ArrayList<>();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        return logged != null && logged.isAuth() ? "listCall": null;
    }

    public String back(){
        getUsers();
        return "listUser";
    }

    public String details(int id){
        for (User user : users) {
            if (user.getId() == id) {
                detail = user;
                break;
            }
        }
        return "detailUser";
    }

    public String edition(){
        edit = detail;
        isLogged = logged.getId().equals(detail.getId());
        edit.setPassword(null);
        return "editUser";
    }

    public String create(){
        newUser = new User();
        return "createUser";
    }

    public String save(boolean insert){
        if (insert){
            if ((!password.equals("") && !newUser.getPassword().equals("")) && password.equals(newUser.getPassword())) return insert();
        } else {
            if (password.equals("") && edit.getPassword().equals("")) return update(false);
            if ((password != null && edit.getPassword() != null) && password.equals(newUser.getPassword())) return update(true);
        }
        return null;
    }

    private String update(boolean hasPass) {
        boolean update = false;
        try {
            update = UserDao.update(edit, hasPass);
            password = null;
            if (isLogged){
                logged = edit;
                isLogged = false;
            }
        } catch (ConnectionException e) {
            logger.log(Level.WARNING, "Problemas de conexão!!");
        }
        return update ? back() : null;
    }

    private String insert() {
        boolean insert = false;
        try {
            insert = UserDao.insert(newUser);
            password = null;
        } catch (ConnectionException e) {
            logger.log(Level.WARNING, "Problemas de conexão!!");
        }
        return insert ? back() : null;
    }

    private String delete(){
        try {
            UserDao.delete(detail.getId());
            return back();
        } catch (ConnectionException e) {
            logger.log(Level.WARNING, "Problemas de conexão!!");
        }
        return null;
    }

    public String logout(){
        logged = new User();
        return "login";
    }
}

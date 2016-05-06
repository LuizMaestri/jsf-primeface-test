package call.beans;

import user.domain.User;
import call.CallDao;
import call.domain.Call;
import call.domain.CallStatus;
import exception.ConnectionException;

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

@ManagedBean(name = "CallBean")
@SessionScoped
public class CallBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(String.valueOf(CallBean.class));

    private List<Call> calls;
    private Call newCall;
    private Call detail;
    private Call edit;
    private boolean refresh = true;

    public CallBean() {

    }

    public List<Call> getCalls() {
        if (refresh) {
            refresh = false;
            List<Call> older = calls != null? calls : new ArrayList<>();
            try {
                calls = CallDao.getCalls();
            } catch (ConnectionException e) {
                calls = older;
                logger.log(Level.WARNING, "Problemas de conexão!!");
            }
        }
        return calls;
    }

    public void setCalls(List<Call> calls) {
        this.calls = calls;
    }

    public Call getNewCall() {
        return newCall;
    }

    public void setNewCall(Call newCall) {
        this.newCall = newCall;
    }

    public Call getDetail() {
        return detail;
    }

    public void setDetail(Call detail) {
        this.detail = detail;
    }

    public Call getEdit() {
        return edit;
    }

    public void setEdit(Call edit) {
        this.edit = edit;
    }

    public String create(){
        newCall = new Call();
        return "createCall";
    }

    public String back(){
        newCall = null;
        detail = null;
        edit = null;
        refresh = true;
        getCalls();
        return "listCall";
    }

    public String save(Integer id, boolean insert){
        return insert ? insert(id) : update();
    }

    private String insert(int id){
        User request = new User();
        request.setId(id);
    newCall.setRequestUser(request);
    boolean insert = false;
    try {
        insert = CallDao.insert(newCall);
    } catch (ConnectionException e) {
        logger.log(Level.WARNING, "Problemas de conexão!!");
    }
    return insert ? back() : null;
}

    private String update(){
        boolean update = false;
        try {
            update = CallDao.update(edit);
        } catch (ConnectionException e) {
            logger.log(Level.WARNING, "Problemas de conexão!!");
        }
        return update ? back() : null;
    }

    public String details(int id){
        edit = null;
        for (Call call : calls) {
            if (call.getId() == id) {
                detail = call;
                break;
            }
        }
        return "detailCall";
    }

    public String edition(){
        edit = detail;
        return "editCall";
    }

    public String assignment(User user){
        detail.setStatus(CallStatus.OPEN);
        detail.setUser(user);
        try {
            CallDao.update(detail);
            return back();
        } catch (ConnectionException e) {
            logger.log(Level.WARNING, "Problemas de conexão!!");
        }
        return null;
    }

    public String unassigned(){
        detail.setStatus(CallStatus.REGISTER);
        detail.setUser(null);
        try {
            CallDao.update(detail);
            return back();
        } catch (ConnectionException e) {
            logger.log(Level.WARNING, "Problemas de conexão!!");
        }
        return null;
    }

    public String reopen(){
        detail.setStatus(CallStatus.REGISTER);
        detail.setUser(null);
        detail.setClose(null);
        detail.setSolution(null);
        try {
            CallDao.update(detail);
            return back();
        } catch (ConnectionException e) {
            logger.log(Level.WARNING, "Problemas de conexão!!");
        }
        return null;
    }

    public String close(){
        detail.setStatus(CallStatus.CLOSE);
        detail.setClose(System.currentTimeMillis());
        try {
            CallDao.update(detail);
            return back();
        } catch (ConnectionException e) {
            logger.log(Level.WARNING, "Problemas de conexão!!");
        }
        return null;
    }

    public String cancel(){
        detail.setStatus(CallStatus.CANCEL);
        try {
            CallDao.update(detail);
            return back();
        } catch (ConnectionException e) {
            logger.log(Level.WARNING, "Problemas de conexão!!");
        }
        return null;
    }

    public String delete(){
        try {
            CallDao.delete(detail.getId());
            return back();
        } catch (ConnectionException e) {
            logger.log(Level.WARNING, "Problemas de conexão!!");
        }
        return null;
    }
}

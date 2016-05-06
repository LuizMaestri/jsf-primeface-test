package utils;

import call.domain.CallPriority;
import call.domain.CallType;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

/**
 * @author luiz
 * @version 1
 * @since 04/05/16
 */

@ManagedBean(name = "Utils")
@ApplicationScoped
public class Utils implements Serializable{

    private static final long serialVersionUID = 1L;

    public CallType[] getTypes(){
        return CallType.values();
    }

    public CallPriority[] getPriorities(){
        return CallPriority.values();
    }
}

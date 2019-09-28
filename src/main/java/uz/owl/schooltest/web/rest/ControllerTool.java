package uz.owl.schooltest.web.rest;

import uz.owl.schooltest.exception.NotFoudException;

public class ControllerTool {
    public static void requireNotNull(Object o){
        if (o==null){
            throw new NotFoudException("Not Found");
        }
    }

    public static void requireNotNull(Object o, String msg){
        if (o==null){
            throw new NotFoudException(msg);
        }
    }
}

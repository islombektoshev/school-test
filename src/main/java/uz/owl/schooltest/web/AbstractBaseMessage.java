package uz.owl.schooltest.web;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class AbstractBaseMessage<C> {
    private final Map<Object, Object> _link = new HashMap<>();

    @JsonIgnore
    private C c;

    protected void setClass(C c){
        this.c = c;
    }

    public C putLink(Object key, Object value) {
         _link.put(key, value);
         return c;
    }

    public C self(Object o){
        _link.put("_self",o);
        return c;
    }

    public C all(Object o){
        _link.put("_all", o);
        return c;
    }
}

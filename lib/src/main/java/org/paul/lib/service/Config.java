package org.paul.lib.service;

import java.util.ArrayList;
import java.util.List;

public class Config {

    static final List<String> PRE_HOSTS=new ArrayList<>();

    void setPreHosts(List<String> list){
        if(null!=list){
            PRE_HOSTS.clear();
            PRE_HOSTS.addAll(list);
        }
    }
}

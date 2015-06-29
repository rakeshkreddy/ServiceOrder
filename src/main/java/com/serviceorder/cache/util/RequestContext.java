package com.serviceorder.cache.util;

/**
 * Created by rakesh on 6/29/15.
 */
public interface RequestContext {
    String getGtid();

    void setGtid(String var1);

    String getUsername();

    void setUsername(String var1);

    String getPassword();

    void setPassword(String var1);
}

package com.serviceorder.cache.util;


/**
 * Created by Rakesh Komulwad on 7/21/2014.
 */
public class ServiceCacheConfiguration {

  Boolean useCache = false;
  Integer dependencyServiceThreadPoolSize = Integer.valueOf(10);//allows caching by compiler
  Integer cacheWaitTimeOutInMillis = null;
  Integer timeToLive = null;


  public Boolean getUseCache() {
    return useCache;
  }

  public void setUseCache(Boolean useCache) {
    this.useCache = useCache;
  }

  public Integer getDependencyServiceThreadPoolSize() {
    return dependencyServiceThreadPoolSize;
  }

  public void setDependencyServiceThreadPoolSize(Integer dependencyServiceThreadPoolSize) {
    this.dependencyServiceThreadPoolSize = dependencyServiceThreadPoolSize;
  }

  public Integer getCacheWaitTimeOutInMillis() {
    return cacheWaitTimeOutInMillis;
  }

  public void setCacheWaitTimeOutInMillis(Integer cacheWaitTimeOutInMillis) {
    this.cacheWaitTimeOutInMillis = cacheWaitTimeOutInMillis;
  }

  public Integer getTimeToLive() {
    return timeToLive;
  }

  public void setTimeToLive(Integer timeToLive) {
    this.timeToLive = timeToLive;
  }
}

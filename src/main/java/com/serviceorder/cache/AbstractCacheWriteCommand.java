package com.serviceorder.cache;

import com.google.common.base.Throwables;

import com.serviceorder.cache.keygen.KeyGenerator;
import com.serviceorder.cache.repo.CacheRepository;
import com.serviceorder.cache.util.RequestContext;
import com.serviceorder.cache.util.ServiceCacheConfiguration;
import com.netflix.hystrix.HystrixCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.List;

/**
 * Generic write-to-cache implementation designed to be subclassed
 *
 * Created by Rakesh Komulwad on 6/12/2014.
 */
public abstract class AbstractCacheWriteCommand<R> extends HystrixCommand<List<R>> {
    private static final Logger log = LoggerFactory.getLogger(AbstractCacheWriteCommand.class);

    List<R> dataToWriteInCache = null;

    Object searchRequest = null;

  /**
   *
   * @param setter
   * @param dataToWriteInCache - Data to be written in the cache
   * @param searchRequest - Search Request which triggered the service call and eventual write to the cache
   */
    protected AbstractCacheWriteCommand(Setter setter,final List<R> dataToWriteInCache, final Object searchRequest){
        super(setter);
        this.dataToWriteInCache = dataToWriteInCache;
        this.searchRequest = searchRequest;
    }


    protected abstract RequestContext getRequestContext();
    protected abstract KeyGenerator getKeyGenerator();
    protected abstract CacheRepository getCacheRepository();
    protected abstract ServiceCacheConfiguration getServiceConfiguration();

    protected List<R> getDataToWriteInCache() {
        return dataToWriteInCache;
    }

    protected Object getSearchRequest() {
        return searchRequest;
    }

    @Override protected List<R> run() throws Exception {

        ServiceCacheConfiguration config = getServiceConfiguration();

        if(config.getUseCache() == false)
            return dataToWriteInCache;

        StopWatch sw = new StopWatch("Cache Write");
        final String gtid = getRequestContext().getGtid();
        String key = null;
        try{
            for (R r : dataToWriteInCache) {
                //save the data in the cache
                key = getKeyToWriteToCache(r, searchRequest);
                sw.start("Key "+key);
                getCacheRepository().save(key , r);
                sw.stop();
            }
        }
        catch(Throwable t){
            log.error("{} failed to write to cache ",gtid, t);
            Throwables.propagate(t);
        }

        log.info("{} {}", gtid, sw);
        return dataToWriteInCache;
    }

  private String getKeyToWriteToCache(R r, Object searchRequest) {
    return getKeyGenerator().extract(r, this.searchRequest);
  }

    /*
    @Override
    protected List<R> getFallback() {
        final String gtid = getRequestContext().getGtid();
        log.debug("{} Write to cache failed", gtid);
        return dataToWriteInCache; // Turn into silent failure
    }
    */
}

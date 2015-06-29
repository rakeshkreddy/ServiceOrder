package com.serviceorder.cache;

import com.google.common.base.Optional;

import com.serviceorder.cache.util.RequestContext;
import com.serviceorder.cache.util.ServiceCacheConfiguration;
import com.netflix.hystrix.HystrixCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Generic service lookup implementation designed to be subclassed
 *
 * Created by Rakesh Komulwad on 6/12/2014.
 */
public abstract class AbstractServiceLookUpCommand<R> extends HystrixCommand<List<R>> {
    private static final Logger log = LoggerFactory.getLogger(AbstractServiceLookUpCommand.class);

    Object searchRequest = null;

    protected AbstractServiceLookUpCommand(Setter setter, final Object searchRequest){
        super(setter);
        this.searchRequest = searchRequest;
    }

    protected abstract RequestContext getRequestContext();
    protected abstract Optional<? extends AbstractCacheWriteCommand> getCacheWriteCommand(List<R> dataToWriteInCache);

    protected abstract List<R> invokeService() throws Throwable;

    protected abstract void clearSearchCriteria();
    protected abstract ServiceCacheConfiguration getServiceConfiguration();

    @Override protected List<R> run() throws Exception {
        List<R> data =  null;
        final String gtid = getRequestContext().getGtid();
        try {
            //call the service
            data = invokeService();

            ServiceCacheConfiguration config = getServiceConfiguration();

            if(config.getUseCache() == false)
                return data;

            //store the info in cache
            if(data != null){
                try{
                    Optional<? extends AbstractCacheWriteCommand> writeCommand = getCacheWriteCommand(data);
                    if(writeCommand.isPresent())
                        writeCommand.get().execute();
                } catch(Throwable t){
                    log.error("{} failed to write to cache ",gtid, t);
                }
            }
            //clear the search criteria, so avoid any further searches
            clearSearchCriteria();

        } catch (Throwable t) {
            log.error("{} failed to lookup from service ",gtid, t);
            throw new Exception(t);
        }
        return data;
    }

    protected Object getSearchRequest() {
      return searchRequest;
    }
}

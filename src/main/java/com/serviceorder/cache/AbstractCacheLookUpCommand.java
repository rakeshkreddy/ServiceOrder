package com.serviceorder.cache;

import com.serviceorder.cache.keygen.KeyGenerator;
import com.serviceorder.cache.repo.CacheRepository;
import com.serviceorder.cache.util.RequestContext;
import com.serviceorder.cache.util.ServiceCacheConfiguration;
import com.netflix.hystrix.HystrixCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Generic Cache lookup implementation designed to be subclassed
 *
 * Created by Rakesh Komulwad on 6/12/2014.
 */
public abstract class AbstractCacheLookUpCommand<R> extends HystrixCommand<List<R>> {

  private static final Logger log = LoggerFactory.getLogger(AbstractCacheLookUpCommand.class);

  List searchCriteriaList = null;

  Object searchRequest = null;

  /**
   * @param setter             Hystrix configuration
   * @param searchCriteriaList - The Search Criteria List
   * @param searchRequest      - The Search Request
   */
  protected AbstractCacheLookUpCommand(Setter setter, final List searchCriteriaList, final Object searchRequest) {
    super(setter);
    this.searchCriteriaList = searchCriteriaList;
    this.searchRequest = searchRequest;
  }

  protected abstract RequestContext getRequestContext();

  protected abstract KeyGenerator getKeyGenerator();

  protected abstract CacheRepository getCacheRepository();

  //protected abstract List<R> getFallbackImplementation();
  protected abstract ServiceCacheConfiguration getServiceConfiguration();

  @Override
  protected List<R> run() {
    ServiceCacheConfiguration config = getServiceConfiguration();

    if (config.getUseCache() == false) {
      return Collections.emptyList();
    }

    StopWatch sw = new StopWatch("Cache Lookup");

    List<R> response = new ArrayList<R>();
    String key = null;
    final String gtid = getRequestContext().getGtid();
    Object searchCriteria = null;
    List<R> dataFromCache = null;
    R domain = null;
    try {
      Map<String,Object> keySearchCriteriaMap = new TreeMap<String,Object>();
      //capture all the keys to query
      for (Iterator iterator = searchCriteriaList.iterator(); iterator.hasNext(); ) {
        searchCriteria = iterator.next();
        key = getKeyGenerator().extract(searchCriteria, searchRequest);
        keySearchCriteriaMap.put(key, searchCriteria);
      }

      //query the cache
      Set<String> keysToQuery = keySearchCriteriaMap.keySet();
      log.debug("{} Querying cache for {}", gtid, keysToQuery);

      sw.start("CacheLookup");
      dataFromCache = getCacheRepository().get(keysToQuery);
      sw.stop();

      //Condition to handle cache timeouts, if the cache is slow the request might fallback
      //in case of timeouts this thread does not abort
      if(isResponseTimedOut() == false) {
        //match the results
        Iterator<String> keysIterator = keysToQuery.iterator();
        Iterator<R> dataIterator = dataFromCache.iterator();

        while(keysIterator.hasNext()){
          key = keysIterator.next();
          searchCriteria = keySearchCriteriaMap.get(key);

          if(dataIterator.hasNext())
            domain = dataIterator.next();
          //data found in the cache
          if(domain != null){
            log.debug("{} CacheHit {}", gtid ,key);
            //remove the search criteria for which data is retrieved
            searchCriteriaList.remove(searchCriteria);
            response.add(domain);
          } else {
            log.warn("{} CacheMiss {}", gtid, key);
          }
        }
      } else {
        log.error("{} Cache TimedOut for {}", gtid, keysToQuery);
      }
    } catch (Throwable t) {
      log.error("{} failed to lookup from cache ", gtid, t);
      //throw the error so that caller can fallback
      throw t;
    }
    log.info("{} {}", gtid, sw);
    return response;
  }

  /**
   * To be overwritten by subclasses to provide any fallback implementation
   */
  @Override
  protected List<R> getFallback() {
    log.warn("{} Issue with Cache falling back and returning empty result", getRequestContext().getGtid());
    return Collections.EMPTY_LIST;
  }

}

package com.serviceorder.cache.keygen;

/**
 * Base interface for Cache Key Generation
 *
 * Created by Rakesh Komulwad on 6/3/2014.
 */
public interface KeyGenerator {
    String extract(Object searchCriteria, Object searchRequest);
}

package com.serviceorder.cache.repo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * Base interface for all CRUD operations in Redis
 *
 * Created by Rakesh Komulwad on 6/4/2014.
 */
public interface CacheRepository<T> {
    T get(String key);

    List<T> get(Set<String> keys);

    void save(String key, T domain);

    void save(Map<String, T> domainData);

    void delete(String key);
}

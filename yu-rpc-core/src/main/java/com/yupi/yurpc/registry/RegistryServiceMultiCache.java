package com.yupi.yurpc.registry;

import com.yupi.yurpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RegistryServiceMultiCache {
    /**
     * 服务缓存
     */
    Map<String, List<ServiceMetaInfo>> serviceCache = new ConcurrentHashMap<>();

    /**
     * 写缓存
     * @param serviceKey
     * @param newServiceCache
     */
    void writeCache(String serviceKey, List<ServiceMetaInfo> newServiceCache) {
        serviceCache.put(serviceKey, newServiceCache);
    }

    /**
     * 读缓存
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> readCache(String serviceKey) {
        return serviceCache.get(serviceKey);
    }

    /**
     * 清空缓存
     */
    void clearCache(String serviceKey) {
        serviceCache.remove(serviceKey);
    }


}

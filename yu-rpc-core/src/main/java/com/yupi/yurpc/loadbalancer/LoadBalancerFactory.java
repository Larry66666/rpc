package com.yupi.yurpc.loadbalancer;

import com.yupi.yurpc.serializer.Serializer;
import com.yupi.yurpc.spi.SpiLoader;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 负载均衡器工厂（工厂模式，用于获取负载均衡器对象）
 */
public class LoadBalancerFactory {

    /**
     * 使用 AtomicReference 来确保线程安全的懒加载
     */
    private static final AtomicReference<LoadBalancer> loadBalancerRef = new AtomicReference<>();

    /**
     * 默认序列化器
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCER = new RoundRobinLoadBalancer();

    /**
     * 获取实例(双重检查锁定)
     * @param key
     * @return
     */
    public static LoadBalancer getInstance(String key) {
        LoadBalancer loadBalancer = loadBalancerRef.get();
        if (loadBalancer == null) {
            synchronized (LoadBalancerFactory.class) {
                loadBalancer = loadBalancerRef.get();
                if (loadBalancer == null) {
                    loadBalancer = SpiLoader.getInstance(LoadBalancer.class, key);
                    loadBalancerRef.set(loadBalancer);
                }
            }
        }
        return loadBalancer;
    }
}

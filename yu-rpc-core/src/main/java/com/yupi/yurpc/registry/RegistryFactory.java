package com.yupi.yurpc.registry;

import com.yupi.yurpc.RpcApplication;
import com.yupi.yurpc.spi.SpiLoader;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 注册中心工厂（用于获取注册中心对象）
 */
public class RegistryFactory {

    /**
     * 使用 AtomicReference 来确保线程安全的懒加载
     */
    private static final AtomicReference<Registry> registryRef = new AtomicReference();

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 获取实例(双重检查锁定)
     * @param key
     * @return
     */
    public static Registry getInstance(String key) {
        Registry registry = registryRef.get();
        if (registry == null) {
            synchronized (RegistryFactory.class) {
                registry = registryRef.get();
                if (registry == null) {
                    registry = SpiLoader.getInstance(Registry.class, key);
                    registryRef.set(registry);
                }
            }
        }
        return registry;
    }
}

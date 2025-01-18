package com.yupi.yurpc.fault.retry;

import com.yupi.yurpc.spi.SpiLoader;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 重试策略工厂（用于获取重试器对象）
 */
public class RetryStrategyFactory {
    /**
     * 使用 AtomicReference 来确保线程安全的懒加载
     */
    private static final AtomicReference<RetryStrategy> retryStrategyRef = new AtomicReference<>();

    /**
     * 默认序列化器
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    /**
     * 获取实例
     * @param key
     * @return
     */
    public static RetryStrategy getInstance(String key) {
        RetryStrategy retryStrategy = retryStrategyRef.get();
        if (retryStrategy == null) {
            synchronized (RetryStrategyFactory.class) {
                retryStrategy = retryStrategyRef.get();
                if (retryStrategy == null) {
                    retryStrategy = SpiLoader.getInstance(RetryStrategy.class, key);
                    retryStrategyRef.compareAndSet(null, retryStrategy);
                }
            }
        }
        return retryStrategy;
    }
}

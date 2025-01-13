package com.yupi.yurpc.serializer;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.yupi.yurpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 序列化器工厂（用于获取序列化器对象）
 * 序列化器对象是可以复用的，没必要每次执行序列化操作前都创建一个新的对象。所以可以使用设计模式中的 工厂模式 + 单例模式 来简化创建和获取序列化器对象的操作
 */
public class SerializerFactory {

    /*static {
        SpiLoader.load(Serializer.class); // 一次性加载了所有META-INF中声明的所有序列化器
    }*/
    /**
     * 使用 AtomicReference 来确保线程安全的懒加载
     */
    private static final AtomicReference<Serializer> serializerRef = new AtomicReference<>();

    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    /**
     * 获取实例(双重检查锁定)
     * @param key
     * @return
     */
    public static Serializer getInstance(String key) {
        Serializer serializer = serializerRef.get();
        if (serializer == null) {
            synchronized (SerializerFactory.class) {
                serializer = serializerRef.get();
                if (serializer == null) {
                    serializer = SpiLoader.getInstance(Serializer.class, key);
                    serializerRef.set(serializer);
                }
            }
        }
        return serializer;
    }
}

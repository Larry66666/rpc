package com.yupi.yurpc.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.yupi.yurpc.RpcApplication;
import com.yupi.yurpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SPI 加载器（支持键值对映射）
 */
@Slf4j
public class SpiLoader {

    /**
     * 存储已加载的类：接口名 =>（key => 实现类）
     */
    private static Map<String, Map<String, Class<?>>> loaderMap = new ConcurrentHashMap<>(); // com.yupi.yurpc.serializer.Serializer, "jdk", com.yupi.yurpc.serializer.Serializer.JdkSerializer

    /**
     * 对象实例缓存（避免重复 new），类路径 => 对象实例，单例模式
     */
    private static Map<String, Object> instanceCache = new ConcurrentHashMap<>(); // "com.yupi.yurpc.serializer.Serializer.JdkSerializer", new JdkSerializer()

    /**
     * 系统 SPI 目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户自定义 SPI 目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 扫描路径
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_CUSTOM_SPI_DIR, RPC_SYSTEM_SPI_DIR};

    /**
     * 动态加载的类列表
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    /**
     * 加载所有类型
     */
//    public static void loadAll() {
//        log.info("加载所有 SPI");
//        for (Class<?> aClass : LOAD_CLASS_LIST) {
//            load(aClass);
//        }
//    }

    /**
     * 获取某个接口的实例
     *
     * @param tClass
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<?> tClass, String key) {
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClassName);
        if (keyClassMap == null) {
            load(tClass, key);
        }
        keyClassMap = loaderMap.get(tClassName);
        Class<?> implClass = keyClassMap.get(key);
        if (implClass == null) {
            throw new RuntimeException(String.format("SpiLoader 的 %s 不存在 key=%s 的类型", tClassName, key));
        }
        String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClassName)) {
            try {
                instanceCache.put(implClassName, implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                String errorMsg = String.format("%s 类实例化失败", implClassName);
                throw new RuntimeException(errorMsg, e);
            }
        }
        return (T) instanceCache.get(implClassName);
    }

    /**
     * 加载某个类型(懒加载)
     *
     * @param loadClass
     * @throws IOException
     */
    public static Class<?> load(Class<?> loadClass, String key) {
        String tClassName = loadClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.computeIfAbsent(tClassName, k -> new HashMap<>());
        Class<?> implClass = keyClassMap.get(key);
        if (implClass == null) {
            for (String scanDir : SCAN_DIRS) {
                List<URL> resources = ResourceUtil.getResources(scanDir + tClassName);
                for (URL resource : resources) {
                    try (InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                         BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            String[] strArray = line.split("=");
                            if (strArray.length > 1 && strArray[0].equals(key)) {
                                String className = strArray[1];
                                implClass = Class.forName(className);
                                keyClassMap.put(key, implClass);
                                log.info("加载序列化器: key={}, 类名={}", key, className);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        log.error("spi resource load error", e);
                    }
                }
                if (implClass != null) {
                    break;
                }
            }
        }
        if (implClass == null) {
            throw new RuntimeException(String.format("SpiLoader 的 %s 不存在 key=%s 的类型", tClassName, key));
        }
        return implClass;
    }
}

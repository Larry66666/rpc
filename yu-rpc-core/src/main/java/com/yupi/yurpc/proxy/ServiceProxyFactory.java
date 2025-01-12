package com.yupi.yurpc.proxy;

import com.yupi.yurpc.RpcApplication;
import com.yupi.yurpc.config.RpcConfig;

import java.lang.reflect.Proxy;

/**
 * 服务代理工厂（用于创建代理对象）:根据指定类创建动态代理对象
 */
public class ServiceProxyFactory {

    /**
     * 根据服务类获取代理对象
     * @param serviceClass
     * @return
     * @param <T>
     */
    public static <T> T getProxy(Class<T> serviceClass) {
        if (RpcApplication.getRpcConfig().isMock()) {
            return getMockProxy(serviceClass);
        }
        else return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, new ServiceProxy());
    }

    private static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(), new Class[]{serviceClass}, new MockServiceProxy());
    }
}

package com.yupi.yurpc.proxy;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Mock 服务代理（JDK 动态代理）
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {
    private static final Faker faker = new Faker();

    /**
     * 调用代理
     * @param proxy the proxy instance that the method was invoked on
     *
     * @param method the {@code Method} instance corresponding to
     * the interface method invoked on the proxy instance.  The declaring
     * class of the {@code Method} object will be the interface that
     * the method was declared in, which may be a superinterface of the
     * proxy interface that the proxy class inherits the method through.
     *
     * @param args an array of objects containing the values of the
     * arguments passed in the method invocation on the proxy instance,
     * or {@code null} if interface method takes no arguments.
     * Arguments of primitive types are wrapped in instances of the
     * appropriate primitive wrapper class, such as
     * {@code java.lang.Integer} or {@code java.lang.Boolean}.
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 根据方法的返回值类型，生成特定的默认值对象
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke {}", method.getName());
        return getDefaultObject(methodReturnType);
    }

    /**
     * 利用Faker库生成指定类型的默认值对象
     * @param type
     * @return
     */
    private Object getDefaultObject(Class<?> type) {
        // 基本类型
        if (type.isPrimitive()) {
            if (type == boolean.class) {
                return faker.random().nextBoolean();
            } else if (type == byte.class) {
                // 生成一个在byte取值范围内的int值，然后再安全地转换为byte
                int value = faker.random().nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE);
                return (byte) value;
            } else if (type == char.class) {
                return faker.chuckNorris().fact().charAt(0);
            } else if (type == short.class) {
                // 生成一个在short取值范围内的int值，然后再安全地转换为short
                int value = faker.random().nextInt(Short.MIN_VALUE, Short.MAX_VALUE);
                return (short) value;
            } else if (type == int.class) {
                return faker.random().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE);
            } else if (type == long.class) {
                return faker.random().nextLong();
            } else if (type == float.class) {
                return (float) faker.random().nextDouble();
            } else if (type == double.class) {
                return faker.random().nextDouble();
            }
        } else if (type == String.class) {
            faker.lorem().sentence();
        }
        // 对象类型
        return null;
    }
}

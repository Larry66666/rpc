package com.yupi.example.consumer;

import com.yupi.example.common.model.User;
import com.yupi.example.common.service.UserService;
import com.yupi.yurpc.config.RpcConfig;
import com.yupi.yurpc.constant.RpcConstant;
import com.yupi.yurpc.proxy.ServiceProxyFactory;
import com.yupi.yurpc.utils.ConfigUtils;

/**
 * 简易服务消费者示例(rpc-core)
 */
public class ConsumerExample {
    public static void main(String[] args) {
        // 加载配置
        RpcConfig rpc = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        System.out.println(rpc);

        // 静态代理
        // UserService userService = new UserServiceProxy();
        // 动态代理
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("yupi");
        // 调用
        User newUser = userService.getUser(user);
        if (newUser != null) {
            System.out.println(user.getName());
        } else {
            System.out.println("user is null");
        }

        long number = userService.getNumber();
        System.out.println(number);
    }
}

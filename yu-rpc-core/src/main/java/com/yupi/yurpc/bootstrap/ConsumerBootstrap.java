package com.yupi.yurpc.bootstrap;

import com.yupi.yurpc.RpcApplication;

public class ConsumerBootstrap {

    /**
     * 初始化
     */
    public static void init() {
        // RPC 框架初始化（配置和注册中心）
        RpcApplication.init();
    }
}

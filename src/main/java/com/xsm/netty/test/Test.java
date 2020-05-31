package com.xsm.netty.test;

import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;

/**
 * @author xsm
 * @Date 2020/5/29 22:50
 */
public class Test {
    public static void main(String[] args) {
        int result = Math.max(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));

        System.out.println(result);
    }
}

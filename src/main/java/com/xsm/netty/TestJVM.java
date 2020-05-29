package com.xsm.netty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: xsm
 * @create: 2020-04-17
 * @description: 内存溢出 dump文件测试
 */
public class TestJVM {

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject("你好"));
        }
    }

    static class OOMObject {
        private String name;

        public OOMObject(String name) {
            this.name = name;
        }
    }
}

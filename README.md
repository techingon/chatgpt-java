# chatgpt-java
一、请自行申请app key

        虽然会有些复杂

二、项目依赖

负责json处理和网络请求
fastjson
hutool-http
二、核心类

ChatAPI，封装会话信息，向ChatGPT API发起请求。
2.1 两个主要的配置信息
2.2 缓存会话上下文信息
2.3 构造方法中，初始化一些预置信息，约束ChatGPT的角色。
2.4 核心发送代码
其中最后把本轮的请求返回加回会话缓存。

三、测试代码

public static void main(String[] args) {
    ChatAPI c = new ChatAPI();
    while(true){
        System.out.print("我:");
        Scanner input=new Scanner(System.in);
        String str=input.next();
        System.out.print("ChaGPT:");
        System.out.println(c.chat(str));
    }
}
其他详细介绍参考：https://zhuanlan.zhihu.com/p/613744177

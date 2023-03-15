# chatgpt-java

详细介绍参考：https://zhuanlan.zhihu.com/p/613744177

一、请自行申请app key

        虽然会有些复杂

二、项目依赖

负责json处理和网络请求

<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.83</version>
</dependency>

<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-http</artifactId>
    <version>5.8.15</version>
</dependency>
二、核心类

ChatAPI，封装会话信息，向ChatGPT API发起请求。
2.1 两个主要的配置信息
/**
 * chatgpt api url
 */
String chatEndpoint = "https://api.openai.com/v1/chat/completions";
/**
 * api key
 */
String apiKey = "Bearer xxxxxxxxx";
 2.2 缓存会话上下文信息

List<Map<String, String>> dataList = new ArrayList<>();
2.3 构造方法中，初始化一些预置信息，约束ChatGPT的角色。

ChatAPI(){
    dataList.add(new HashMap<String, String>(){{
        put("role", "system");
        put("content", "你是一个游戏中可以跟user友好互动的宠物小狗，你很忠诚，也很幽默，经常会说一些笑话逗主人开心，你现在已经2岁了。");
    }});
}
2.4 核心发送代码

    /**
     * 发送消息
     */
    public String chat(String txt) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("model", "gpt-3.5-turbo");
        dataList.add(new HashMap<String, String>(){{
            put("role", "user");
            put("content", txt);
        }});
        paramMap.put("messages", dataList);
        JSONObject message = null;
        try {
            String body = HttpRequest.post(chatEndpoint)
                    .header("Authorization", apiKey)
                    .header("Content-Type", "application/json")
                    .body(JSON.toJSONString(paramMap))
                    .execute()
                    .body();
            JSONObject jsonObject = JSONObject.parseObject(body);
            System.out.println(jsonObject.toJSONString());
            JSONArray choices = jsonObject.getJSONArray("choices");
            JSONObject result = choices.getJSONObject(0);
            message = result.getJSONObject("message");
 
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        String res = message.getString("content");
        dataList.add(new HashMap<String, String>(){{
            put("role", "assistant");
            put("content", res);
        }});
        return res;
    }
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

package com.echx.chatgpt;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class ChatAPI {

    /**
     * chatgpt api url
     */
    String chatEndpoint = "https://api.openai.com/v1/chat/completions";
    /**
     * api key
     */
    String apiKey = "Bearer xxxxxxxxx";

    List<Map<String, String>> dataList = new ArrayList<>();

    ChatAPI(){
        dataList.add(new HashMap<String, String>(){{
            put("role", "system");
            put("content", "你是一个游戏中可以跟user友好互动的宠物小狗，你很忠诚，也很幽默，经常会说一些笑话逗主人开心，你现在已经2岁了。");
        }});
    }

    /**
     * 发送消息
     *
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
}

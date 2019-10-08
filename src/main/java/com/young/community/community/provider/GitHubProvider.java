package com.young.community.community.provider;

import com.alibaba.fastjson.JSON;
import com.young.community.community.dto.AccessTokenDTO;
import com.young.community.community.dto.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

@Component
public class GitHubProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON.toJSONString(accessTokenDTO), mediaType);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string().split("[=&]")[1];
            return string;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GitHubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            return JSON.parseObject(string, GitHubUser.class);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.zcy.iqconnect.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcy.iqconnect.core.*;
import com.zcy.iqconnect.model.HttpHeader;
import com.zcy.iqconnect.model.IqOptionLoginResponse;
import com.zcy.iqconnect.service.IqService;
import com.zcy.iqconnect.util.HttpClientUtil;
import com.zcy.iqconnect.util.StringUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * create date : 2019/1/8
 */
@Service
public class IqServiceImpl implements IqService {
    /**
     * 根据email和密码登录iqoption，获取ssid
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public String login(String username, String password) throws Exception {

        //判断参数
        if (StringUtil.isEmpty(username) || StringUtil.isEmpty(password)) {
            throw new RuntimeException("iqoption的用户名或密码为空，无法登录获取ssid");
        }

        //返回结果
        String ssid = "";

        //设置头信息
        HttpHeader httpHeader = new HttpHeader();
        httpHeader.setContentType("application/x-www-form-urlencoded");
        //httpHeader.setAccept("*/*");
/*        httpHeader.setAcceptEncoding("gzip, deflate, br");
        httpHeader.setAcceptLanguage("zh-CN,zh;q=0.9");
        httpHeader.setConnection("keep-alive");
        httpHeader.setHost("auth.iqoption.com");
        httpHeader.setOrigin("https://iqoption.com");
        httpHeader.setReferer("https://iqoption.com/en");
        httpHeader.setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");*/

        //请求地址
        String url = Constant.LOGIN_URL;

        //设置参数
        Map<String, Object> params = new HashMap<>();
        params.put("email", username);
        params.put("password", password);
        //params.put("google_client_id", "1621745674.1545026677");

        String responseString = HttpClientUtil.doPost(url, params, httpHeader);

        //将String转为登录返回对象，获取ssid
        ObjectMapper objectMapper = new ObjectMapper();
        IqOptionLoginResponse iqOptionLoginResponse = objectMapper.readValue(responseString, new TypeReference<IqOptionLoginResponse>() {
        });
        if (iqOptionLoginResponse != null) {
            IqOptionLoginResponse.Data data = iqOptionLoginResponse.getData();
            if (data != null) {
                ssid = data.getSsid();
            }
        }
        return ssid;
    }
}

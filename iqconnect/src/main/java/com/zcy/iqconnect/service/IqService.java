package com.zcy.iqconnect.service;

/**
 * create date : 2019/1/8
 */
public interface IqService {
    /**
     * 根据email和密码登录iqoption，获取ssid
     *
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    String login(String username, String password) throws Exception;
}

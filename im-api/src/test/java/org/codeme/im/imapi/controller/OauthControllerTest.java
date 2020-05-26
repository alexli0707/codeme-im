package org.codeme.im.imapi.controller;

import org.codeme.im.imcommon.http.util.JsonTools;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

/**
 * OauthControllerTest
 *
 * @author walker lee
 * @date 2020/5/25
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OauthControllerTest {

    @LocalServerPort
    private int serverPort;

    private String url;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void testRegister() {
        url = "http://127.0.0.1:" + serverPort;
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        Map params = new HashMap(3);
        params.put("username", "admin3");
        params.put("password", "111111");
        params.put("confirmed_password", "111111");
        String requestJson = JsonTools.simpleObjToStr(params);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        System.out.println(url);
        String result = restTemplate.postForObject(url + "/api/oauth/register", entity, String.class);
        System.out.println(result);
    }
}

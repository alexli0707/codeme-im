package org.codeme.im.imclient.service.impl;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.codeme.im.imclient.config.IMClientProjectProperties;
import org.codeme.im.imclient.service.ApiService;
import org.codeme.im.imcommon.constant.RestHttpErrorResponseEnum;
import org.codeme.im.imcommon.http.RestHttpResponse;
import org.codeme.im.imcommon.http.RestMeta;
import org.codeme.im.imcommon.http.exp.RestHttpException;
import org.codeme.im.imcommon.http.util.JsonTools;
import org.codeme.im.imcommon.model.vo.AccessToken;
import org.codeme.im.imcommon.model.vo.params.OauthParams;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * ApiServiceImpl
 *
 * @author walker lee
 * @date 2020/5/20
 */
@Service
@Slf4j
public class ApiServiceImpl {

    private static final String BEARER = "Bearer %s";

    private ApiService apiService;


    public ApiServiceImpl(IMClientProjectProperties imClientProjectProperties) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(imClientProjectProperties.getApiServerUrl()).client(client)
                .addConverterFactory(GsonConverterFactory.create(gson)).build();
        this.apiService = retrofit.create(ApiService.class);
    }

    /**
     * 授权换取token
     *
     * @param username
     * @param password
     * @return
     * @throws IOException
     */
    public RestHttpResponse<AccessToken> oauth(String username, String password) throws IOException, RestHttpException {
        Call<RestHttpResponse<AccessToken>> userInfoCall = apiService.oauth(new OauthParams(username, password));
        Response<RestHttpResponse<AccessToken>> response = userInfoCall.execute();
        return response.body();
    }

    /**
     * 获取imserver地址
     * @param accessToken
     * @return
     * @throws IOException
     * @throws RestHttpException
     */
    public RestHttpResponse<String> getIMServerUrl(String accessToken) throws IOException, RestHttpException {
        Call<RestHttpResponse<String>> call = apiService.getIMServerUrl(String.format(BEARER, accessToken));
        Response<RestHttpResponse<String>> response = call.execute();
        return response.body();
    }


    private void handleRestHttpResponseError(Response response, boolean throwError) throws RestHttpException {
        String errorMsg = null;
        try {
            errorMsg = response.errorBody().string();
            log.error("getInternalCustomerInfo fail:" + errorMsg);
        } catch (Exception e) {
            log.error("getInternalCustomerInfo fail witht Exception");
            throw new RestHttpException(RestHttpErrorResponseEnum.INTERNAL_API_ERROR);
        }
        if (throwError) {
            RestHttpResponse hzHttpResponse = JsonTools.strToObject(errorMsg, RestHttpResponse.class);
            int httpCode = response.code();
            if (null == hzHttpResponse) {
                throw new RestHttpException(RestHttpErrorResponseEnum.INTERNAL_API_ERROR);
            }
            RestMeta hzMeta = hzHttpResponse.getMeta();
            if (null == hzMeta) {
                throw new RestHttpException(RestHttpErrorResponseEnum.INTERNAL_API_ERROR);
            }
            throw new RestHttpException(hzMeta.getMessage(), hzMeta.getCode(), httpCode);
        }
    }
}

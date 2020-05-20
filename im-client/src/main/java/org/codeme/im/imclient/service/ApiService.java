package org.codeme.im.imclient.service;

import org.codeme.im.imcommon.constant.UriConstant;
import org.codeme.im.imcommon.http.RestHttpResponse;
import org.codeme.im.imcommon.model.vo.AccessToken;
import org.codeme.im.imcommon.model.vo.params.OauthParams;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * ApiService
 *
 * @author walker lee
 * @date 2020/5/20
 */
public interface ApiService {

    @POST(UriConstant.API + "/oauth")
    Call<RestHttpResponse<AccessToken>> oauth(@Body OauthParams oauthParams);
}

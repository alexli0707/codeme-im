package org.codeme.im.imapi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 授权token
 * </p>
 *
 * @author codeme
 * @since 2020-05-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AccessToken implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * token
     */
    private String accessToken;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 过期时间,默认是秒
     */
    private Integer expiresIn;

    /**
     * token类型
     */
    private String tokenType;

    /**
     * 创建时间
     */
    private LocalDateTime createAt;

    /**
     * 更新时间
     */
    private LocalDateTime updateAt;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 系统类型: 1-android(手机) 2-ios 3-web  4-android(平板) 5-ipad
     */
    private Integer osType;

    /**
     * 厂商名称
     */
    private String manufacturer;

    /**
     * 设备名称
     */
    private String device;

    /**
     * 登录ip
     */
    private String ip;

    /**
     * 国家
     */
    private String country;

    /**
     * 城市
     */
    private String city;

    /**
     * 地区
     */
    private String area;

    /**
     * 区域
     */
    private String region;

    /**
     * 运营商
     */
    private String isp;


    public AccessToken() {
    }

    public AccessToken(Long userId, String accessToken, String refreshToken, int expiresIn) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }


}

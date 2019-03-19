package com.cloud.nets.beans;

import com.cloud.nets.enums.TokenLocation;

/**
 * Author lijinghuan
 * Email:ljh0576123@163.com
 * CreateTime:2019/3/19
 * Description:token属性配置(接口请求验证时有用)
 * Modifier:
 * ModifyContent:
 */
public class TokenProperties {

    /**
     * token信息携带位置
     */
    private TokenLocation location;
    /**
     * token名称
     */
    private String tokenName = "token";

    public TokenLocation getLocation() {
        if (location == null) {
            location = TokenLocation.header;
        }
        return location;
    }

    public void setLocation(TokenLocation location) {
        this.location = location;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }
}

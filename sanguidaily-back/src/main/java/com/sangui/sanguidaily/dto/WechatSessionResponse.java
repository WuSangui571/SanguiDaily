package com.sangui.sanguidaily.dto;

public record WechatSessionResponse(
    String openid,
    String sessionKey,
    Integer errcode,
    String errmsg
) {}

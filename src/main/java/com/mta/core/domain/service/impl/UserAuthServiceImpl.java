package com.mta.core.domain.service.impl;

import com.mta.core.domain.request.LoginRequest;
import com.mta.core.domain.request.RegisterRequest;
import com.mta.core.domain.response.RegisterResponse;
import com.mta.core.domain.service.UserAuthService;

public class UserAuthServiceImpl implements UserAuthService {
    @Override
    public RegisterResponse register(RegisterRequest request) {
        return null;
    }

    @Override
    public RegisterResponse login(LoginRequest request) {
        return null;
    }

    @Override
    public String refreshToken(String verifyToken) {
        return "";
    }
}

package com.mta.core.domain.service;

import com.mta.core.domain.request.LoginRequest;
import com.mta.core.domain.request.RegisterRequest;
import com.mta.core.domain.response.LoginResponse;
import com.mta.core.domain.response.RegisterResponse;

public interface UserAuthService {

   RegisterResponse register(RegisterRequest request);

   LoginResponse login(LoginRequest request);

   String refreshToken(String verifyToken);
}

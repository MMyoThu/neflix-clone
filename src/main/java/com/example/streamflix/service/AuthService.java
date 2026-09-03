package com.example.streamflix.service;

import com.example.streamflix.dto.RegisterForm;
import com.example.streamflix.entity.User;

public interface AuthService {

    User register(RegisterForm form);
}

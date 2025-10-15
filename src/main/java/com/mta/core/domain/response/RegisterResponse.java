package com.mta.core.domain.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RegisterResponse {

   private String registerToken;
   private String verifyToken;
   private String userId;
   private String email;
}

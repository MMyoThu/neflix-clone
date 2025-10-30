package com.mta.core.domain.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {

   private Long userId;
   private String email;
   private boolean success;
   private String message;
}

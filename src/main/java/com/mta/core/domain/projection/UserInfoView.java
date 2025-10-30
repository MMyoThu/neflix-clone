package com.mta.core.domain.projection;

import java.util.Date;

public interface UserInfoView {
    String getEmail();
    String getFirstName();
    String getLastName();
    Date getRegDt();
    Long getId();
    String getPassword();
}

package com.mta.core.domain.repository;

import com.mta.core.domain.entity.UserInfo;
import com.mta.core.domain.projection.UserInfoView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfoView findByEmail(String email);
}

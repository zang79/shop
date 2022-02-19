package com.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;
//스프링 DATA JPA의 Auditing 기능 = 등록일 수정일 등록자 수정자를 자동 입력해준다. Auditin 기능을 사용하기 위한 Config 파일 : AuditConfig.java

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if(authentication != null){
            userId = authentication.getName();//현재 로그인 한 사용자의 정보를 조회해 사용자의 이름을 등록자와수정자로 지정한다.

        }
        return Optional.of(userId);
    }

}
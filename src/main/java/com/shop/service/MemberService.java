package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
/*
비즈니스 로직을 담당하는 서비스 계층 클래스에 @Transcational 어노테이션을 선언해 로직을 처리하다 에러가 발생했다면, 변경된
데이터를 로직을 수행하기 이전 상태로 콜백시켜준다.
*/
@Transactional
/*빈을 주입하는 방법으로는 @Autowired 어노테이션을 이용하거나, 필드 주입(Setter), 생성자 주입을 이용하는 법이있다.
@RequiredArgsConstructor 어노테이션은 final이나 @NonNull이 붙은 필드에 생정자를 생성해준다
빈에 생성자가 1개이고 생성자의 파라미터 타입이 빈으로 등록 가능하다면 @Autowired 어노테이션 없이 의존성 주입이 가능하다.*/
@RequiredArgsConstructor
public class MemberService implements UserDetailsService { //MemberService가 UseDetailService를 구현하도록 implements해준다.
    private final MemberRepository memberRepository;
    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }
    //이미 가입된 회원 예외 발생
    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
    //UserDetailsService인터페이스의 loadUserByUsername() 메소드를 오버라이딩 한다.. 로그인할 유저의 email을 파라미터로 받는다.
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder()//UserDetail을 구현하고 있는 User객체를 반환해준다. User 객체를 생성하기 위해서 생성자로 회원의 이메일, 비밀번호 role을 넘긴다.
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}

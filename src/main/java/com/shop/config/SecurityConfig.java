package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
/*WebSecurityConfigurerAdapoter를 상속받는 클래스에 @EnableWebSecurity 어노테이션을 선언하면 SpringSecurityFilterChain이
자동으로 포한된다. WebSecurityConfigurerAdapter를 상속받아 메소드 오버라이딩을 통해 보안 설정을 커스터마이징할 수 있다.*/
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;
    //책에 없는 부분
    //http 요청에 대한 보안을 설정한다. 페이지 권한설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정을 작성한다.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/members/login")//로그인 페이지 URL을 설정한다.
                .defaultSuccessUrl("/")//로그인 성공 시 이동할 URL을 설정한다.
                .usernameParameter("email")//로그인 시 사용할 파라미터 이름으로 email을 지정한다
                .failureUrl("/members/login/error")//로그인 실패 시 이동할 URL을 설정한다.
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");//로그아웃 성공 시 이동할 URL을 설정한다.

        /*183p 페이지 권한 주기(ADMIN만 상품등록)*/
        http.authorizeRequests()//시큐리티 처리에 HttpServletRequenst를 이용한다는 것을 의미
                .mvcMatchers("/","/members/**","/item/**", "/images/**").permitAll()//permitAll()을 통해 모든
                //사용자가 인증(로그인)없이 해당 경로를 접근할 수 있도록 설정
                .mvcMatchers("/admin/**").hasRole("ADMIN")// /admin으로 시작하는 경로는 해당 계정이 ADMIN Role일 경우
                //에만 접근 가능하도록 설정
                .anyRequest().authenticated();//위에서 설정해준 경로를 제외한 나머지 경로들은 모두 인증을 요구하도록 설정
        
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());//인즈오디지 않은 사용자가 리소스에 접근하였을때
        //수행되는 핸들러를 등록
    }
    /*183p 어드민 권한주기 : static 디렉터리의 하위 파일은 인증을 무시하도록 설정*/
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }




    //비밀번호를 데이터베이스에 그대로 저장했을 경우, 데이터베이스가 해킹당하면 고객의 회원정보가 그대로 노출되기 때문에
    //이를 해결하기 위해 BCryptPasswordEncoder의 해시 함수를 이용하여 비밀번호를 암호화해 저장한다.
    //BCryptPasswordEncoder를 Bean으로 등록하여 사용한다.
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception{//Sprin Security에서 인증은 AuthenticationManager를 통해
        //이루어지며 AuthenticationManagerBuilder가 AuthenticationManager를 생성한다.
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());// userDetailService를 구현하고 있는 객체로 memberService를 지정해주며, 비밀번호 암호화를 위해
        //oasswirdEncoder를 지정해준다.
    }
}

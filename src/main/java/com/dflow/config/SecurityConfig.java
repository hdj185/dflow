package com.dflow.config;

import com.dflow.login.service.impl.LoginDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.savedrequest.NullRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity  // filterChain이 자동 포함됨
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private LoginDetailServiceImpl loginDetailServiceImpl;

    private final CustomLogoutHandler customLogoutHandler;

    public SecurityConfig(CustomLogoutHandler customLogoutHandler) {
        this.customLogoutHandler = customLogoutHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin()
            .loginPage("/auth/login")
            .usernameParameter("memberId")  // 아이디 입력창의 name과 일치하는 값
            .failureUrl("/auth/login/error")
            .failureHandler(customAuthenticationFailureHandler()) // 커스텀한 AuthenticationFailureHandler 등록
            .defaultSuccessUrl("/main/main", true)
            .and()
            .logout()
            .addLogoutHandler(customLogoutHandler)
            .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout")) // 로그아웃 URL 설정
            .logoutSuccessUrl("/auth/login") // 로그아웃 성공 시 이동할 URL 설정
            .invalidateHttpSession(true) // HTTP 세션 무효화 여부 설정
            .deleteCookies("JSESSIONID"); // 필요한 경우 쿠키 삭제

        http.authorizeRequests()
            .mvcMatchers("/auth/login", "/auth/login/**", "/auth/join", "/auth/confirmInfo", "/auth/sendEmail").permitAll()
            .mvcMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
            .anyRequest().authenticated();

        // 인증되지 않은 사용자가 리소스에 접근했을 때 수행되는 핸들러 등록
        http.exceptionHandling()
            .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        // 필요한 경우에만 세션 생성하게 하기
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .invalidSessionUrl("/auth/login")
            .sessionAuthenticationErrorUrl("/auth/login");

        // 자동로그인 기능을 위한 쿠키 활성화


        http.rememberMe()   // 자동로그인 기능 추가 시작.
            .key("uniqueAndSecret")  // 특정 key 값을 사용하려면 주석 해제.
            .tokenValiditySeconds(604800)  // 유효시간을 변경하려면 주석 해제. default는 14일입니다.
            .rememberMeParameter("remember-me") // HTML form에서 'remember-me' 체크박스의 이름을 변경하려면 주석 해제.
            .userDetailsService(loginDetailServiceImpl);

    }

    // static 디렉토리 하위 파일은 인증을 무시함
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/font/**", "/img/**", "/js/**","/editor/**", "/api/**");
    }

    // 해시 함수를 이용하여 비밀번호 저장
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager 생성 후 인증하기
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginDetailServiceImpl)
            .passwordEncoder(passwordEncoder());
    }

    // 로그인 실패 핸들러
    @Bean
    public AuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler(); // 별도로 구현한 커스텀한 AuthenticationFailureHandler 클래스를 반환
    }

    // 변경된 세션 등록
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}

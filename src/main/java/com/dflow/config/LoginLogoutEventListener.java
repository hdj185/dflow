package com.dflow.config;

import com.dflow.entity.LoginOutHistory;
import com.dflow.login.CustomUser;
import com.dflow.repository.LoginOutHistoryRepository;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class LoginLogoutEventListener {

    private final HttpServletRequest request;

    private final LoginOutHistoryRepository loginOutHistoryRepository;

    public LoginLogoutEventListener(HttpServletRequest request, LoginOutHistoryRepository loginOutHistoryRepository) {
        this.request = request;
        this.loginOutHistoryRepository = loginOutHistoryRepository;
    }

    @EventListener
    public void handleLogin(AuthenticationSuccessEvent event) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) event.getSource();
        String memberId = authenticationToken.getName(); // 또는 원하는 필드로부터 값을 추출

        LoginOutHistory history = new LoginOutHistory();
        history.setMemberId(memberId);
        history.setTimestamp(LocalDateTime.now());
        history.setActivityType("LOGIN");

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();

            String xffHeader = request.getHeader("X-Forwarded-For");

            if (xffHeader == null) {
                history.setClientIp(request.getRemoteAddr());
            } else {
                // XFF header로부터 클라이언트 ip 추출
                history.setClientIp(xffHeader.split(",")[0]);
            }

            // Get the full header info for the 'User-Agent' from the request.
            String userAgentFullHeaderInfo = request.getHeader("User-Agent");
            UserAgent userAgent = UserAgent.parseUserAgentString(userAgentFullHeaderInfo);
            Browser browser = userAgent.getBrowser();
            OperatingSystem os = userAgent.getOperatingSystem();

            if (userAgentFullHeaderInfo != null) {
                history.setUserAgent(browser.getName() + " / " + os.getName());
            }
        }

        loginOutHistoryRepository.save(history);
    }

//    // Logout 이벤트는 HttpServletRequest 를 통해 처리하며, 세션에서 memberId를 가져옵니다.
//    @EventListener
//    public void handleLogout(SessionDestroyedEvent event) {
//        List<SecurityContext> lstSecurityContext = event.getSecurityContexts();
//        for (SecurityContext securityContext : lstSecurityContext) {
//            Authentication auth = securityContext.getAuthentication();
//
//            if (auth != null && auth.getPrincipal() instanceof CustomUser) { // CustomUser 클래스로 캐스팅 가능한지 확인
//                CustomUser customUser = (CustomUser) auth.getPrincipal();
//                String memberId = customUser.getUsername(); // 또는 다른 필드로부터 값을 추출
//
//                LoginOutHistory history = new LoginOutHistory();
//                history.setMemberId(memberId);
//                history.setTimestamp(LocalDateTime.now());
//                history.setActivityType("LOGOUT");
//
//                // Set client IP and user agent information for logout event.
//                // Note: This may not work if your application is behind a proxy or load balancer,
//                // you would then need to parse the "X-Forwarded-For" header instead of getRemoteAddr().
//                history.setClientIp(request.getRemoteAddr());
//
//                // Get the full header info for the 'User-Agent' from the request.
//                String userAgentFullHeaderInfo = request.getHeader("User-Agent");
//                history.setUserAgent(userAgentFullHeaderInfo);
//
//                loginOutHistoryRepository.save(history);
//            }
//        }
//    }

}

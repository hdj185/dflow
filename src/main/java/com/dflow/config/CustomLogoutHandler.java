package com.dflow.config;

import com.dflow.entity.LoginOutHistory;
import com.dflow.login.CustomUser;
import com.dflow.repository.LoginOutHistoryRepository;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    private final LoginOutHistoryRepository loginOutHistoryRepository;

    public CustomLogoutHandler(LoginOutHistoryRepository loginOutHistoryRepository) {
        this.loginOutHistoryRepository = loginOutHistoryRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUser) {
            CustomUser customUser = (CustomUser) authentication.getPrincipal();
            String memberId = customUser.getUsername();

            LoginOutHistory history = new LoginOutHistory();
            history.setMemberId(memberId);
            history.setTimestamp(LocalDateTime.now());
            history.setActivityType("LOGOUT");

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

            loginOutHistoryRepository.save(history);
        }
    }
}

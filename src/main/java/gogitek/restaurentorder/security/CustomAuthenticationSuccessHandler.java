package gogitek.restaurentorder.security;

import gogitek.restaurentorder.constaint.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String redirectURL = request.getContextPath();
        if (userDetails.hasRole(Role.CHEF.getType())) {
            redirectURL = "admin/preorder";
        } else if (userDetails.hasRole(Role.WAITER.getType())){
            redirectURL = "staff/list-order";
        }
        else redirectURL = "admin";
        response.sendRedirect(redirectURL);

    }
}

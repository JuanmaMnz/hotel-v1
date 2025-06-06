package com.hotel.Security.Auth.Service;

import com.hotel.Common.Response.Response;
import com.hotel.Security.Account.Enums.Role;
import com.hotel.Security.Account.Model.Account;
import com.hotel.Security.Account.Service.IAccountService;
import com.hotel.Security.Auth.DTO.AuthRequest;
import com.hotel.Security.Auth.DTO.AuthResponse;
import com.hotel.Security.Config.Service.CookieService;
import com.hotel.Security.Config.Service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.hotel.Common.Response.Utils.ResponseUtils.generateResponse;
import static com.hotel.Security.Auth.Messages.AuthResponseMessages.USER_AUTHENTICATED;
import static com.hotel.Security.Auth.Utils.AuthUtils.createAuthResponse;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements  IAuthService {

    private final AuthenticationManager authenticationManager;
    private final IAccountService accountService;
    private final JwtService jwtService;
    private final CookieService cookieService;

    @Override
    public Response authenticate(AuthRequest authRequest, HttpServletResponse httpServletResponse) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        Account account = accountService.getAccountByEmail(authRequest.getEmail());

        jwtService.revokeAllUserAccessTokens(account);
        String accessToken = jwtService.generateAccessToken(account);
        jwtService.saveAccessTokenForUser(account, accessToken);

        cookieService.expireCookie(httpServletResponse, "jwt");
        cookieService.addCookie(httpServletResponse, "jwt", accessToken, -1);

        AuthResponse response = createAuthResponse(accessToken, account.getRoles());
        return generateResponse(response, OK, USER_AUTHENTICATED);
    }

    @Override
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities() == null) {
            return "";
        }

        return authentication.getName();
    }

    @Override
    public boolean isCurrentUserEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }

        String roleEmployee = Role.EMPLOYEE.getAuthority();

        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleEmployee));
    }

}
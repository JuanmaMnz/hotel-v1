package com.hotel.Security.Config.Filter;

import com.hotel.Security.Config.Service.CookieService;
import com.hotel.Security.Config.Service.JwtService;
import com.hotel.Security.Token.Repository.TokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.hotel.Security.Config.SecurityConfig.PUBLIC_URLS;
import static com.hotel.Security.Config.SecurityConfig.PUBLIC_GET_URLS;
import static com.hotel.Security.Config.SecurityConfig.PUBLIC_POST_URLS;
import org.springframework.http.HttpMethod;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public final String OOPS_ERROR = "Oops, algo inesperado ha ocurrido";
    public final String INVALID_JWT = "Token JWT no válido";

    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final CookieService cookieService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        try {
            String jwt = extractTokenFromHeaderOrCookie(request);

            if (jwt == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (isPublicUrl(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            final String userEmail = jwtService.getSubject(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                boolean isTokenValid = tokenRepository.findByToken(jwt)
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);

                if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            filterChain.doFilter(request, response);

        } catch (SignatureException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_JWT);
            logger.warn("Se ha enviado un JWT con la firma inválida.");

        } catch (ExpiredJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_JWT);
            logger.warn("El JWT enviado ya ha expirado. Se requiere autenticación nuevamente.");

        } catch (MalformedJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_JWT);
            logger.warn("Se ha recibido un JWT mal formado. El formato del token es inválido.");

        } catch (UnsupportedJwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_JWT);
            logger.warn("Se ha recibido un JWT con un formato no soportado.");

        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, INVALID_JWT);
            logger.warn("Se ha recibido un JWT vacío o con argumentos inválidos.");

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, OOPS_ERROR);
            logger.error("Se produjo un error inesperado al procesar el JWT: {}", e.getMessage(), e);
        }
    }

    private String extractTokenFromHeaderOrCookie(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return cookieService.getCookieValue(request, "jwt");
    }

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private boolean isPublicUrl(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();

        boolean matchPublic = Arrays.stream(PUBLIC_URLS)
                .anyMatch(publicUrl -> pathMatcher.match(publicUrl, path));

        boolean matchPublicGet = HttpMethod.GET.matches(method) &&
                Arrays.stream(PUBLIC_GET_URLS)
                        .anyMatch(publicUrl -> pathMatcher.match(publicUrl, path));

        boolean matchPublicPost = HttpMethod.POST.matches(method) &&
                Arrays.stream(PUBLIC_POST_URLS)
                        .anyMatch(publicUrl -> pathMatcher.match(publicUrl, path));

        return matchPublic || matchPublicGet || matchPublicPost;
    }

}
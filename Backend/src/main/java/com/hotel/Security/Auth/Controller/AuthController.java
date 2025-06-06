package com.hotel.Security.Auth.Controller;

import com.hotel.Common.Response.Response;
import com.hotel.Security.Auth.DTO.AuthRequest;
import com.hotel.Security.Auth.Service.IAuthService;
import com.hotel.Security.RateLimiter.Annotation.WithIpRateLimit;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/hotel")
@RequiredArgsConstructor
@Tag(name = "Autenticación")
public class AuthController {

    private final IAuthService authService;

    @Operation(
            summary = "Autenticar usuarios existentes",
            description = "Permite verificar la identidad de un usuario, toma las credenciales proporcionadas por el usuario, las válida y establece si el usuario tiene acceso o no"
    )
    @PostMapping("/authenticate")
    @WithIpRateLimit(limit = 25, duration = 60000)
    public Response authenticate (
            @Valid @RequestBody AuthRequest authRequest,
            HttpServletResponse httpServletResponse
    ) {
        return authService.authenticate(authRequest, httpServletResponse);
    }


    @PostMapping("/logout")
    public void logout(){
    }

}
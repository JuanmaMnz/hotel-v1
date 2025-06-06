package com.hotel.Security.Auth.Service;

import com.hotel.Common.Response.Response;
import com.hotel.Security.Auth.DTO.AuthRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {

    Response authenticate (AuthRequest request, HttpServletResponse httpServletResponse);

    String getCurrentUsername();

    boolean isCurrentUserEmployee();
}
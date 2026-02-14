package com.hotel.hotel.helpers.auth;

import com.hotel.hotel.internal.dto.TokenValidationResponse;
import com.hotel.hotel.infrastructure.security.AuthContextFilter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;

public class AuthUtils {

    private AuthUtils() {
        throw new UnsupportedOperationException("This class should never be instantiated");
    }

    public static TokenValidationResponse getAuth(NativeWebRequest request) {
        if (request == null) {
            return null;
        }
        Object value = request.getAttribute(AuthContextFilter.AUTH_CONTEXT_KEY, RequestAttributes.SCOPE_REQUEST);
        if (value instanceof TokenValidationResponse response) {
            return response;
        }
        return null;
    }

    public static boolean isAdmin(TokenValidationResponse auth) {
        return auth != null && auth.getRole() != null && "ADMIN".equalsIgnoreCase(auth.getRole());
    }
}

package com.baro.noi_that_api.common.security;

import com.baro.noi_that_api.exception.AppException;
import com.baro.noi_that_api.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtils {

    // ===================== GET CURRENT INFO =====================

    public static String getCurrentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt)) return null;

        Jwt jwt = (Jwt) auth.getPrincipal();
        return jwt.getSubject();
    }

    public static Integer getCurrentId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt)) return null;

        Jwt jwt = (Jwt) auth.getPrincipal();
        Object idClaim = jwt.getClaim("id");

        if (idClaim == null) return null;

        if (idClaim instanceof Integer) {
            return (Integer) idClaim;
        } else if (idClaim instanceof Long) {
            return ((Long) idClaim).intValue();
        } else if (idClaim instanceof String) {
            try {
                return Integer.parseInt((String) idClaim);
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    public static String getCurrentRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt)) return null;

        Jwt jwt = (Jwt) auth.getPrincipal();
        return jwt.getClaimAsString("scope");
    }

    // ===================== ROLE CHECK =====================

    public static boolean isAdmin() {
        String role = getCurrentRole();
        return role != null && role.contains("ROLE_ADMIN");
    }

    public static boolean isStaff() {
        String role = getCurrentRole();
        return role != null && (role.contains("ROLE_STAFF") || role.contains("ROLE_ADMIN"));
    }

    public static boolean isCustomer() {
        String role = getCurrentRole();
        return role != null && role.contains("ROLE_CUSTOMER");
    }

    // ===================== OWNERSHIP CHECK =====================

    /**
     * STAFF chỉ được sửa chính mình (ADMIN thì được tất cả)
     */
    public static void checkStaffAccess(Integer id) {
        Integer currentId = getCurrentId();

        if (currentId == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // ADMIN thì cho qua
        if (isAdmin()) {
            return;
        }

        // STAFF chỉ được sửa chính mình
        if (!currentId.equals(id)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    /**
     * CUSTOMER chỉ được sửa chính mình
     */
    public static void checkCustomerAccess(Integer id) {
        Integer currentId = getCurrentId();

        if (currentId == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if (!currentId.equals(id)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }
}
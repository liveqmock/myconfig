package net.myconfig.service.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

	private SecurityUtils() {
	}

	public static Authentication authentication() {
		SecurityContext context = SecurityContextHolder.getContext();
		return context.getAuthentication();
	}

}

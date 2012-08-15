package net.myconfig.service.security.support;

import net.myconfig.service.api.security.UserProfile;

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

	public static UserProfile profile() {
		Authentication authentication = authentication();
		if (authentication != null) {
			Object details = authentication.getDetails();
			if (details instanceof UserProfile) {
				return (UserProfile) details;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
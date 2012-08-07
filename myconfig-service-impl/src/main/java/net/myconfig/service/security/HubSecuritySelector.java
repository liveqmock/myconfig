package net.myconfig.service.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.myconfig.core.AppFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.SecurityManagement;
import net.myconfig.service.api.security.SecuritySelector;
import net.myconfig.service.api.security.SecurityService;
import net.myconfig.service.api.security.UserToken;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

@Component
public class HubSecuritySelector implements SecuritySelector {

	private final Logger logger = LoggerFactory.getLogger(HubSecuritySelector.class);

	private final SecurityService securityService;
	private final Map<String, SecurityManagement> managers;

	@Autowired
	public HubSecuritySelector(Collection<SecurityManagement> managers, SecurityService securityService) {
		this.securityService = securityService;
		this.managers = Maps.uniqueIndex(managers, new Function<SecurityManagement, String>() {
			@Override
			public String apply(SecurityManagement manager) {
				logger.info("[{}] {}", manager.getId(), manager);
				return manager.getId();
			}
		});
	}

	@Override
	public String getSecurityManagementId() {
		return securityService.getSecurityMode();
	}

	@Override
	@Transactional
	public void switchSecurityMode(String mode) {
		logger.info("[security] Changing security mode to {}", mode);
		String currentMode = getSecurityManagementId();
		if (!StringUtils.equals(currentMode, mode)) {
			SecurityManagement manager = managers.get(mode);
			if (manager == null) {
				throw new SecurityManagementNotFoundException(mode);
			}
			securityService.setSecurityMode(mode);

		} else {
			logger.info("[security] {} mode is already selected.", mode);
		}
	}

	@Override
	public List<String> getSecurityModes() {
		List<String> modes = new ArrayList<String>(managers.keySet());
		Collections.sort(modes);
		return modes;
	}

	protected SecurityManagement getSecurityManagement() {
		String mode = getSecurityManagementId();
		SecurityManagement management = managers.get(mode);
		if (management != null) {
			return management;
		} else {
			throw new SecurityManagementNotFoundException(mode);
		}
	}

	@Override
	public UserToken authenticate(Authentication authentication) {
		return getSecurityManagement().authenticate(authentication);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return getSecurityManagement().supports(authentication);
	}

	@Override
	public boolean hasUserFunction(Authentication authentication, UserFunction fn) {
		return getSecurityManagement().hasUserFunction(authentication, fn);
	}

	@Override
	public boolean hasApplicationFunction(Authentication authentication, int application, AppFunction fn) {
		return getSecurityManagement().hasApplicationFunction(authentication, application, fn);
	}

	@Override
	public boolean allowLogin() {
		return getSecurityManagement().allowLogin();
	}

}
package net.myconfig.service.security.provider;

import static net.myconfig.service.db.SQLColumns.DISPLAYNAME;
import static net.myconfig.service.db.SQLColumns.EMAIL;
import static net.myconfig.service.db.SQLColumns.MODE;
import static net.myconfig.service.db.SQLColumns.NAME;
import static net.myconfig.service.db.SQLColumns.VERIFIED;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.model.Ack;
import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.security.LDAPConfiguration;
import net.myconfig.service.api.security.LDAPConfigurator;
import net.myconfig.service.api.security.User;
import net.myconfig.service.cache.CacheNames;
import net.myconfig.service.security.UserAlreadyDefinedException;
import net.myconfig.service.validation.UserValidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LdapUserProvider extends AbstractUserProvider implements LDAPConfigurator {

	private final ConfigurationService configurationService;

	@Autowired
	public LdapUserProvider(DataSource dataSource, Validator validator, ConfigurationService configurationService) {
		super(dataSource, validator, "ldap");
		this.configurationService = configurationService;
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(CacheNames.LDAP_CONFIGURATION)
	public LDAPConfiguration loadConfiguration() {
		String server = configurationService.getParameter(ConfigurationKey.LDAP_SERVER);
		String server = configurationService.getParameter(ConfigurationKey.LDAP_SERVER);
		String rootDN = configurationService.getParameter(ConfigurationKey.LDAP_ROOT_DN);
		String userSearchBase = configurationService.getParameter(ConfigurationKey.LDAP_USER_SEARCH_BASE);
		String userSearchFilter = configurationService.getParameter(ConfigurationKey.LDAP_SEARCH_FILTER);
		String managerDN = configurationService.getParameter(ConfigurationKey.LDAP_MANAGER_DN);
		String managerPassword = configurationService.getParameter(ConfigurationKey.LDAP_MANAGER_PASSWORD);
		return new LDAPConfiguration(enabled, server, rootDN, userSearchBase, userSearchFilter, managerDN, managerPassword);
	}

	@Override
	@Transactional
	@CacheEvict(CacheNames.LDAP_CONFIGURATION)
	public void saveConfiguration(LDAPConfiguration configuration) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isInternalEnabled() {
		// FIXME Checks the configuration
		return true;
	}

	@Override
	public boolean isConfigured() {
		// FIXME Checks the configuration
		return true;
	}

	@Override
	@Transactional(readOnly = true)
	public User getUser(String username, String password) {
		// FIXME Checks the user from the database (enabled, verified)
		// FIXME Connects to the LDAP
		return null;
	}

	@Override
	public Ack create(String name, String displayName, String email) {
		// Validation
		validate(UserValidation.class, NAME, name);
		try {
			// Creates the user
			int count = getNamedParameterJdbcTemplate().update(USER_CREATE,
					new MapSqlParameterSource(MODE, getId()).addValue(NAME, name).addValue(DISPLAYNAME, "").addValue(EMAIL, "").addValue(VERIFIED, true));
			// OK
			return Ack.one(count);
		} catch (DuplicateKeyException ex) {
			throw new UserAlreadyDefinedException(name, email);
		}
	}

}
package net.myconfig.service.impl;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.service.api.AuthProviderSelector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfigurationServiceImpl extends AbstractDaoService implements AuthProviderSelector {

	public static final String SECURITY_MODE = "security.mode";
	
	private static final String SECURITY_MODE_DEFAULT = "disabled";

	@Autowired
	public ConfigurationServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	@Override
	public String getSelectedAuthProviderId() {
		return getParameter(SECURITY_MODE, SECURITY_MODE_DEFAULT);
	}

	@Transactional(readOnly = true)
	public String getParameter(String name, String defaultValue) {
		String value = getFirstItem(SQL.CONFIGURATION_VALUE, new MapSqlParameterSource(SQLColumns.NAME, name), String.class);
		if (value == null) {
			return defaultValue;
		} else {
			return value;
		}
	}

}
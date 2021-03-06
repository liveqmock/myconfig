package net.myconfig.service.impl;

import java.util.List;
import java.util.Set;

import javax.sql.DataSource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import net.myconfig.service.exception.ValidationException;
import net.sf.jstring.LocalizableMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

public abstract class AbstractDaoService extends NamedParameterJdbcDaoSupport {

	protected static final String USER_ALL = "*";
	
	private final Validator validator;
	
	public AbstractDaoService(DataSource dataSource, Validator validator) {
		setDataSource(dataSource);
		this.validator = validator;
	}
	
	protected <T> ValidationException validationException (Set<ConstraintViolation<T>> violations) {
		ConstraintViolation<T> violation = violations.iterator().next();
		// Message code
		String code = String.format("%s.%s",
				violation.getRootBeanClass().getName(),
				violation.getPropertyPath());
		// Message
		Object oMessage;
		String message = violation.getMessage();
		if (StringUtils.startsWith(message, "{net.myconfig")) {
			String key = StringUtils.strip(message, "{}");
			oMessage = new LocalizableMessage(key);
		} else {
			oMessage = message;
		}
		// Exception
		return new ValidationException(new LocalizableMessage(code), oMessage, violation.getInvalidValue());
	}

	protected <T> void validate(Class<T> validationClass, String propertyName, Object value) {
		Set<ConstraintViolation<T>> violations = validator.validateValue(validationClass, propertyName, value);
		if (!violations.isEmpty()) {
			throw validationException (violations);
		}
	}
	
	protected <T> T getFirstItem (String sql, MapSqlParameterSource criteria, Class<T> type) {
		List<T> items = getNamedParameterJdbcTemplate().queryForList(sql, criteria, type);
		if (items.isEmpty()) {
			return null;
		} else {
			return items.get(0);
		}
	}

}

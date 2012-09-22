package net.myconfig.service.security;

import static net.myconfig.service.db.SQLColumns.APPLICATION;
import static net.myconfig.service.db.SQLColumns.ENVIRONMENT;
import static net.myconfig.service.db.SQLColumns.GRANTEDFUNCTION;
import static net.myconfig.service.db.SQLColumns.USER;

import java.util.EnumSet;
import java.util.List;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.myconfig.core.AppFunction;
import net.myconfig.core.EnvFunction;
import net.myconfig.core.UserFunction;
import net.myconfig.service.api.security.GrantService;
import net.myconfig.service.db.SQL;
import net.myconfig.service.db.SQLColumns;
import net.myconfig.service.impl.AbstractDaoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

@Service
public class GrantServiceImpl extends AbstractDaoService implements GrantService {

	@Autowired
	public GrantServiceImpl(DataSource dataSource, Validator validator) {
		super(dataSource, validator);
	}

	// FIXME Cache
	@Override
	public EnumSet<UserFunction> getUserFunctions(String name) {
		List<UserFunction> fns = Lists.transform(getNamedParameterJdbcTemplate().queryForList(SQL.FUNCTIONS_USER, new MapSqlParameterSource(SQLColumns.USER, name), String.class),
				new Function<String, UserFunction>() {
					@Override
					public UserFunction apply(String name) {
						return UserFunction.valueOf(name);
					}
				});
		if (fns.isEmpty()) {
			return EnumSet.noneOf(UserFunction.class);
		} else {
			return EnumSet.copyOf(fns);
		}
	}

	// FIXME Cache
	@Override
	public boolean hasUserFunction(String name, UserFunction fn) {
		return getFirstItem(SQL.FUNCTION_USER, new MapSqlParameterSource(USER, name).addValue(GRANTEDFUNCTION, fn.name()), String.class) != null;
	}

	// FIXME Cache
	@Override
	public boolean hasAppFunction(String name, int application, AppFunction fn) {
		return getFirstItem(SQL.FUNCTION_APP, new MapSqlParameterSource(USER, name).addValue(APPLICATION, application).addValue(GRANTEDFUNCTION, fn.name()), String.class) != null;
	}

	// FIXME Cache
	@Override
	public boolean hasEnvFunction(String name, int application, String environment, EnvFunction fn) {
		return getFirstItem(SQL.FUNCTION_ENV, new MapSqlParameterSource(USER, name).addValue(APPLICATION, application).addValue(ENVIRONMENT, environment).addValue(GRANTEDFUNCTION, fn.name()),
				String.class) != null;
	}

}
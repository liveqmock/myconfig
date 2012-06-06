package net.myconfig.service.impl;

import java.io.FileNotFoundException;

import javax.annotation.PostConstruct;

import net.myconfig.service.api.ConfigurationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractConfigurationService implements ConfigurationService {

	private final Logger logger = LoggerFactory.getLogger(ConfigurationService.class);

	private final String profile;
	private final String dbDriver;
	private final String dbUrl;
	private final String dbUser;
	private final String dbPassword;
	private final int dbPoolInitial;
	private final int dbPoolMax;

	public AbstractConfigurationService(String profile, String dbDriver, String dbUrl, String dbUser, String dbPassword, int dbPoolInitial, int dbPoolMax) {
		this.profile = profile;
		this.dbDriver = dbDriver;
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		this.dbPoolInitial = dbPoolInitial;
		this.dbPoolMax = dbPoolMax;
	}
	
	@PostConstruct
	public void init() throws FileNotFoundException {
		initGeneral();
		initLogging();
	}

	protected void initLogging() throws FileNotFoundException {
		// FIXME Log4jConfigurer.initLogging(loggingPath);
	}

	protected void initGeneral() {
		logger.info("Using {} profile", profile);
	}

	@Override
	public String getDBDriver() {
		return dbDriver;
	}

	@Override
	public String getDBURL() {
		return dbUrl;
	}

	@Override
	public String getDBUser() {
		return dbUser;
	}

	@Override
	public String getDBPassword() {
		return dbPassword;
	}

	@Override
	public int getDBPoolInitial() {
		return dbPoolInitial;
	}

	@Override
	public int getDBPoolMax() {
		return dbPoolMax;
	}

}

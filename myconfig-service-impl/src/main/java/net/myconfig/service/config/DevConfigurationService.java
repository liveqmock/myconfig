package net.myconfig.service.config;

import net.myconfig.core.MyConfigProfiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(MyConfigProfiles.DEV)
public class DevConfigurationService extends AbstractConfigurationService {
	
	public DevConfigurationService() {
		super (
				MyConfigProfiles.DEV,
				"classpath:log4j_dev.properties",
				"org.h2.Driver",
				String.format("jdbc:h2:file:%s/myconfig-dev/db/data;AUTOCOMMIT=OFF;MVCC=true", System.getProperty("user.home")),
				"sa",
				"",
				1,
				2);
	}

}

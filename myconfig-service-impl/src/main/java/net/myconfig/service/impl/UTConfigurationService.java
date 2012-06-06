package net.myconfig.service.impl;

import net.myconfig.core.MyConfigProfiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile(MyConfigProfiles.TEST)
public class UTConfigurationService extends AbstractConfigurationService {
	
	public UTConfigurationService() {
		super (
				MyConfigProfiles.TEST,
				"org.h2.Driver",
				"jdbc:h2:file:target/db/data;AUTOCOMMIT=OFF;MVCC=true",
				"sa",
				"",
				1,
				2);
	}

}

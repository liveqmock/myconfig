package net.myconfig.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.exception.ApplicationNameAlreadyDefinedException;
import net.myconfig.service.exception.ApplicationNotFoundException;
import net.myconfig.service.exception.EnvironmentNotFoundException;
import net.myconfig.service.exception.KeyNotFoundException;
import net.myconfig.service.exception.VersionNotFoundException;
import net.myconfig.service.model.Ack;
import net.myconfig.service.model.ApplicationSummary;
import net.myconfig.service.model.ConfigurationSet;
import net.myconfig.service.model.ConfigurationValue;
import net.myconfig.test.AbstractIntegrationTest;

public class MyConfigServiceTest extends AbstractIntegrationTest {
	
	@Autowired
	private MyConfigService myConfigService;
	
	@Test
	public void get_key_ok() {
		String value = myConfigService.getKey("myapp", "1.1", "UAT", "jdbc.user");
		assertEquals ("1.1 jdbc.user UAT", value);
	}
	
	@Test(expected = KeyNotFoundException.class)
	public void get_key_not_found() {
		myConfigService.getKey("myapp", "1.1", "UAT", "jdbc.usr");
	}
	
	@Test
	public void get_env () {
		ConfigurationSet set = myConfigService.getEnv("myapp", "1.1", "UAT");
		assertNotNull (set);
		List<ConfigurationValue> values = set.getValues();
		assertNotNull (values);
		assertEquals (2, values.size());
		{
			ConfigurationValue value = values.get(0);
			assertEquals ("jdbc.password", value.getKey());
			assertEquals ("Password used to connect to the database", value.getDescription());
			assertEquals ("1.1 jdbc.password UAT", value.getValue());
		}
		{
			ConfigurationValue value = values.get(1);
			assertEquals ("jdbc.user", value.getKey());
			assertEquals ("User used to connect to the database", value.getDescription());
			assertEquals ("1.1 jdbc.user UAT", value.getValue());
		}
	}
	
	@Test(expected = ApplicationNotFoundException.class)
	public void get_env_no_app () {
		myConfigService.getEnv("myappxxx", "1.1", "UAT");
	}
	
	@Test(expected = VersionNotFoundException.class)
	public void get_env_no_version () {
		myConfigService.getEnv("myapp", "1.x", "UAT");
	}
	
	@Test(expected = EnvironmentNotFoundException.class)
	public void get_env_no_environment () {
		myConfigService.getEnv("myapp", "1.1", "XXX");
	}
	
	@Test
	public void applications() {
		List<ApplicationSummary> applications = myConfigService.getApplications();
		assertNotNull (applications);
		ApplicationSummary myapp = Iterables.find(applications, new Predicate<ApplicationSummary>() {
			@Override
			public boolean apply(ApplicationSummary summary) {
				return "myapp".equals(summary.getName());
			}
		});
		assertEquals (1, myapp.getId());
		assertEquals ("myapp", myapp.getName());
		// TODO Assert stats
	}
	
	@Test
	public void applicationCreate () {
		ApplicationSummary summary = myConfigService.createApplication("test");
		assertNotNull (summary);
		assertEquals ("test", summary.getName());
		assertTrue (summary.getId() > 0);
		// TODO Assert stats
	}
	
	@Test(expected = ApplicationNameAlreadyDefinedException.class)
	public void applicationCreate_not_unique () {
		myConfigService.createApplication("test2");
		myConfigService.createApplication("test2");
	}

	@Test
	public void applicationDelete () {
		int id = myConfigService.createApplication("test3").getId();
		Ack ack = myConfigService.deleteApplication(id);
		assertNotNull (ack);
		assertTrue (ack.isSuccess());
	}

	@Test
	public void applicationDelete_cannot () {
		Ack ack = myConfigService.deleteApplication(-1);
		assertNotNull (ack);
		assertFalse (ack.isSuccess());
	}

}

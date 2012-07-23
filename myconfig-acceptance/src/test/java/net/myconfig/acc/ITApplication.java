package net.myconfig.acc;

import static org.junit.Assert.assertEquals;
import net.myconfig.acc.page.ApplicationsPage;
import net.myconfig.acc.page.HomePage;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ITApplication extends AccTest {
	
	private ApplicationsPage applications;

	@Before
	public void applications() {
		// Goes to the applications page
		HomePage home = new HomePage(driver, pageContext);
		applications = home.ui();
	}
	
	@Test
	@Ignore
	public void appChangeLanguage() {
		// Select French
		applications.selectLanguage ("FR");
		assertEquals("Applications configurées", applications.getPageTitle());
		// Select English
		applications.selectLanguage ("EN");
		assertEquals("Configured applications", applications.getPageTitle());
	}
	
}

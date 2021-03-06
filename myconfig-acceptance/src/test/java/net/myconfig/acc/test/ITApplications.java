package net.myconfig.acc.test;

import net.myconfig.acc.page.ApplicationsPage;
import net.myconfig.acc.page.HomePage;

import org.junit.Before;
import org.junit.Test;

public class ITApplications extends AbstractGUIUseCase {

	private ApplicationsPage applications;

	@Before
	public void applications() {
		// Goes to the applications page
		HomePage home = new HomePage(driver, pageContext);
		applications = home.ui();
	}

	/**
	 * Given I am on the list of applications And an unique application name
	 * When I create the application Then I should see the application in the
	 * list
	 */
	@Test
	public void createApplication() throws Exception {
		String name = appName();
		applications.createApplication(appId(), name);
		applications.checkForApplication(name);
	}
	
	@Test
	public void createAlreadyExistingApplicationID() throws Exception {
		String appId = appId();
		applications.createApplication(appId, appName());
		// ... a second time
		applications.createApplication(appId, appName());
		applications.checkForError("error", "[S-014] Application ID \"%s\" is already defined.", appId);
	}

	/**
	 * Given I am on the list of applications And an unique application name
	 * When I create the application And I create the application Then I should
	 * see the "[S-002] The application with name "%s" is already defined."
	 * application error
	 */
	@Test
	public void createAlreadyExistingApplication() throws Exception {
		String name = appName();
		applications.createApplication(appId(), name);
		// ... a second time
		applications.createApplication(appId(), name);
		applications.checkForError("error", "[S-002] The application with name \"%s\" is already defined.", name);
	}

	/**
	 * Creating and deleting an application
	 */
	@Test
	public void createAndDeleteApplication() throws Exception {
		String name = appName();
		applications.createApplication(appId(), name);
		applications.deleteApplication(name);
		applications.checkForApplicationNotPresent(name);
	}

}

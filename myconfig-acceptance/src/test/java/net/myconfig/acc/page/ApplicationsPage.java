package net.myconfig.acc.page;

import static org.junit.Assert.assertFalse;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.collect.Lists;

public class ApplicationsPage extends Page {

	public ApplicationsPage(WebDriver driver, PageContext pageContext) {
		super(driver, pageContext);
		driver.findElement(By.id("applications"));
		takeScreenshot("Applications");
	}

	public void createApplication(String id, String name) {
		// Creates the application with its ID and name
		id("application-create-id").sendKeys(id);
		id("application-create-name").sendKeys(name);
		id("application-create-submit").click();
	}
	
	public void checkForApplication (String name) {
		// Waits for the name to appear
		driver.findElement(byApplicationName(name));
		takeScreenshot("Application-" + name);
	}

	public void deleteApplication(String name) {
		// Screenshot
		takeScreenshot("Application-Delete-Before-" + name);
		// Gets the delete button for this application
		WebElement deleteButton = xpath("//td[contains(@class,'item-column-name') and contains(text(), '%s')]/parent::tr//input[contains(@class,'item-action-delete')]", name);
		// Clicks it
		deleteButton.click();
		
		// Alert management
		confirmDialog("Application-Delete-Alert", "Ok", "[M-001-C] Do you want to delete the \"%s\" application and all its associated configuration?", name);
		
		// Checks the application is not there any longer
		checkForApplicationNotPresent(name);
	}

	public List<String> getApplicationNames() {
		List<WebElement> tds = driver.findElements(byXpath("//td[contains(@class, 'item-column-name')]"));
		List<String> appNames = Lists.transform(tds, webElementTextFn);
		return appNames;
	}

	public By byApplicationName(String name) {
		return byElement("td", "item-column-name", name);
	}

	public void checkForApplicationNotPresent(String name) {
		// Waits for the application list to be reloaded
		id("applications");
		// Checks the application is not there any longer
		List<String> appNames = getApplicationNames();
		assertFalse(String.format("%s application is still present", name), appNames.contains(name));
		// Screenshot
		takeScreenshot("Application-NotPresent-" + name);
	}

}

package net.myconfig.web.support.fm;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Locale;

import net.myconfig.web.language.CurrentLocale;
import net.sf.jstring.Strings;

import org.junit.Before;
import org.junit.Test;

import freemarker.template.TemplateModelException;

public class FnLocLinkTest {
	
	private static final String LINK = "<a href=\"ui/\">";
	private FnLocLink fn;
	
	@Before
	public void before() {
		Locale.setDefault(Locale.ENGLISH);
		Strings strings = new Strings("META-INF.resources.web-labels");
		CurrentLocale currentLocale = mock(CurrentLocale.class);
		when(currentLocale.getCurrentLocale()).thenReturn(Locale.ENGLISH);
		fn = new FnLocLink(strings, currentLocale);
	}
	
	@Test(expected = NullPointerException.class)
	public void list_null () throws TemplateModelException {
		fn.exec(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void list_1 () throws TemplateModelException {
		fn.exec(Arrays.asList("home.ui.message"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void list_3 () throws TemplateModelException {
		fn.exec(Arrays.asList("home.ui.message", LINK, "arg"));
	}
	
	@Test
	public void replaced () throws TemplateModelException {
		Object message = fn.exec(Arrays.asList("home.ui.message", LINK));
		assertEquals ("Click <a href=\"ui/\">here</a> to access the Web interface.", message);
	}
	
	@Test
	public void no_replacement () throws TemplateModelException {
		Object message = fn.exec(Arrays.asList("home.ui", LINK));
		assertEquals ("Web access", message);
	}

}

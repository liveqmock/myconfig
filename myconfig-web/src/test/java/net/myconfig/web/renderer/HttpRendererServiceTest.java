package net.myconfig.web.renderer;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.myconfig.test.AbstractIntegrationTest;

public class HttpRendererServiceTest extends AbstractIntegrationTest {
	
	@Autowired
	private HttpRendererService httpRendererService;
	
	@Test
	public void jsonAvailable() {
		HttpRenderer<Object> renderer = httpRendererService.getRenderer(Object.class, "json");
		assertNotNull (renderer);
	}
	
	@Test(expected = RendererNotFoundException.class)
	public void notAvailable() {
		httpRendererService.getRenderer(Object.class, "xxx");
	}

}
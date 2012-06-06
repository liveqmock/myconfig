package net.myconfig.web.rest;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import net.myconfig.core.CoreException;
import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.model.ConfigurationSet;
import net.myconfig.web.renderer.HttpRenderer;
import net.myconfig.web.renderer.HttpRendererService;
import net.myconfig.web.renderer.RendererNotFoundException;
import net.sf.jstring.Strings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(method = RequestMethod.GET, value = "/get")
public class GetController {

	private final Logger errors = LoggerFactory.getLogger("User");

	private final Strings strings;
	private final MyConfigService myConfigService;
	private final HttpRendererService httpRendererService;

	@Autowired
	public GetController(Strings strings, MyConfigService myConfigService, HttpRendererService httpRendererService) {
		this.myConfigService = myConfigService;
		this.strings = strings;
		this.httpRendererService = httpRendererService;
	}
	
	// TODO Moves this handler in a more generic location
	@ExceptionHandler(CoreException.class)
	public ResponseEntity<String> onException (Locale locale, CoreException ex) {
		// Generates a UUID
		String uuid = UUID.randomUUID().toString();
		// Traces the error
		// TODO Adds request information
		// TODO Adds authentication information
		// TODO Custom logging for Prod
		errors.error(String.format("[%s] %s", uuid, ex.getLocalizedMessage(strings, Locale.ENGLISH)));
		// Returns a message to display to the user
		String message = strings.get(locale, "general.error", ex, uuid);
		// Ok
		return new ResponseEntity<String>(message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@RequestMapping("/version")
	public @ResponseBody
	String version () {
		return myConfigService.getVersion();
	}

	// FIXME Configuration description for app x version

	@RequestMapping("/key/{key}/{application}/{version}/{environment}")
	public @ResponseBody
	String key(@PathVariable String application, @PathVariable String version, @PathVariable String environment, @PathVariable String key) {
		return myConfigService.getKey(application, version, environment, key);
	}

	@RequestMapping("/env/{application}/{version}/{environment}/{mode}")
	public void env (@PathVariable String application, @PathVariable String version, @PathVariable String environment,
			@PathVariable String mode,
			HttpServletResponse response) throws IOException {
		// Gets the configuration
		ConfigurationSet set = myConfigService.getEnv (application, version, environment);
		// Gets the renderer
		HttpRenderer<ConfigurationSet> renderer = getConfigurationSetRenderer(mode);
		// Renders the configuration into the response
		renderer.renderer (set, response);
	}

	private HttpRenderer<ConfigurationSet> getConfigurationSetRenderer(String mode) {
		try {
			return httpRendererService.getRenderer (ConfigurationSet.class, mode);
		} catch (RendererNotFoundException ex) {
			throw new ConfigurationModeNotFoundException (mode);
		}
	}

}

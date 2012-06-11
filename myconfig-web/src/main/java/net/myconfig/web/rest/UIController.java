package net.myconfig.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.myconfig.service.api.MyConfigService;
import net.myconfig.service.model.Ack;
import net.myconfig.service.model.ApplicationConfiguration;
import net.myconfig.service.model.ApplicationSummary;
import net.sf.jstring.Strings;

@Controller
@RequestMapping("/ui")
public class UIController extends AbstractRESTController implements UIInterface {

	@Autowired
	public UIController(Strings strings, MyConfigService myConfigService) {
		super(strings, myConfigService);
	}

	@Override
	@RequestMapping(value = "/applications", method = RequestMethod.GET)
	public @ResponseBody List<ApplicationSummary> applications() {
		return getMyConfigService().getApplications();
	}
	
	@Override
	@RequestMapping(value = "/application/{name}", method = RequestMethod.PUT)
	public @ResponseBody ApplicationSummary applicationCreate(@PathVariable String name) {
		return getMyConfigService().createApplication (name);
	}
	
	@Override
	@RequestMapping(value = "/application/{id}", method = RequestMethod.DELETE)
	public @ResponseBody Ack applicationDelete(@PathVariable int id) {
		return getMyConfigService().deleteApplication (id);
	}
	
	@Override
	@RequestMapping(value = "/application/{id}", method = RequestMethod.GET)
	public ApplicationConfiguration applicationConfiguration(int id) {
		return getMyConfigService().getApplicationConfiguration (id);
	}
	
	@Override
	@RequestMapping(value = "/version/{id}/{name}", method = RequestMethod.PUT)
	public Ack versionCreate(@PathVariable int id, @PathVariable String name) {
		return getMyConfigService().createVersion (id, name);
	}

}

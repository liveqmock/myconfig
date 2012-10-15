package net.myconfig.acc.client

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertNull
import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail
import groovyx.net.http.ContentType
import groovyx.net.http.Method
import net.myconfig.acc.support.AccUtils
import net.myconfig.client.java.MyConfigClient
import net.myconfig.client.java.support.ClientMessageException
import net.myconfig.core.model.ConfigurationUpdate
import net.myconfig.core.model.ConfigurationUpdates

import org.junit.BeforeClass
import org.junit.Test

class ITRestGet extends AbstractClientUseCase {
	
	@BeforeClass
	static void initTest() {
		MyConfigClient client = AccUtils.CONTEXT.getClient();
		// Deletes the application if needed
		def summaries = client.applications().getSummaries();
		def summary = summaries.find { it -> (it.getName() == "myapp") }
		if (summary != null) {
			client.applicationDelete(summary.getId());
		}
		// Test application
		def id = client.applicationCreate("myapp").getId();
		// Versions
		client.versionCreate(id, "1.0");
		client.versionCreate(id, "1.1");
		client.versionCreate(id, "1.2");
		// Environments
		client.environmentCreate(id, "DEV");
		client.environmentCreate(id, "ACC");
		client.environmentCreate(id, "UAT");
		client.environmentCreate(id, "PROD");
		// Keys
		client.keyCreate(id, "jdbc.user", "User used to connect to the database");
		client.keyCreate(id, "jdbc.password", "Password used to connect to the database");
		// Matrix & configuration
		List<ConfigurationUpdate> updates = new ArrayList<ConfigurationUpdate>()
		["1.0", "1.1", "1.2"].each() {
			version ->
				["jdbc.user", "jdbc.password"].each() {
					key -> client.keyVersionAdd(id, version, key)
					["DEV", "ACC", "UAT", "PROD"].each {
						env -> 
							def value = "$version $env $key"
							updates.add(new ConfigurationUpdate(env, version, key, value))
					}
				}
		}
		client.updateConfiguration(id, new ConfigurationUpdates(updates))
	}
	
	@Test
	void version() {
		def actualVersion = client().version()
		def expectedVersion = AccUtils.CONTEXT.getVersion()
		if (expectedVersion != actualVersion) {
			fail("Expected version $version but was $content")
		}
	}
	
	@Test
	void get_key_ok() {
		def value = client().key("myapp", "1.2", "UAT", "jdbc.user")
		assertEquals('1.2 UAT jdbc.user', value)
	}
	
	@Test
	void get_key_not_found() {
		try {
			client().key("myapp", "1.2", "UAT", "jdbc.usr")
		} catch (ClientMessageException ex) {
			def message = ex.getMessage();
			def staticMessage = message[0..-37]
			assertEquals("", staticMessage);
		}
	}
	
	@Test
	void get_env_json_concise () {
		http.request ( Method.GET, ContentType.JSON ) {
			uri.path = "env/myapp/UAT/1.2/json/concise"
			response.success = { resp, json ->
				assertEquals (2, json.size());
				assertEquals ("1.2 UAT jdbc.user", json["jdbc.user"]); 
				assertEquals ("1.2 UAT jdbc.password", json["jdbc.password"]); 
			}
		}
	}
	
	@Test
	void get_env_json_complete () {
		http.request ( Method.GET, ContentType.JSON ) {
			uri.path = "env/myapp/UAT/1.2/json/complete"
			response.success = { resp, json ->
				assertEquals (2, json.size());
				assertEquals ("jdbc.password", json[0]["key"]);
				assertEquals ("Password used to connect to the database", json[0]["description"]); 
				assertEquals ("1.2 UAT jdbc.password", json[0]["value"]);
				assertEquals ("jdbc.user", json[1]["key"]);
				assertEquals ("User used to connect to the database", json[1]["description"]); 
				assertEquals ("1.2 UAT jdbc.user", json[1]["value"]); 
			}
		}
	}
	
	@Test
	void get_env_json_default () {
		http.request ( Method.GET, ContentType.JSON ) {
			uri.path = "env/myapp/UAT/1.2/json"
			response.success = { resp, json ->
				assertEquals (2, json.size());
				assertEquals ("jdbc.password", json[0]["key"]);
				assertEquals ("Password used to connect to the database", json[0]["description"]); 
				assertEquals ("1.2 UAT jdbc.password", json[0]["value"]);
				assertEquals ("jdbc.user", json[1]["key"]);
				assertEquals ("User used to connect to the database", json[1]["description"]); 
				assertEquals ("1.2 UAT jdbc.user", json[1]["value"]); 
			}
		}
	}
	
	@Test
	void get_env_properties () {
		http.request ( Method.GET, ContentType.TEXT ) {
			uri.path = "env/myapp/UAT/1.2/properties"
			response.success = { resp, reader ->
				def content = reader.text
				assertEquals ("""# Configuration properties for 'myapp'
# Version: 1.2
# Environment: UAT

# Password used to connect to the database
jdbc.password = 1.2 UAT jdbc.password

# User used to connect to the database
jdbc.user = 1.2 UAT jdbc.user

""", content); 
			}
		}
	}
	
	@Test
	void get_env_json_unknown_variant () {
		http.request ( Method.GET, ContentType.TEXT ) {
			uri.path = "env/myapp/UAT/1.2/json/xxxx"
			response.failure = { resp, reader ->
				def content = reader.text
				assertEquals(500, resp.status);
				def expectedMessage = """\
An error has occurred.
Message: [W-003] "xxxx" variant is not supported for rendering a configuration as JSON.
Reference: """
				// Removes the last 36 characters of the content
				def modifiedContent = content[0..-37]
				// Comparison
				assertEquals (expectedMessage, modifiedContent); 
			}
			response.success = { resp ->
				fail("Should have failed")
			}
		}
	}
	
	@Test
	void get_env_xml_unknown_variant () {
		http.request ( Method.GET, ContentType.TEXT ) {
			uri.path = "env/myapp/UAT/1.2/xml/xxxx"
			response.failure = { resp, reader ->
				def content = reader.text
				assertEquals(500, resp.status);
				def expectedMessage = """\
An error has occurred.
Message: [W-004] "xxxx" variant is not supported for rendering a configuration as XML.
Reference: """
				// Removes the last 36 characters of the content
				def modifiedContent = content[0..-37]
				// Comparison
				assertEquals (expectedMessage, modifiedContent); 
			}
			response.success = { resp ->
				fail("Should have failed")
			}
		}
	}
	
	@Test
	void get_env_xml_attributesOnly () {
		http.get ( path: "env/myapp/UAT/1.2/xml/attributesOnly") { resp, xml ->
			assertEquals ("myapp", xml.@application.text())
			assertEquals ("UAT", xml.@environment.text())
			assertEquals ("1.2", xml.@version.text())
			assertEquals ("jdbc.password", xml.param[0].@name.text())
			assertEquals ("1.2 UAT jdbc.password", xml.param[0].@value.text())
			assertEquals ("Password used to connect to the database", xml.param[0].@description.text())
			assertEquals ("jdbc.user", xml.param[1].@name.text())
			assertEquals ("1.2 UAT jdbc.user", xml.param[1].@value.text())
			assertEquals ("User used to connect to the database", xml.param[1].@description.text())
		}
	}
	
	@Test
	void get_env_xml_default () {
		http.get ( path: "env/myapp/UAT/1.2/xml") { resp, xml ->
			assertEquals ("myapp", xml.@application.text())
			assertEquals ("UAT", xml.@environment.text())
			assertEquals ("1.2", xml.@version.text())
			assertEquals ("jdbc.password", xml.param[0].@name.text())
			assertEquals ("1.2 UAT jdbc.password", xml.param[0].@value.text())
			assertEquals ("Password used to connect to the database", xml.param[0].@description.text())
			assertEquals ("jdbc.user", xml.param[1].@name.text())
			assertEquals ("1.2 UAT jdbc.user", xml.param[1].@value.text())
			assertEquals ("User used to connect to the database", xml.param[1].@description.text())
		}
	}
	
	@Test
	void get_env_xml_mixed () {
		http.get ( path: "env/myapp/UAT/1.2/xml/mixed") { resp, xml ->
			assertEquals ("myapp", xml.@application.text())
			assertEquals ("UAT", xml.@environment.text())
			assertEquals ("1.2", xml.@version.text())
			assertEquals ("jdbc.password", xml.param[0].@name.text())
			assertEquals ("1.2 UAT jdbc.password", xml.param[0].text())
			assertEquals ("jdbc.user", xml.param[1].@name.text())
			assertEquals ("1.2 UAT jdbc.user", xml.param[1].text())
		}
	}
	
	@Test
	void get_env_xml_tagsOnly () {
		http.get ( path: "env/myapp/UAT/1.2/xml/tagsOnly") { resp, xml ->
			assertEquals ("myapp", xml.application.text())
			assertEquals ("UAT", xml.environment.text())
			assertEquals ("1.2", xml.version.text())
			assertEquals ("jdbc.password", xml.param[0].name.text())
			assertEquals ("1.2 UAT jdbc.password", xml.param[0].value.text())
			assertEquals ("Password used to connect to the database", xml.param[0].description.text())
			assertEquals ("jdbc.user", xml.param[1].name.text())
			assertEquals ("1.2 UAT jdbc.user", xml.param[1].value.text())
			assertEquals ("User used to connect to the database", xml.param[1].description.text())
		}
	}
	
	@Test
	void get_env_unknown_mode () {
		http.request ( Method.GET, ContentType.TEXT ) {
			uri.path = "env/myapp/UAT/1.2/xxx"
			response.failure = { resp, reader ->
				def content = reader.text
				assertEquals(500, resp.status);
				def expectedMessage = """\
An error has occurred.
Message: [W-002] "xxx" render mode is not defined.
Reference: """
				// Removes the last 36 characters of the content
				def modifiedContent = content[0..-37]
				// Comparison
				assertEquals (expectedMessage, modifiedContent); 
			}
			response.success = { resp ->
				fail("Should have failed")
			}
		}
	}

}

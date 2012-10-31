package net.myconfig.service.impl;

import java.sql.SQLException;

import net.myconfig.core.model.Event;
import net.myconfig.core.model.EventCategory;
import net.myconfig.service.api.EventService;
import net.myconfig.test.AbstractIntegrationTest;

import org.dbunit.dataset.DataSetException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EventServiceTest extends AbstractIntegrationTest {
	
	@Autowired
	private EventService eventService;
	
	@Test
	public void save() throws DataSetException, SQLException {
		assertRecordNotExists("select id from events where category = '%s'", EventCategory.CONFIGURATION_SET.name());
		Event e = new Event(EventCategory.CONFIGURATION_SET, "id", "message");
		eventService.saveEvent(e);
		assertRecordCount(1, "select id from events where category = '%s'", EventCategory.CONFIGURATION_SET.name());
	}

}
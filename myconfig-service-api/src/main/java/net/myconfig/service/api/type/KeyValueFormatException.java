package net.myconfig.service.api.type;

import net.myconfig.core.InputException;
import net.sf.jstring.Localizable;

public class KeyValueFormatException extends InputException {

	public KeyValueFormatException(String key, String value, Localizable message) {
		super(key, value, message);
	}

}

package net.myconfig.service.exception;

import net.myconfig.core.InputException;
import net.sf.jstring.Localizable;

public class ValidationException extends InputException {

	public ValidationException(Localizable field, Object message, Object value) {
		super(field, message, value);
	}

}

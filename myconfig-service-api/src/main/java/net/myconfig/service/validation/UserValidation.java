package net.myconfig.service.validation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class UserValidation {

	@NotNull
	@NotBlank
	@Trimmed
	@Size(min = 1, max = 80)
	@Pattern(regexp = ValidationConstants.NAME_REGEXP, message = ValidationConstants.NAME_REGEXP_MESSAGE)
	public String name;

	@NotNull
	@NotBlank
	@Trimmed
	@Size(min = 1, max = 80)
	public String displayName;

	@NotNull
	@Size(min = 1, max = 120)
	@Email
	public String email;

}

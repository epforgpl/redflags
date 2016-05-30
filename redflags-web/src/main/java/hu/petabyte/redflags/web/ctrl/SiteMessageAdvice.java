package hu.petabyte.redflags.web.ctrl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 *
 * @author Zsolt Jurányi
 *
 */
@ControllerAdvice
public class SiteMessageAdvice {

	@Value("${site.message:}")
	private String message;

	@ModelAttribute("message")
	public String getMessage() {
		return message;
	}

}

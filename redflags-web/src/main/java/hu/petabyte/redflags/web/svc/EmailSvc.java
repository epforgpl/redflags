/*
   Copyright 2014-2016 PetaByte Research Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package hu.petabyte.redflags.web.svc;

import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * @author Zsolt Jurányi
 */
@Service
public class EmailSvc {

	private static final Logger LOG = LoggerFactory.getLogger(EmailSvc.class);

	private @Value("${redflags.from}") String from;
	private @Autowired InternalRenderer renderer;
	private @Autowired MailSender mailSender;

	public boolean send(String to, String subject, String text) {
		try {
			JavaMailSenderImpl sender = (JavaMailSenderImpl) mailSender;
			MimeMessage mail = sender.createMimeMessage();
			mail.setContent(text.replaceAll("\n", "<br\\>\n").trim(),
					"text/html; charset=UTF-8");
			MimeMessageHelper helper = new MimeMessageHelper(mail, false,
					"UTF-8");
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			sender.send(mail);
			LOG.trace("E-mail sent with subject: {}", subject);
			return true;
		} catch (Exception ex) {
			LOG.warn("Failed to send email (subject: " + subject + ").", ex);
			return false;
		}
	}

	public String text(String templateName, Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response,
			Locale locale) {
		try {
			return renderer.evalView(request, response, model, locale,
					templateName);
		} catch (Exception e) {
			return null;
		}
	}

}

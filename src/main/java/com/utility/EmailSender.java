/**
 *
 */
package com.utility;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author USER
 *
 */
public class EmailSender implements Serializable {

	private static Logger logger = LoggerFactory.getLogger(EmailSender.class);

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final String CONST_NO_REPLY = "no-reply@prudential.com.sg";
	private static final String CONST_ADMIN = "Admin";

	private static final String CONST_FILE_PATH = "d:\\temp\\";
	private static final String CONST_TEMPLATE_PATH = "d:\\temp\\";

	/**
	 *
	 */
	public EmailSender() {
		super();
	}

	/**
	 *
	 * @param recipient
	 * @param criteriaMap
	 * @param filename
	 * @param subject
	 * @param templateFile
	 */
	public void sendEmialWithAttachment(String recipient, Map<String, String> criteriaMap, String filename,
			String subject, String templateFile) {

		if (recipient != null) {
			try {
				SendEmail mail = new SendEmail(SendEmail.CONST_HOST_MAIL);
				mail.setSender(CONST_NO_REPLY, CONST_ADMIN);
				mail.setSendHTML(true);
				mail.setSubject(subject);

				String content = getContent(templateFile, criteriaMap);
				mail.setMessage(content);
				mail.addRecipients(recipient, recipient);

				String path = CONST_FILE_PATH;
				mail.setPath(path);
				mail.setFilename(filename);

				mail.sent();
				logger.trace("email has been sent");
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}

	/**
	 *
	 * @param name
	 * @param templateFile
	 * @param criteriaMap
	 * @return
	 */
	public String getContent(String name, String templateFile, Map<String, String> criteriaMap) {
		String content = "";

		try {

			String criteria = convertToHtml(criteriaMap);
			FileManager fileManager = new FileManager(CONST_TEMPLATE_PATH, templateFile);
			content = fileManager.readFile();

			if (!StringUtils.isEmpty(name)) {
				content = CommonMethod.getReplaceString(content, "<@name@>", name);
			}

			content = CommonMethod.getReplaceString(content, "<@criteriaMap@>", criteria);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return content;
	}

	/**
	 *
	 * @param criteriaMap
	 * @return
	 */
	public String convertToHtml(Map<String, String> criteriaMap) {
		StringBuilder html = new StringBuilder();
		int i = 0;

		if ((criteriaMap == null) || (criteriaMap.size() == 0)) {
			html.append("N.A.");
			html.append("<br /><br />");

		} else {
			html.append("<table><thead><tr>");
			html.append("<th>Criteria</th>");
			html.append("<th>Value</th>");
			html.append("</thead>");

			if (criteriaMap.containsKey("test")) {
				html.append(getHtmlTr(i));
				html.append("<td>Test</td>");
				html.append("<td>Testing 123</td>");
				html.append("</tr>");
				i++;
			}

			html.append("</table>");
			html.append("<br />");
		}
		return html.toString();
	}

	/**
	 *
	 * @param templateFile
	 * @param criteriaMap
	 * @return
	 */
	public String getContent(String templateFile, Map<String, String> criteriaMap) {
		String content = "";

		try {
			FileManager fileManager = new FileManager(CONST_TEMPLATE_PATH, templateFile);
			content = fileManager.readFile();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return content;
	}

	/**
	 *
	 * @param i
	 * @return
	 */
	public String getHtmlTr(int i) {
		return (i % 2) == 0 ? "<tr style='background-color: #dddddd;'>" : "<tr>";
	}

}

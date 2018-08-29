package com.utility;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class SendEmail implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(SendEmail.class);

	private static final int HIGHEST = 1;
	private static final int HIGH = 2;
	private static final int NORMAL = 3;
	private static final int LOW = 4;
	private static final int LOWEST = 5;

	public static final String CONST_HOST_MAIL = "mail.smtp.host";

	private HashMap<String, Object> sender;
	private HashMap<String, Object> recipients;
	private HashMap<String, Object> cc;
	private HashMap<String, Object> bcc;

	private Session session;

	private String subject;
	private String message;
	private String filename;
	private String path;

	private int priority;
	private boolean sendHTML;

	/**
	 *
	 * @param smtp
	 */
	public SendEmail(String smtp) {
		this(smtp, 3);
	}

	public SendEmail(String smtp, int priority) {
		sendHTML = false;

		Properties props = System.getProperties();
		props.put(CONST_HOST_MAIL, smtp);
		this.priority = priority;
		sender = new HashMap<String, Object>();
		recipients = new HashMap<String, Object>();
		cc = new HashMap<String, Object>();
		bcc = new HashMap<String, Object>();
	}

	/**
	 *
	 */
	public void sent() {
		try {
			MimeMessage message = new MimeMessage(session);
			message.addHeader("X-Priority", String.valueOf(priority));

			if (getSender() != null) {
				InternetAddress ia = new InternetAddress();

				if (sender.get("1") != null) {
					ia.setAddress(String.valueOf(sender.get("1")));
				}

				if (sender.get("2") != null) {
					ia.setAddress(String.valueOf(sender.get("2")));
				}

				message.setFrom(ia);
			}

			if (getRecipients().size() > 0) {
				message.addRecipients(Message.RecipientType.TO, convertToAddress(getRecipients()));
			}

			if (getCarbonCopy().size() > 0) {
				message.addRecipients(Message.RecipientType.CC, convertToAddress(getCarbonCopy()));
			}

			if (getBlindCarbonCopy().size() > 0) {
				message.addRecipients(Message.RecipientType.BCC, convertToAddress(getBlindCarbonCopy()));
			}

			message.setSubject(getSubject());
			setBody(message);
			Transport.send(message);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 *
	 * @param message
	 * @throws MessagingException
	 */
	protected void setBody(MimeMessage message) throws MessagingException {
		if ((getFilename() != null) && (getPath() != null)) {
			Multipart multipart = new MimeMultipart();
			MimeBodyPart emailMessage = new MimeBodyPart();
			MimeBodyPart attachment = new MimeBodyPart();

			emailMessage.setContent(getMessage(), "text/html");

			FileDataSource fileDataSource = new FileDataSource(getPath() + "/" + getFilename()) {
				@Override
				public String getContentType() {
					return "application/octet-stream";
				}
			};

			attachment.setDataHandler(new DataHandler(fileDataSource));
			attachment.setFileName(getFilename());
			multipart.addBodyPart(emailMessage);
			multipart.addBodyPart(attachment);

			message.setContent(multipart);

		} else if (isSendHTML()) {
			message.setContent(getMessage(), "text/html");

		} else {
			message.setContent(getMessage(), "text/plain");
		}
	}

	/**
	 *
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	protected InternetAddress[] convertToAddress(HashMap<String, Object> map) throws UnsupportedEncodingException {
		int i = 0;
		InternetAddress[] address = new InternetAddress[map.size()];

		for (Object key : map.keySet()) {
			address[i] = new InternetAddress();
			address[i].setAddress(String.valueOf(key));
			if (map.get(key) != null) {
				address[i].setPersonal(String.valueOf(map.get(key)));
			}
			++i;
		}
		return address;
	}

	/**
	 *
	 * @param address
	 * @param name
	 */
	public void addSender(String address, String name) {
		recipients.put("1", address);
		recipients.put("2", name);
	}

	/**
	 *
	 * @param address
	 * @param name
	 */
	public void addRecipients(String address, String name) {
		recipients.put(address, name);
	}

	/**
	 *
	 * @param address
	 * @param name
	 */
	public void addBlindCarbonCopy(String address, String name) {
		bcc.put(address, name);
	}

	/**
	 *
	 * @param address
	 * @param name
	 */
	public void addCarbonCopy(String address, String name) {
		cc.put(address, name);
	}

	/**
	 * @return the sender
	 */
	public HashMap<String, Object> getSender() {
		return sender;
	}

	/**
	 * @param sender
	 *            the sender to set
	 */
	public void setSender(HashMap<String, Object> sender) {
		this.sender = sender;
	}

	/**
	 *
	 * @param address
	 * @param name
	 */
	public void setSender(String address, String name) {
		sender.put("1", address);
		sender.put("2", name);
	}

	/**
	 * @return the recipients
	 */
	public HashMap<String, Object> getRecipients() {
		return recipients;
	}

	/**
	 * @param recipients
	 *            the recipients to set
	 */
	public void setRecipients(HashMap<String, Object> recipients) {
		this.recipients = recipients;
	}

	/**
	 * @return the cc
	 */
	public HashMap<String, Object> getCarbonCopy() {
		return cc;
	}

	/**
	 * @param cc
	 *            the cc to set
	 */
	public void setCc(HashMap<String, Object> cc) {
		this.cc = cc;
	}

	/**
	 * @return the bcc
	 */
	public HashMap<String, Object> getBlindCarbonCopy() {
		return bcc;
	}

	/**
	 * @param bcc
	 *            the bcc to set
	 */
	public void setBcc(HashMap<String, Object> bcc) {
		this.bcc = bcc;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * @param priority
	 *            the priority to set
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}

	/**
	 * @return the sendHTML
	 */
	public boolean isSendHTML() {
		return sendHTML;
	}

	/**
	 * @param sendHTML
	 *            the sendHTML to set
	 */
	public void setSendHTML(boolean sendHTML) {
		this.sendHTML = sendHTML;
	}

}

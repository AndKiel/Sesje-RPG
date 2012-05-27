package rpgApp.utils

import rpgApp.main.IndexApplication

import com.vaadin.terminal.DownloadStream
import com.vaadin.terminal.ParameterHandler
import com.vaadin.terminal.URIHandler
import com.vaadin.ui.Window
import com.vaadin.ui.Window.Notification

public class UrlParameter implements URIHandler, ParameterHandler {
	private String email = null
	private String code = null
	private IndexApplication app

	UrlParameter(IndexApplication app) {
		this.app = app
	}
	/**
	 * Handle the URL parameters and store them for the URI
	 * handler to use.
	 */
	public void handleParameters(Map parameters) {
		// Get and store the passed HTTP parameter.
		if (parameters.containsKey("email")) {
			email = ((String[])parameters.get("email"))[0]
		}
		if (parameters.containsKey("code")) {
			code = ((String[])parameters.get("code"))[0]
		}
	}

	/**
	 * Provides the dynamic resource if the URI matches the
	 * resource URI. The matching URI is "/myresource" under
	 * the application URI context.
	 * 
	 * Returns null if the URI does not match. Otherwise
	 * returns a download stream that contains the response
	 * from the server.
	 */
	public DownloadStream handleURI(URL context, String relativeUri) {
		// Catch the given URI that identifies the resource,
		// otherwise let other URI handlers or the Application
		// to handle the response.
		if (!relativeUri.startsWith("activation")) {
			return
		}

		if(email == null || code == null) {
			app.getMainWindow().showNotification("Invalid activation link", Notification.TYPE_WARNING_MESSAGE)
		} else {
			String notification = "Account: "+email+" has been activated. You can now Log in"+code
			Window.Notification emailNotif = new Window.Notification(notification, Notification.TYPE_WARNING_MESSAGE)
			emailNotif.setDelayMsec(5000)
			app.getMainWindow().showNotification(emailNotif);
		}

		email = null
		code = null
		return null
	}
}
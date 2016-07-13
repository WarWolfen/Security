package security;

import java.util.Locale;
import java.util.ResourceBundle;

import user.User;

/**
 * Security Demo
 * 
 * @author Meldric
 *
 */
public class SecurityDemo {

	public static void main(String[] args) {

		String language = new String("en");
		String country = new String("US");

		Locale currentLocale;
		ResourceBundle messages;

		currentLocale = new Locale(language, country);
		messages = ResourceBundle.getBundle("PrivilegeDescriptionBundle", currentLocale);

		System.out.println(messages.getString(EPrivileges.USER_HAS_ROLE.name()));
		System.out.println(messages.getString(EPrivileges.ADMIN_DUMMY_02.name()));
		System.out.println(messages.getString(EPrivileges.ADMIN_IS_GOD_ADMIN.name()));

		System.out.println("-------------------------------------" + System.lineSeparator());

		Security.setDebug(false);
		Security.setDev(true);

		Security.printPrivilegeMatrix();

		User user = new User("Werner", 2);
		user.setPrivileges(23454);
		Security.listAllPrivileges(user);

		Security.grantPrivilege(user, EPrivileges.USER_HAS_ROLE);
		Security.listAllPrivileges(user);

		Security.revokePrivilege(user, EPrivileges.USER_HAS_ROLE);
		Security.revokePrivilege(user, EPrivileges.USER_DUMMY_01);
		Security.listAllPrivileges(user);

		Security.grantPrivilege(user, EPrivileges.ADMIN_IS_GOD_ADMIN);
		Security.listAllPrivileges(user);

		Security.hasPrivilege(user, EPrivileges.ADMIN_IS_GOD_ADMIN);
	}
}

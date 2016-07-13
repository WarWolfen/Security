package security;

import user.User;

/**
 * Security
 * 
 * @author Meldric
 *
 */
public class Security {

	private static boolean debug = false;
	private static boolean dev = false;

	/**
	 * Private constructor to prevent instantiation
	 */
	private Security() {
		// Private constructor to prevent instantiation
	}

	/**
	 * Check if a user is godadmin
	 * 
	 * @param user
	 * @return isGodAdmin
	 */
	private static boolean isGodAdmin(User user) {
		boolean isGodAdmin = false;
		long privs = user.getPrivileges();
		if (privs < 0) {
			isGodAdmin = true;
		}
		return isGodAdmin;
	}

	/**
	 * Lists all privileges for the given user
	 * 
	 * All privileges for the users are listed, even if the user is godadmin.
	 * It can be that the user has only the godadmin privilege, but this will
	 * guarantee that all checks for privileges will return true, even if not
	 * all individual privileges are granted.
	 * 
	 * @param user
	 */
	public static void listAllPrivileges(User user) {
		long p = 1;
		long privs = user.getPrivileges();

		StringBuffer info = new StringBuffer("List of privileges for " + user.getName() + ": " + System.lineSeparator());

		for (int i = 0; i < 64; i++) {
			if (debug) {
				System.out.println("Check for " + EPrivileges.values()[i]);
				System.out.println(String.format("%64s", Long.toBinaryString(privs)).replace(" ", "0"));
				System.out.println(String.format("%64s", Long.toBinaryString(p)).replace(" ", "0"));
				System.out.println(String.format("%64s", Long.toBinaryString(privs & p)).replace(" ", "0") + " "
						+ ((privs & p) > 0));
				System.out.println("----------------------------------------------------------------");
			}

			if ((privs & p) == p) {
				info.append("- " + EPrivileges.values()[i] + System.lineSeparator());
			}
			p = p << 1;
		}
		System.out.println(info);
		System.out.println("-------------------------------------" + System.lineSeparator());
	}

	/**
	 * Checks if the current user has a given privilege
	 * 
	 * @param priv
	 * @return
	 */
	public static boolean hasPrivilege(User user, EPrivileges priv) {
		if (isGodAdmin(user)) {
			if (dev) {
				StringBuffer info = new StringBuffer("");
				info.append(String.format("%15s", "User:          ") + user + System.lineSeparator());
				info.append(String.format("%15s", "Privilege:     ") + priv + System.lineSeparator());
				info.append(String.format("%15s", "Has privilege: ") + "true (god admin)");

				System.out.println(info);
				System.out.println("-------------------------------------" + System.lineSeparator());
			}
			return true;
		} else {
			boolean hasPrivilege = false;
			long p = priv.getValue();

			// Check if user has privilege
			// Take p (with value 1) and shift the one as often as the ordinal
			// of the given privilege. This results in a mask (like 000100000...).
			// This mask is & (AND) with the privileges of the user to see if the
			// user has the bit set. If yes, the user has the privilege.
			if ((user.getPrivileges() & p) == p) {
				hasPrivilege = true;
			}

			if (dev) {
				StringBuffer info = new StringBuffer("");
				info.append(String.format("%15s", "User:          ") + user + System.lineSeparator());
				info.append(String.format("%15s", "Privilege:     ") + priv + System.lineSeparator());
				info.append(String.format("%15s", "Has privilege: ") + hasPrivilege);

				System.out.println(info);
				System.out.println("-------------------------------------" + System.lineSeparator());
			}
			return hasPrivilege;
		}
	}

	/**
	 * Gives a privilege to a user
	 * 
	 * @param user
	 * @param priv
	 */
	public static void grantPrivilege(User user, EPrivileges priv) {
		long privs = user.getPrivileges();
		long privToGrant = priv.getValue();

		// To grant a privilege, the given privs are ^ed (XOR) with
		// the privilege to grant. So the bit for the given privilege
		// will be set.
		//
		// Example 1:
		// 00001010110 : Privileges of the user
		// 00001000000 : Privilege to grant
		// ----------- ^ (XOR)
		// 00001010110 : Result (nothing changed, the user had the privilege already)
		//
		// Example 2:
		// 00001010110 : Privileges of the user
		// 00000100000 : Privilege to grant
		// ----------- ^ (XOR)
		// 00001110110 : Result (the privilege was granted)
		user.setPrivileges(privs ^ privToGrant);
	}

	/**
	 * Revokes a privilege from a user
	 * 
	 * @param user
	 * @param priv
	 */
	public static void revokePrivilege(User user, EPrivileges priv) {
		long privs = user.getPrivileges();
		long privToRevoke = priv.getValue();

		// To revoke a privilege, the given privs are &ed (AND) with
		// the privilege to revoke. The result needs to be ^ed (XOR)
		// to the privileges of the user.
		//
		// Example 1:
		// 00001010110 : Privileges of the user
		// 00000100000 : Privilege to revoke
		// ----------- & (AND)
		// 00000000000 : Result 1
		// ----------- ^ (XOR to the privileges of the user, line 1)
		// 00001010110 : Result 2 (nothing changed, the user did not have the privilege)
		//
		// Example 2:
		// 00001010110 : Privileges of the user
		// 00001000000 : Privilege to revoke
		// ----------- & (AND)
		// 00001000000 : Result 1
		// ----------- ^ (XOR to the privileges of the user, line 1)
		// 00000010110 : Result 2 (the privilege was removed)
		user.setPrivileges((privs & privToRevoke) ^ privs);
	}

	/**
	 * Helper to print out the matrix of privileges and their
	 * numbers.
	 */
	public static void printPrivilegeMatrix() {

		if (dev) {
			long privs = 0b0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0000_0001L;

			// Loop starts from 0, because privs has already a value of 1
			// So, the one will be shifted 63 times to get 64 values
			for (int i = 0; i < 64; i++) {
				long priv = privs << i;

				StringBuffer info = new StringBuffer("");
				info.append(String.format("%30s", "" + EPrivileges.values()[i]));
				info.append("    ");
				info.append(String.format("%64s", Long.toBinaryString(priv)).replace(" ", "0"));
				info.append(String.format("%24s", priv));

				System.out.println(info);
			}
			System.out.println("-------------------------------------" + System.lineSeparator());
		} else {
			System.out.println("Enable Dev-Mode to see this information!");
		}
	}

	// Getters and Setters

	/**
	 * Gets debug flag for Security
	 * 
	 * @return debug
	 */
	public static boolean isDebug() {
		return debug;
	}

	/**
	 * Sets debug flag for Security
	 * 
	 * @param debug
	 */
	public static void setDebug(boolean debug) {
		Security.debug = debug;
	}

	/**
	 * Gets developer flag for Security
	 * 
	 * @return dev
	 */
	public static boolean isDev() {
		return dev;
	}

	/**
	 * Sets developer flag for Security
	 * 
	 * @param dev
	 */
	public static void setDev(boolean dev) {
		Security.dev = dev;
	}
}

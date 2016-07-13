package user;

/**
 * User
 * 
 * @author Meldric
 *
 */
public class User {

	private String name = "";
	private long privileges = 0;

	/**
	 * Constructor for name only
	 * 
	 * @param name
	 */
	public User(String name) {
		this(name, 0);
	}

	/**
	 * Constructor including name and privileges
	 * 
	 * @param name
	 * @param privileges
	 */
	public User(String name, long privileges) {
		this.name = name;
		this.privileges = privileges;
	}

	// Getters and Setters

	/**
	 * Gets the name
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the privileges
	 * 
	 * @return privileges
	 */
	public long getPrivileges() {
		return privileges;
	}

	/**
	 * Sets the privileges
	 * 
	 * @param privileges
	 */
	public void setPrivileges(long privileges) {
		this.privileges = privileges;
	}

	/**
	 * Replacement of the default toString
	 */
	@Override
	public String toString() {
		return name;
	}
}

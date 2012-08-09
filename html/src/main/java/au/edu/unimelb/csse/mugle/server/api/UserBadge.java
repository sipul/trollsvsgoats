package au.edu.unimelb.csse.mugle.server.api;

/** An instance of a badge in the current user's profile.
 */
class UserBadge {
	/** Whether the user has achieved this badge. */
	public boolean achieved = false;
	/** The amount of progress the user has made towards this badge. */
	public int progress = 0;
}

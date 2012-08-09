package au.edu.unimelb.csse.mugle.server.api;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import au.edu.unimelb.csse.mugle.shared.api.GameTokenError;

/** Utilities for dealing with game tokens.
 */
final class GameToken {
	/** Regular expression for UUID version 4. */
	private static Pattern uuid4_regex = Pattern.compile("[0-9a-fA-F]{8}-" +
			"[0-9a-fA-F]{4}-4[0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-" +
			"[0-9a-fA-F]{12}");

	/** Check whether a game token has a valid format.
	 * The expected format is that of a UUID version 4
	 * (see http://en.wikipedia.org/wiki/UUID).
	 */
	public static boolean valid_format(String token)
	{
		Matcher m = uuid4_regex.matcher(token);
		return m.matches();
	}

	/** Check whether a game token has a valid format, and throw a
	 * GameTokenError if it does not.
	 */	
	public static void validate_format(String token) throws GameTokenError
	{
		if (!valid_format(token))
			throw new GameTokenError("Invalid game token " + token);
	}
}

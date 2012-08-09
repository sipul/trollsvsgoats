package au.edu.unimelb.csse.mugle.server.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import au.edu.unimelb.csse.mugle.client.api.KeyValueService;
import au.edu.unimelb.csse.mugle.shared.api.GameTokenError;
import au.edu.unimelb.csse.mugle.shared.api.KeyError;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class KeyValueServiceImpl extends RemoteServiceServlet implements
		KeyValueService {
	/** Test implementation: Store the state in memory.
	 * Maps game tokens onto a map from keys to values.
	 */
	private static Map<String, Map<String, Serializable>> game_key_value_map =
			new HashMap<String, Map<String, Serializable>>();
	
	private Map<String, Serializable> get_key_value_map(String gameToken)
		throws GameTokenError {
		GameToken.validate_format(gameToken);
		Map<String, Serializable> key_value_map =
			game_key_value_map.get(gameToken);
		if (key_value_map == null)
		{
			key_value_map = new HashMap<String, Serializable>();
			game_key_value_map.put(gameToken, key_value_map);
		}
		return key_value_map;
	}
	
	@Override
	public void put(String gameToken, String key, Serializable value)
		throws GameTokenError {
		this.get_key_value_map(gameToken).put(key, value);
	}

	@Override
	public Serializable get(String gameToken, String key)
		throws GameTokenError, KeyError {
		if (this.get_key_value_map(gameToken).containsKey(key))
			return this.get_key_value_map(gameToken).get(key);
		else
			throw new KeyError(key);
	}

	@Override
	public boolean containsKey(String gameToken, String key)
		throws GameTokenError {
		return this.get_key_value_map(gameToken).containsKey(key);
	}

}

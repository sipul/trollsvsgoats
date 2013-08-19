package au.edu.unimelb.csse.mugle.server.api;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import au.edu.unimelb.csse.mugle.client.api.HighscoreService;
import au.edu.unimelb.csse.mugle.shared.api.GameTokenError;
import au.edu.unimelb.csse.mugle.shared.multiplayer.api.InstanceTokenError;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class HighScoreServiceImpl extends RemoteServiceServlet implements
		HighscoreService {
	/** Test implementation: Store the state in memory.
	 * Maps game tokens onto the user's score for that game.
	 */
	private static Map<String, Integer> game_score_map =
			new HashMap<String, Integer>();

	@Override
	public void saveScore(String gameToken, int score)
		throws GameTokenError {
		GameToken.validate_format(gameToken);
		Integer oldScore = game_score_map.get(gameToken);
		if (oldScore == null || score > oldScore)
			game_score_map.put(gameToken, score);
	}

	@Override
	public int getHighScore(String gameToken)
		throws GameTokenError {
		GameToken.validate_format(gameToken);
		Integer score = game_score_map.get(gameToken);
		if (score == null)
			return 0;
		return score;
	}

    @Override
    public LinkedHashMap<String, Integer> getRankedHighScores(String gameToken,
            long fromIncl, long toIncl, String instanceToken)
            throws GameTokenError, InstanceTokenError {
        LinkedHashMap<String, Integer> result = new LinkedHashMap<String, Integer>();
        result.put("test1", 3);
        result.put("test2", 2);
        result.put("test3", 1);
        return result;
    }
}

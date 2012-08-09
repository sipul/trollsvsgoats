/*  Melbourne University Game-based Learning Environment
 *  Copyright (C) 2011 The University of Melbourne
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package au.edu.unimelb.csse.mugle.client.api;

import au.edu.unimelb.csse.mugle.shared.api.GameTokenError;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Developer API for accessing the current user's high-score.
 * This allows the game to save the user's highest score.
 */
@RemoteServiceRelativePath("api-highscore")
public interface HighscoreService extends RemoteService
{
    /** Saves the score for the current player. If this is better than the
     * player's current high score, sets the new high score. Otherwise, does
     * nothing.
     * @param gameToken The secret token for the current game.
     * @param score The player's score.
     */
    public void saveScore(String gameToken, int score)
        throws GameTokenError;

    /** Gets the highest score of the current player.
     * @param gameToken The secret token for the current game.
     * @return The player's highest score.
     */
    public int getHighScore(String gameToken) throws GameTokenError;
}

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

package au.edu.unimelb.csse.mugle.client.multiplayer.api;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MultiPlayerGameServiceAsync {
    /**
     * Create a new game instance.
     * 
     * @param gameToken
     *            The secret token for the current game.
     * @param size
     *            The maximum number of player for the game.
     * @param isPublic
     *            True if the game can be joined public.
     * @param callback
     *            return the game instance token.
     */
    public void createGameInstance(String gameToken, int size,
            boolean isPublic, AsyncCallback<String> callback);

    /**
     * Join a game instance.
     * 
     * @param instanceToken
     *            The secret token for the game instance to join.
     * @param callback
     *            return a list of all the player names for this game.
     */
    public void joinGame(String instanceToken, AsyncCallback<String[]> callback);
}

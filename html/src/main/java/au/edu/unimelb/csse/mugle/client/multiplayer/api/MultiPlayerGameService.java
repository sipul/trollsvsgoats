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

import au.edu.unimelb.csse.mugle.shared.api.GameTokenError;
import au.edu.unimelb.csse.mugle.shared.multiplayer.api.GameInstanceNotExists;
import au.edu.unimelb.csse.mugle.shared.multiplayer.api.InstanceTokenError;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Services for managing game activity of a player.
 */
@RemoteServiceRelativePath("api-multiPlayerGame")
public interface MultiPlayerGameService extends RemoteService {
    /**
     * Create a new game instance.
     * 
     * @param gameToken
     *            The secret token for the current game.
     * @param size
     *            The maximum number of player for the game.
     * @param isPublic
     *            True if the game can be joined public.
     * @return the game instance token.
     * @throws GameTokenError
     */
    public String createGameInstance(String gameToken, int size,
            boolean isPublic) throws GameTokenError;

    /**
     * Join a game instance.
     * 
     * @param instanceToken
     *            The secret token for the game instance to join.
     * @return a list of all the player names for this game.
     * @throws InstanceTokenError
     * @throws GameInstanceNotExists
     */
    public String[] joinGame(String instanceToken) throws InstanceTokenError,
            GameInstanceNotExists;
}

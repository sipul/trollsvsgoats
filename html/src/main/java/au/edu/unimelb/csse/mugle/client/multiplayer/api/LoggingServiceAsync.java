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

import au.edu.unimelb.csse.mugle.shared.multiplayer.api.GameState;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Services for logging game state.
 */
public interface LoggingServiceAsync {
    /**
     * Create a log for the current game.
     * 
     * @param instanceToken
     *            The secret token of the current game instance.
     * @param state
     *            the game state.
     * @param hasLogMessages
     *            whether the log contains messages which are sent during the
     *            game play.
     */
    void log(String instanceToken, GameState state, boolean hasLogMessages,
            AsyncCallback<Void> callback);

}

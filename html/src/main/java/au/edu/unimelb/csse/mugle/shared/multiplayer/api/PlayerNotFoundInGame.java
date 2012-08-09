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

package au.edu.unimelb.csse.mugle.shared.multiplayer.api;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PlayerNotFoundInGame extends Exception implements Serializable,
        IsSerializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = -8185329216181264778L;

    public PlayerNotFoundInGame() {
        super();
    }

    public PlayerNotFoundInGame(String playerName) {
        super("Player not found in this game: " + playerName);
    }
}

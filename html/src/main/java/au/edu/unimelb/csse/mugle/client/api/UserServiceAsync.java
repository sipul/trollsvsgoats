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

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Developer async API for accessing information about the current user.
 */
public interface UserServiceAsync
{
    /** Gets the the nickname of the user playing the game.
     * This should be used whenever the user's name needs to be displayed.
     * Note that this nickname may change, so it should not be used to
     * permanently associate information with the user (see getUserID).
     * @param callback Called once the operation finishes. On success, passes
     *  the nickname.
     */
    void getUserNickName(AsyncCallback<String> callback);

    /** Gets the user ID of the user playing the game.
     * This is guaranteed never to change for this user, so it should be used
     * any time some permanent information needs to be associated with a user.
     * @param callback Called once the operation finishes. On success, passes
     *  the user ID.
     */
    void getUserID(AsyncCallback<String> callback);
}

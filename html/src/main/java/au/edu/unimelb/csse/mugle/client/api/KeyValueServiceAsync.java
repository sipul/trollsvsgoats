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

import java.io.Serializable;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Developer async API for accessing the key/value store.
 * This allows each game to associate arbitrary data with string keys,
 * independently for each user.
 */
public interface KeyValueServiceAsync
{
    /** Store an object in the key-value store.
     * The value will be specific to the current user, and will only be able
     * to be retrieved by the same user.
     * @param gameToken The secret token for the current game.
     * @param key The key to associate the object with.
     * @param value The object to store.
     * @param callback Called once the operation finishes.
     */
    public void put(String gameToken, String key, Serializable value,
        AsyncCallback<Void> callback);

    /** Retrieve an object from the key-value store.
     * The value will be the one stored by the current user.
     * @param gameToken The secret token for the current game.
     * @param key The key the object is associated with.
     * @param callback Called once the operation finishes. On success, passes
     *  the object associated with the key. Fails (KeyError) if no object is
     *  associated with that key.
     */
    public void get(String gameToken, String key,
        AsyncCallback<Serializable> callback);

    /** Test whether a key has an associated object for the current user.
     * @param gameToken The secret token for the current game.
     * @param key The key the object is associated with.
     * @param callback Called once the operation finishes. On success, passes
     *  true if a value is associated with the key.
     */
    public void containsKey(String gameToken, String key,
        AsyncCallback<Boolean> callback);
}

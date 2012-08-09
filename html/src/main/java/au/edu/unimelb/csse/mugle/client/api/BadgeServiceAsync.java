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

import au.edu.unimelb.csse.mugle.shared.api.Badge;

/**
 * Developer async API for accessing the badges (achievements) system.
 * This allows each game to award the user with badges, as well as update the
 * progress of badges.
 */
public interface BadgeServiceAsync
{
    /** Get a list of badge names for this game.
     * @param gameToken The secret token for the current game.
     * @param callback Called once the operation finishes. On success, passes
     *  a list of strings corresponding to the 'name' field of each badge in
     *  the current game.
     */
    void getBadgeNames(String gameToken, AsyncCallback<String[]> callback);

    /** Get the static (user-independent) information about a badge for the
     * current game.
     * @param gameToken The secret token for the current game.
     * @param name The name of the badge.
     * @param callback Called once the operation finishes. On success, passes
     *  information about that badge. Fails (BadgeError) if no badge by that
     *  name.
     */
    void getBadgeInfo(String gameToken, String name,
        AsyncCallback<Badge> callback);

    /** Sets the Badge to the "achieved" state.
     * @param gameToken The secret token for the current game.
     * @param name The name of the badge
     * @param callback Called once the operation finishes. Fails (BadgeError)
     * if no badge by that name.
     */
    void setAchieved(String gameToken, String name,
        AsyncCallback<Void> callback);

    /** Increments the progress of the badge by the given amount.
     * If the progress reaches the maximum for this badge, sets the badge to
     * achieved. The progress is capped at the badge maximum.
     * @param gameToken The secret token for the current game.
     * @param name The name of the badge
     * @param amount The amount to increment the badge by. Must not be
     *  negative.
     * @param callback Called once the operation finishes. On success, passes
     * true if the badge was achieved as a result. Fails (BadgeError) if no
     * badge by that name.
     */
    void incrementProgress(String gameToken, String name, int amount,
            AsyncCallback<Boolean> callback);

    /** Increments the progress of the badge by 1.
     * @param gameToken The secret token for the current game.
     * @param name The name of the badge
     * @param callback Called once the operation finishes. On success, passes
     * true if the badge was achieved as a result. Fails (BadgeError) if no
     * badge by that name.
     */
    void incrementProgress(String gameToken, String name,
            AsyncCallback<Boolean> callback);

    /** Checks whether the badge has been achieved by the player.
     * @param gameToken The secret token for the current game.
     * @param name The name of the badge
     * @param callback Called once the operation finishes. On success, passes
     * true if the badge is achieved. Fails (BadgeError) if no badge by that
     * name.
     */
    void isAchieved(String gameToken, String name,
            AsyncCallback<Boolean> callback);

    /** Returns the progress of the current badge.
     * Use getBadgeInfo to retrieve the maximum progress for the badge.
     * @param gameToken The secret token for the current game.
     * @param name The name of the badge
     * @param callback Called once the operation finishes. On success, passes
     * the progress of the badge. Fails (BadgeError) if no badge by that name.
     */
    void getProgress(String gameToken, String name,
            AsyncCallback<Integer> callback);
}

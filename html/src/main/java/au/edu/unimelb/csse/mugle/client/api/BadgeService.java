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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import au.edu.unimelb.csse.mugle.shared.api.Badge;
import au.edu.unimelb.csse.mugle.shared.api.BadgeError;
import au.edu.unimelb.csse.mugle.shared.api.GameTokenError;

/**
 * Developer API for accessing the badges (achievements) system.
 * This allows each game to award the user with badges, as well as update the
 * progress of badges.
 */
@RemoteServiceRelativePath("api-badge")
public interface BadgeService extends RemoteService
{
    /** Get a list of badge names for this game.
     * @param gameToken The secret token for the current game.
     * @return List of strings corresponding to the 'name' field of each
     *  badge in the current game.
     */
    public String[] getBadgeNames(String gameToken)
        throws GameTokenError;

    /** Get the static (user-independent) information about a badge for the
     * current game.
     * @param gameToken The secret token for the current game.
     * @param name The name of the badge.
     * @return Information about that badge.
     * @throws BadgeError If no badge by that name.
     */
    public Badge getBadgeInfo(String gameToken, String name)
        throws GameTokenError, BadgeError;

    /** Sets the Badge to the "achieved" state.
     * @param gameToken The secret token for the current game.
     * @param name The name of the badge
     * @throws BadgeError If no badge by that name.
     */
    public void setAchieved(String gameToken, String name)
        throws GameTokenError, BadgeError;

    /** Increments the progress of the badge by the given amount.
     * If the progress reaches the maximum for this badge, sets the badge to
     * achieved. The progress is capped at the badge maximum.
     * @param gameToken The secret token for the current game.
     * @param name The name of the badge
     * @param amount The amount to increment the badge by. Must not be
     *  negative.
     * @return true if the badge was achieved as a result.
     * @throws BadgeError If no badge by that name.
     */
    public boolean incrementProgress(String gameToken, String name,
        int amount) throws GameTokenError, BadgeError;

    /** Increments the progress of the badge by 1.
     * @param gameToken The secret token for the current game.
     * @param name The name of the badge
     * @return true if the badge was achieved as a result.
     * @throws BadgeError If no badge by that name.
     */
    public boolean incrementProgress(String gameToken, String name)
        throws GameTokenError, BadgeError;

    /** Checks whether the badge has been achieved by the player.
     * @param gameToken The secret token for the current game.
     * @param name The name of the badge
     * @return true if achieved, false otherwise
     * @throws BadgeError If no badge by that name.
     */
    public boolean isAchieved(String gameToken, String name)
        throws GameTokenError, BadgeError;

    /** Returns the progress of the current badge.
     * Use getBadgeInfo to retrieve the maximum progress for the badge.
     * @param gameToken The secret token for the current game.
     * @param name The name of the badge
     * @return The progress of the badge
     * @throws BadgeError If no badge by that name.
     */
    public int getProgress(String gameToken, String name)
        throws GameTokenError, BadgeError;
}

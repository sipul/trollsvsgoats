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

package au.edu.unimelb.csse.mugle.shared.api;

import java.io.Serializable;

/** Provides static (user-independent) information about a badge.
 * This is the information that the developers have provided to describe a
 * badge in the MUGLE control panel.
 */
public class Badge implements Serializable {
    private static final long serialVersionUID = 4335025238357031611L;

    private String name;
    private String displayName;
    private String description;
    private int maxProgress;

    public Badge()
    {
        this.name = "";
        this.displayName = "";
        this.description = "";
        this.maxProgress = 0;
    }

    /** Construct a new Badge object. */
    public Badge(String name, String displayName, String description,
            int maxProgress)
    {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.maxProgress = maxProgress;
    }

    /** The short name used to identify a badge. */
    public String getName() {
        return name;
    }

    /** The long "friendly" string which names the badge for users. */
    public String getDisplayName() {
        return displayName;
    }

    /** The even longer text which describes how to earn the badge.
     * This should be in the form of an instruction, such as "rescue the
     * princess."
     */
    public String getDescription() {
        return description;
    }

    /** For progress badges, the total number of progress units required to
     * earn the badge. For non-progress badges, 0.
     */
    public int getMaxProgress() {
        return maxProgress;
    }

    @Override
    public String toString() {
        return "Badge [" +
            this.name + ", " +
            this.displayName + ", " +
            this.description + ", " +
            this.maxProgress + "]";
    }
}

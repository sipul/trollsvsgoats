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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/** Provides access to pre-initialised instances of all the asynchronous
 * services offered by MUGLE.
 */
public class Services
{
    /** An instance of the asynchronous user service. */
    public static final UserServiceAsync user =
        GWT.create(UserService.class);
    /** An instance of the asynchronous key-value service. */
    public static final KeyValueServiceAsync keyvalue =
        GWT.create(KeyValueService.class);
    /** An instance of the asynchronous badge service. */
    public static final BadgeServiceAsync badges =
        GWT.create(BadgeService.class);
    /** An instance of the asynchronous high score service. */
    public static final HighscoreServiceAsync highscore =
        GWT.create(HighscoreService.class);

    static
    {
        ((ServiceDefTarget) badges).setServiceEntryPoint("/mugle/api-badge");
        ((ServiceDefTarget) highscore).setServiceEntryPoint(
                "/mugle/api-highscore");
        ((ServiceDefTarget) keyvalue).setServiceEntryPoint(
                "/mugle/api-keyvalue");
        ((ServiceDefTarget) user).setServiceEntryPoint("/mugle/api-user");
    }
}

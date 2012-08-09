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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Provides access to pre-initialised instances of all the asynchronous
 * multi-player game services offered by MUGLE.
 */
public class MultiServices {
    /** An instance of the asynchronous multi-player game service. */
    public static final MultiPlayerGameServiceAsync multigame = GWT
            .create(MultiPlayerGameService.class);

    /** An instance of the asynchronous logging service. */
    public static final LoggingServiceAsync logging = GWT
            .create(LoggingService.class);

    static {
        ((ServiceDefTarget) multigame)
                .setServiceEntryPoint("/mugle/api-multiPlayerGame");
        ((ServiceDefTarget) logging).setServiceEntryPoint("/mugle/api-logging");
    }
}

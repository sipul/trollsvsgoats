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

import java.util.List;
import java.util.Map;

/** Interface indicating that a type contains game state */
public interface HasGameState {
    public String getAttribute(String name);

    public Map<String, String> getAttributeValuePairs();

    public List<String> getList(String attribute);

    public Map<String, List<String>> getAttributeListPairs();

    public void addAttribute(String attribute, String value);

    public void setAttributeValuePairs(Map<String, String> attributeValuePairs);

    public void addList(String attribute, List<String> list);

    public void setAttributeListPairs(
            Map<String, List<String>> attributeListPairs);
}

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/** The game state of a game instance */
public class GameState implements Serializable, IsSerializable, HasGameState {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1834183271711438452L;
    private Map<String, String> attributeValuePairs;
    private Map<String, List<String>> attributeListPairs;

    /**
     * Return an attribute.
     * 
     * @param name
     *            the attribute name.
     */
    public String getAttribute(String name) {
        if (attributeValuePairs == null)
            return null;
        else
            return attributeValuePairs.get(name);
    }

    /**
     * Return the attribute-value pairs.
     * 
     * @return the attribute-value pairs.
     */
    public Map<String, String> getAttributeValuePairs() {
        return attributeValuePairs;
    }

    /**
     * Add an attribute.
     * 
     * @param attribute
     *            the attribute.
     * @param value
     *            the value.
     */
    public void addAttribute(String attribute, String value) {
        if (attributeValuePairs == null)
            attributeValuePairs = new HashMap<String, String>();
        attributeValuePairs.put(attribute, value);
    }

    /**
     * Set the attribute-value pairs.
     * 
     * @param attributeValuePairs
     *            the attribute-value pairs.
     */
    public void setAttributeValuePairs(Map<String, String> attributeValuePairs) {
        this.attributeValuePairs = attributeValuePairs;
    }

    /**
     * Return an attribute list.
     * 
     * @param attribute
     *            the attribute of the list.
     */
    public List<String> getList(String attribute) {
        if (attributeListPairs == null)
            return null;
        else
            return attributeListPairs.get(attribute);
    }

    /**
     * Return the attribute-list pairs.
     * 
     * @return the attribute-list pairs.
     */
    public Map<String, List<String>> getAttributeListPairs() {
        return attributeListPairs;
    }

    /**
     * Add an attribute-list.
     * 
     * @param attribute
     *            the attribute.
     * @param list
     *            the list.
     */
    public void addList(String attribute, List<String> list) {
        if (attributeListPairs == null)
            attributeListPairs = new HashMap<String, List<String>>();
        attributeListPairs.put(attribute, list);
    }

    /**
     * Set the attribute-list pairs.
     * 
     * @param attributeListPairs
     *            the attribute-list pairs.
     */
    public void setAttributeListPairs(
            Map<String, List<String>> attributeListPairs) {
        this.attributeListPairs = attributeListPairs;
    }
}

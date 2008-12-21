/*
 * Copyright (C) 2005-2008 Alfresco Software Limited. This program is free
 * software; you can redistribute it and/or modify it under the terms of the GNU
 * General Public License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version. This program
 * is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You
 * should have received a copy of the GNU General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin
 * Street, Fifth Floor, Boston, MA 02110-1301, USA. As a special exception to
 * the terms and conditions of version 2.0 of the GPL, you may redistribute this
 * Program in connection with Free/Libre and Open Source Software ("FLOSS")
 * applications as described in Alfresco's FLOSS exception. You should have
 * recieved a copy of the text describing the FLOSS exception, and it is also
 * available here: http://www.alfresco.com/legal/licensing"
 */

package sample.model.constraint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/*
 * This class contains utilities that help convert propertyName string with
 * substitution tokens of form ${token} to the string with substituted values.
 * It should handle an arbitrary number of substitution tokens. For example:
 * propertyName TYPE:"{http://www.alfresco.org/model/content/1.0}content" AND
 * (@\{http\://www.alfresco.org/model/content/1.0\}name:"${my:authorisedBy}"
 * will gets ${my:authorisedBy} replaced by its actual value.
 */

public class MultipleValueDependentSearchListConstraint extends LuceneSearchBasedListConstraint {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(MultipleValueDependentSearchListConstraint.class);

	protected String propertyName;

	public MultipleValueDependentSearchListConstraint() {
	}

	protected List<String> getSearchResult() {
		if (log.isDebugEnabled())
			log.debug("Original Query  " + propertyName);

		// for(String key : node.getProperties().keySet()) log.info(key + " " +
		// node.getProperties().get(key));
		List<String> allowedValues = new ArrayList<String>();
		Object topFolders = node.getProperties().get(propertyName);
		if (topFolders instanceof List) {
			Map<String, String> map = new HashMap<String, String>();
			for (String topFolder : (List<String>) topFolders) {
				map.put("XXX", topFolder);
				String finalQuery = replaceQueryParametersWithValues(query, map);
				log.info("aftre " + finalQuery);
				searchForAllowedValues(finalQuery, allowedValues);
			}
		}
		// the UI cannot render drop down without any elements, so add at least
		// one.
		if (allowedValues.size() == 0)
			allowedValues.add("");
		return allowedValues;
	}

	public void setPropertyName(String prop) {
		propertyName = prop.trim();
	}

}

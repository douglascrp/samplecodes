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
import java.util.List;

import org.alfresco.model.ContentModel;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.ResultSetRow;
import org.alfresco.service.cmr.search.SearchService;
import org.apache.log4j.Logger;

/*
 * This class contains utilities that help convert query string with
 * substitution tokens of form ${token} to the string with substituted values.
 * It should handle an arbitrary number of substitution tokens. For example:
 * query TYPE:"{http://www.alfresco.org/model/content/1.0}content" AND
 * (@\{http\://www.alfresco.org/model/content/1.0\}name:"${my:authorisedBy}"
 * will gets ${my:authorisedBy} replaced by its actual value.
 */

public class LuceneSearchBasedListConstraint extends SearchBasedDependencyListConstraint {

	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(LuceneSearchBasedListConstraint.class);

	protected String query;

	protected StoreRef getStoreRef() {
		return new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore");
	}

	private String childPattern;

	public LuceneSearchBasedListConstraint() {
	}

	protected List<String> getSearchResult() {
		if (log.isDebugEnabled())
			log.debug("Original Query  " + query);

		String finalQuery = resolveDependenciesOnProperties(query);

		List<String> allowedValues = new ArrayList<String>();
		searchForAllowedValues(finalQuery, allowedValues);
		// the UI cannot render drop down without any elements, so add at least
		// one.
		if (allowedValues.size() == 0)
			allowedValues.add("");
		return allowedValues;
	}

	protected void searchForAllowedValues(String query, List<String> allowedValues) {
		if (log.isDebugEnabled())
			log.debug("Query to get Allowed values " + query);
		StoreRef storeRef = getStoreRef();
		ResultSet resultSet = getServiceRegistry().getSearchService().query(storeRef, SearchService.LANGUAGE_LUCENE, query);
		NodeService nodeSvc = getServiceRegistry().getNodeService();
		log.info("resultSet.length() " + resultSet.length());

		if (childPattern != null && resultSet.length() != 0) {
			ResultSetRow row = resultSet.getRow(0);
			for (ChildAssociationRef childAssociationRef : nodeSvc.getChildAssocs(row.getNodeRef())) {
				allowedValues.add((String) nodeSvc.getProperty(childAssociationRef.getChildRef(), ContentModel.PROP_NAME));
			}
		} else {
			for (ResultSetRow row : resultSet) {
				allowedValues.add((String) nodeSvc.getProperty(row.getNodeRef(), ContentModel.PROP_NAME));
			}
		}
	}

	public void setQuery(String newquery) {
		query = newquery;
	}

	public String getChildPattern() {
		return childPattern;
	}

	public void setChildPattern(String childPattern) {
		this.childPattern = childPattern;
	}
}

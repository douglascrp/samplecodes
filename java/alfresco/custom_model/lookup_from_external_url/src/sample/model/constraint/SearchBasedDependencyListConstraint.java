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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.alfresco.web.bean.repository.Node;
import org.apache.log4j.Logger;

public abstract class SearchBasedDependencyListConstraint extends SearchBasedListConstraint {
	private static Logger log = Logger.getLogger(SearchBasedDependencyListConstraint.class);
	protected Node node;

	protected String resolveDependenciesOnProperties(String query) {
		List<String> propNames = getPropertyNames(query, getTokenExpression());
		Map<String, String> map = populateNodeValues(propNames, node);
		
		String newQuery = replaceQueryParametersWithValues(query, map);
		return newQuery;
	}
	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	/**
	 * Pulls out tokens corresponding to property names
	 * 
	 * @param query
	 * @return
	 */
	private List<String> getPropertyNames(String query, String tokenRegexpExpression) {
		Pattern patternMatcher = Pattern.compile(tokenRegexpExpression);
		Matcher matcher = patternMatcher.matcher(query);
		List<String> arr = new ArrayList<String>();
		while (matcher.find()) {
			String propToken = matcher.group();
			//log.info("propToken " + propToken);
			propToken = propToken.substring(2, propToken.length() - 1);
			arr.add(propToken);
		}
		return arr;
	}

	private Map<String, String> populateNodeValues(List<String> propNames, Node node) {
		Map<String, String> result = new HashMap<String, String>();
		for (String propName : propNames) {
			log.info("looking for propName " + propName);
			if (node.hasProperty(propName)) {
				//log.info("find value " + node.getProperties().get(propName).toString());
				result.put(propName, node.getProperties().get(propName).toString());
			}
		}
		return result;
	}

	/**
	 * Pulls out tokens corresponding to property names
	 * 
	 * @param query
	 * @return
	 */
	private String replaceQueryParametersWithValues(String query, Map<String, String> props) {
		String finalQuery = query;
		for (String key : props.keySet()) {
			String token = "\\$\\{" + key + "\\}";
			finalQuery = query.replaceAll(token, props.get(key));
		}
		return finalQuery;
	}
}

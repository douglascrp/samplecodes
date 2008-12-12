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

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.alfresco.repo.dictionary.constraint.ListOfValuesConstraint;
import org.alfresco.service.ServiceRegistry;
import org.apache.log4j.Logger;

// TODO:

/*
 * Sample configuration <constraint name="my:customConstraint"
 * type="org.alfresco.sample.web.SearchBasedListConstraint" > <parameter
 * name="query"> <value>
 * TYPE:"{http://www.alfresco.org/model/content/1.0}content" </value>
 * </parameter> </constraint> <property name="my:authorisedBy">
 * <title>Authorized By</title> <type>d:text</type> <constraints> <constraint
 * ref="my:customConstraint" /> </constraints> </property>
 */

public abstract class SearchBasedListConstraint extends ListOfValuesConstraint
		implements Serializable {
	private static Logger log = Logger
			.getLogger(SearchBasedListConstraint.class);

	@Override
	public void initialize() {
	}

	@Override
	public List<String> getAllowedValues() {
		List<String> allowedValues = getSearchResult();
		super.setAllowedValues(allowedValues);
		return allowedValues;
	}

	protected abstract List<String> getSearchResult();

	/**
	 * Set the values that are allowed by the constraint.
	 * 
	 * @param values
	 *            a list of allowed values
	 */
	@SuppressWarnings("unchecked")
	public void setAllowedValues(List allowedValues) {
	}

	@Override
	protected void evaluateCollection(Collection<Object> collection) {
	}

	/** Null implementation effectively turns of constraints checking */
	@Override
	protected void evaluateSingleValue(Object value) {
	}

	private static ServiceRegistry registry;

	public ServiceRegistry getServiceRegistry() {
		return registry;
	}

	public static void setServiceRegistry(ServiceRegistry registry) {
		SearchBasedListConstraint.registry = registry;
	}

	protected static String tokenExpression = "\\$\\{[a-zA-Z]+:[a-zA-Z]+\\}";

	public void setTokenExpression(String value) {
		tokenExpression = value;
	}

	public String getTokenExpression() {
		return tokenExpression;
	}

}

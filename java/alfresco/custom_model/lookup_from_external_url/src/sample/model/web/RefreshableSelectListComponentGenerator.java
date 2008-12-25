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

package sample.model.web;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.alfresco.repo.dictionary.constraint.ListOfValuesConstraint;
import org.alfresco.service.cmr.dictionary.Constraint;
import org.alfresco.service.cmr.dictionary.ConstraintDefinition;
import org.alfresco.service.cmr.dictionary.DataTypeDefinition;
import org.alfresco.service.cmr.dictionary.PropertyDefinition;
import org.alfresco.web.app.servlet.FacesHelper;
import org.alfresco.web.bean.generator.TextFieldGenerator;
import org.alfresco.web.bean.repository.Node;
import org.alfresco.web.ui.repo.component.property.PropertySheetItem;
import org.alfresco.web.ui.repo.component.property.UIPropertySheet;
import org.apache.log4j.Logger;

import sample.model.constraint.LuceneSearchBasedListConstraint;
import sample.model.constraint.SearchBasedListConstraint;

public class RefreshableSelectListComponentGenerator extends TextFieldGenerator {
    private static Logger log = Logger.getLogger(RefreshableSelectListComponentGenerator.class);

    private boolean autoRefresh = false;

    public boolean isAutoRefresh() {
        return autoRefresh;
    }

    /**
     * This gets set from faces-config-beans.xml, and allows some drop downs to be automaticlaly refreshable (i.e. country), and others not (i.e. city).
     */
    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    protected UIComponent createComponent(FacesContext context, UIPropertySheet propertySheet, PropertySheetItem item) {
        UIComponent component = null;

        if (propertySheet.inEditMode()) {
            // if the field has the list of values constraint and it is editable a SelectOne component is required otherwise create the standard edit component
            ListOfValuesConstraint constraint = getListOfValuesConstraint(context, propertySheet, item);

            PropertyDefinition propDef = this.getPropertyDefinition(context, propertySheet.getNode(), item.getName());

            if (constraint != null && item.isReadOnly() == false && propDef != null && propDef.isProtected() == false) {
                component = context.getApplication().createComponent(UISelectOne.COMPONENT_TYPE);
                FacesHelper.setupComponentId(context, component, item.getName());

                // create the list of choices
                UISelectItems itemsComponent = (UISelectItems) context.getApplication().createComponent("javax.faces.SelectItems");

                List<SelectItem> items = new ArrayList<SelectItem>(3);
                String name = item.getResolvedDisplayLabel();
                if(name == null) name = item.getName();
                items.add(new SelectItem("", "Select a " + name + " ..."));
                List<String> values = constraint.getAllowedValues();
                for (String value : values) {
                    Object obj = null;

                    // we need to setup the list with objects of the correct type
                    if (propDef.getDataType().getName().equals(DataTypeDefinition.INT)) {
                        obj = Integer.valueOf(value);
                    } else if (propDef.getDataType().getName().equals(DataTypeDefinition.LONG)) {
                        obj = Long.valueOf(value);
                    } else if (propDef.getDataType().getName().equals(DataTypeDefinition.DOUBLE)) {
                        obj = Double.valueOf(value);
                    } else if (propDef.getDataType().getName().equals(DataTypeDefinition.FLOAT)) {
                        obj = Float.valueOf(value);
                    } else {
                        obj = value;
                    }

                    if(value.trim().length() != 0) items.add(new SelectItem(obj, value));
                }
                if (log.isDebugEnabled()) log.debug("inspecting empty drop down" + items.size() + " " + ((String) ((SelectItem) items.get(0)).getValue()).trim().length());
                if (items.size() == 0 || items.size() == 1 && ((String) ((SelectItem) items.get(0)).getValue()).trim().length() == 0) {
                    component.setRendered(false);
                } else {
                    component.setRendered(true);
                }
                itemsComponent.setValue(items);

                // add the items as a child component
                component.getChildren().add(itemsComponent);
                if (isAutoRefresh()) {
                    component.getAttributes().put("onchange", "submit()");

                }
            } else
                return super.createComponent(context, propertySheet, item);
        } else
            return super.createComponent(context, propertySheet, item);
        return component;
    }

    /**
     * Retrieves the list of values constraint for the item, if it has one
     * 
     * @param context
     *            FacesContext
     * @param propertySheet
     *            The property sheet being generated
     * @param item
     *            The item being generated
     * @return The constraint if the item has one, null otherwise
     */
    protected ListOfValuesConstraint getListOfValuesConstraint(FacesContext context, UIPropertySheet propertySheet, PropertySheetItem item) {
        Constraint constraint = null;

        // get the property definition for the item
        PropertyDefinition propertyDef = getPropertyDefinition(context, propertySheet.getNode(), item.getName());

        if (propertyDef != null) {
            // go through the constaints and see if it has the list of values constraint
            List<ConstraintDefinition> constraints = propertyDef.getConstraints();
            for (ConstraintDefinition constraintDef : constraints) {
                constraint = constraintDef.getConstraint();
                if (constraint instanceof LuceneSearchBasedListConstraint) {
                    Node currentNode = (Node) propertySheet.getNode();
                    // This is a workaround for the fact that constraints do not have a reference to Node.
                    if (log.isDebugEnabled()) log.debug("current node: " + currentNode);
                    ((LuceneSearchBasedListConstraint) constraint).setNode(currentNode);
                    break;
                }

                if (constraint instanceof ListOfValuesConstraint) {
                    break;
                }
            }
        }
        return (ListOfValuesConstraint) constraint;
    }
}

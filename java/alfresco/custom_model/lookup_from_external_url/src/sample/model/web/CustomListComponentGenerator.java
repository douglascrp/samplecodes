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

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;

import org.alfresco.repo.dictionary.constraint.ListOfValuesConstraint;
import org.alfresco.service.cmr.dictionary.AssociationDefinition;
import org.alfresco.service.cmr.dictionary.Constraint;
import org.alfresco.service.cmr.dictionary.ConstraintDefinition;
import org.alfresco.service.cmr.dictionary.PropertyDefinition;
import org.alfresco.web.bean.generator.TextFieldGenerator;
import org.alfresco.web.bean.repository.Node;
import org.alfresco.web.ui.repo.component.property.PropertySheetItem;
import org.alfresco.web.ui.repo.component.property.UIProperty;
import org.alfresco.web.ui.repo.component.property.UIPropertySheet;
import org.apache.log4j.Logger;

import sample.model.constraint.LuceneSearchBasedListConstraint;
import sample.model.constraint.SearchBasedListConstraint;

/**
 * Generates a text field component.
 * 
 * @author jbarmash
 */
public class CustomListComponentGenerator extends TextFieldGenerator {
    private static Logger log = Logger.getLogger(CustomListComponentGenerator.class);

    // private String tutorialQuery =
    // "( TYPE:\"{http://www.alfresco.org/model/content/1.0}content\" AND
    // (@\\{http\\://www.alfresco.org/model/content/1.0\\}name:\"tutorial\"
    // TEXT:\"tutorial\"))"
    // ;

    private boolean autoRefresh = false;

    public boolean isAutoRefresh() {
        return autoRefresh;
    }

    /**
     * This gets set from faces-config-beans.xml, and allows some drop downs to
     * be automaticlaly refreshable (i.e. country), and others not (i.e. city).
     */
    public void setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected UIComponent createComponent(FacesContext context, UIPropertySheet propertySheet, PropertySheetItem item) {
        UIComponent component = super.createComponent(context, propertySheet, item);
        // log.info("********************** " + item + " >" + component + " >" +
        // (component instanceof UISelectOne) + " " + isAutoRefresh());
        if (component instanceof UISelectOne && isAutoRefresh()) {
            component.getAttributes().put("onchange", "submit()");
        }
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
        ListOfValuesConstraint lovConstraint = null;

        // log.info("propertySheet: " + propertySheet.getNode() + " item: " +
        // item.getName());
        // get the property definition for the item
        PropertyDefinition propertyDef = getPropertyDefinition(context, propertySheet.getNode(), item.getName());

        if (propertyDef != null) {
            // go through the constaints and see if it has the
            // list of values constraint
            List<ConstraintDefinition> constraints = propertyDef.getConstraints();
            for (ConstraintDefinition constraintDef : constraints) {
                Constraint constraint = constraintDef.getConstraint();
                // log.info("constraint: " + constraint);
                if (constraint instanceof LuceneSearchBasedListConstraint) {
                    Node currentNode = (Node) propertySheet.getNode();
                    // This is a workaround for the fact that constraints do not
                    // have a reference to Node.
                    ((LuceneSearchBasedListConstraint) constraint).setNode(currentNode);
                    lovConstraint = (SearchBasedListConstraint) constraint;
                    break;
                }

                if (constraint instanceof ListOfValuesConstraint) {
                    lovConstraint = (ListOfValuesConstraint) constraint;
                    break;
                }
            }
        }
        return lovConstraint;
    }

    public UIComponent generateAndReplace(FacesContext context, UIPropertySheet propertySheet, PropertySheetItem item) {
        UIComponent component = null;
        if (item instanceof UIProperty) {
            if (item.getChildCount() != 0) {
                item.getChildren().remove(item.getChildCount() - 1);
            }
            // get the property definition
            PropertyDefinition propertyDef = getPropertyDefinition(context, propertySheet.getNode(), item.getName());

            // create the component and add it to the property sheet
            component = createComponent(context, propertySheet, item);

            // setup the component for multi value editing if necessary
            component = setupMultiValuePropertyIfNecessary(context, propertySheet, item, propertyDef, component);

            // setup common aspects of the property i.e. value binding
            setupProperty(context, propertySheet, item, propertyDef, component);

            // add the component now, it needs to be added before the
            // validations
            // are setup as we need access to the component id, which in turn
            // needs
            // to have a parent to get the correct id
            item.getChildren().add(component);

            // setup the component for mandatory validation if necessary
            setupMandatoryPropertyIfNecessary(context, propertySheet, item, propertyDef, component);

            // setup any constraints the property has
            setupConstraints(context, propertySheet, item, propertyDef, component);

            // setup any converter the property needs
            setupConverter(context, propertySheet, item, propertyDef, component);
        } else {
            // get the association definition
            AssociationDefinition assocationDef = this.getAssociationDefinition(context, propertySheet.getNode(), item.getName());

            // create the component and add it to the property sheet
            component = createComponent(context, propertySheet, item);

            // setup common aspects of the association i.e. value binding
            setupAssociation(context, propertySheet, item, assocationDef, component);

            // add the component now, it needs to be added before the
            // validations
            // are setup as we need access to the component id, which needs have
            // a
            // parent to get the correct id
            item.getChildren().add(component);

            // setup the component for mandatory validation if necessary
            setupMandatoryAssociationIfNecessary(context, propertySheet, item, assocationDef, component);

            // setup any converter the association needs
            setupConverter(context, propertySheet, item, assocationDef, component);
        }

        return component;
    }
}

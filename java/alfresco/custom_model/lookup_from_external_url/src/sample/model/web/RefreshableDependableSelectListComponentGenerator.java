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

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.alfresco.service.cmr.dictionary.PropertyDefinition;
import org.alfresco.web.ui.repo.component.property.PropertySheetItem;
import org.alfresco.web.ui.repo.component.property.UIPropertySheet;
import org.apache.log4j.Logger;

public class RefreshableDependableSelectListComponentGenerator extends RefreshableSelectListComponentGenerator {
    private static Logger log = Logger.getLogger(RefreshableDependableSelectListComponentGenerator.class);

    public UIComponent replaceOptions(FacesContext context, UIPropertySheet propertySheet, PropertySheetItem item) {
        // this part makes it to be dynamic
        if (item.getChildCount() != 0) {
            if (log.isDebugEnabled()) log.debug("Removing  " + item.getChildren().get(item.getChildCount() - 1));
            item.getChildren().remove(item.getChildCount() - 1);
        }
        // get the property definition
        PropertyDefinition propertyDef = getPropertyDefinition(context, propertySheet.getNode(), item.getName());

        // create the component and add it to the property sheet
        UIComponent component = createComponent(context, propertySheet, item);

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
        return component;
    }
}

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

import org.alfresco.model.ContentModel;
import org.alfresco.repo.dictionary.constraint.ListOfValuesConstraint;
import org.alfresco.service.cmr.dictionary.PropertyDefinition;
import org.alfresco.service.cmr.repository.InvalidNodeRefException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.web.app.servlet.FacesHelper;
import org.alfresco.web.ui.repo.component.property.PropertySheetItem;
import org.alfresco.web.ui.repo.component.property.UIPropertySheet;
import org.apache.log4j.Logger;

public class SingleValueComponentGenerator extends RefreshableDependableSelectListComponentGenerator {
    private static Logger log = Logger.getLogger(SingleValueComponentGenerator.class);

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
                if (name == null) name = item.getName();
                List<String> nodeIdList = constraint.getAllowedValues();
                if (log.isDebugEnabled()) log.debug("nodeIdList.size()-----<" + nodeIdList.size());
                if (nodeIdList.size() == 1) {
                    String nodeId = nodeIdList.get(0);
                    NodeRef nodeRef = new NodeRef(new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore"), nodeId);
                    String value = "";
                    try {
                        value = (String) getNodeService().getProperty(nodeRef, ContentModel.PROP_NAME);
                    } catch (InvalidNodeRefException exception) {
                        // for now we ignore it
                        log.info("ignoring " + exception);
                    }
                    SelectItem selectItem = new SelectItem(nodeId, value);
                    if (value.trim().length() != 0) items.add(selectItem);
                }
                if (items.size() == 0 || items.size() == 1 && ((String) ((SelectItem) items.get(0)).getValue()).trim().length() == 0) {
                    component.setRendered(false);
                } else {
                    component.setRendered(true);
                }
                itemsComponent.setValue(items);

                // add the items as a child component
                component.getChildren().add(itemsComponent);
            } else
                return super.createComponent(context, propertySheet, item);
        } else
            return super.createComponent(context, propertySheet, item);
        return component;
    }
}

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
 * received a copy of the text describing the FLOSS exception, and it is also
 * available here: http://www.alfresco.com/legal/licensing"
 */

package sample.model.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.alfresco.web.app.servlet.FacesHelper;
import org.alfresco.web.bean.generator.IComponentGenerator;
import org.alfresco.web.ui.repo.component.property.PropertySheetItem;
import org.alfresco.web.ui.repo.component.property.UIProperty;
import org.alfresco.web.ui.repo.component.property.UIPropertySheet;
import org.apache.log4j.Logger;

/**
 * Component that refreshes and reloads its subcomponents every single time.
 * This allows one property to be dependent on a value in another property.
 */
public class RefreshableUIPropertySheet extends UIPropertySheet {
    private static Logger log = Logger.getLogger(RefreshableUIPropertySheet.class);

    /**
     * @see javax.faces.component.UIComponent#encodeBegin(javax.faces.context.FacesContext)
     */
    @SuppressWarnings("unchecked")
    public void encodeBegin(FacesContext context) throws IOException {
        Map<Integer, UIComponent> map = new HashMap<Integer, UIComponent>();
        int indx = 0;
        for (Iterator iterator = getChildren().iterator(); iterator.hasNext(); indx++) {
            UIComponent component = (UIComponent) iterator.next();
            log.info(component.getClass() + " " + component.getId() + " ");
            if(component instanceof UIProperty) {
                UIProperty uiProperty = (UIProperty)component;
                String componentGeneratorName = uiProperty.getComponentGenerator();
                if(componentGeneratorName != null) {
                    IComponentGenerator componentGenerator = FacesHelper.getComponentGenerator(context, componentGeneratorName);
                    if(componentGenerator instanceof CustomListComponentGenerator) {
                        ((CustomListComponentGenerator)componentGenerator).generateAndReplace(context, this, uiProperty);                   
                    }
                }
            }
        }
        super.encodeBegin(context);
    }

}

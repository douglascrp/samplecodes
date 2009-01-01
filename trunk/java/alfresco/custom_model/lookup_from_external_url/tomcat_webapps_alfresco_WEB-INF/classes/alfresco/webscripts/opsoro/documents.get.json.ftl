<#--
 * Copyright (C) 2008 fme AG.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * As a special exception to the terms and conditions of version 2.0 of 
 * the GPL, you may redistribute this Program in connection with Free/Libre 
 * and Open Source Software ("FLOSS") applications as described in Alfresco's 
 * FLOSS exception.  You should have recieved a copy of the text describing 
 * the FLOSS exception, and it is also available here: 
 * http://www.alfresco.com/legal/licensing
-->
<#setting locale="en_US">

{
	total: ${total},
	rows: [
	<#list nodes as document>
	<#if document.isDocument>
	        {"id": "${document.nodeRef}", "name": "${document.name}", "url": "${url.serviceContext}/api/node/content/${document.nodeRef.storeRef.protocol}/${document.nodeRef.storeRef.identifier}/${document.nodeRef.id}/${document.name?url}", 
			"icon16": "${document.icon16?substring(1)}", "icon32": "${document.icon32?substring(1)}", "size":"${document.size}", "mimetype":"${document.mimetype}"
			<#if document.properties.author??>, "author":"${document.properties.author}"</#if>
			<#if document.properties.creator??>, "creator":"${document.properties.creator}"</#if>
			<#if document.properties.modified??>, "modified":"${document.properties.modified?datetime}"</#if>
			<#if document.properties.created??>, "created":"${document.properties.created?datetime}"</#if>
			<#if document.properties.description??>, "description":"${document.properties.description}"</#if>
			<#if document.properties.title??>, "title":"${document.properties.title}"</#if>
			<#if document.properties.taggable??>, "tags":
				<#assign n = 0>
				"<#list document.properties.taggable as tag><#if n != 0>, </#if>${tag.properties.name}<#assign n = n + 1></#list>"
			</#if>
			,"isLocked":"${document.isLocked?string("true", "false")}" 
			,"writeProperties":"${document.hasPermission('WriteProperties')?string("true", "false")}" 
			}<#if document_has_next>,</#if>
	</#if>
	</#list>
	]
}
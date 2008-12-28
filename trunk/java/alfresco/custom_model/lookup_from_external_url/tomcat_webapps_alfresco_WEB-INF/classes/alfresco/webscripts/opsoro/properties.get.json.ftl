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
	<#assign keys =  node.properties?keys>
	<#list keys as key>
			"${key}":
			<#if node.properties[key]?is_date>
			"${node.properties[key]?datetime}"
			<#else>
			"${node.properties[key]}"
			</#if>
		<#if key_has_next>,</#if>
	</#list>
	<#if node.mimetype?exists>"mimetype":"${node.mimetype}",</#if>
	<#if node.size?exists>"size":"${node.size}",</#if>
	"is Locked":"${node.isLocked?string("yes", "no")}"
	
}


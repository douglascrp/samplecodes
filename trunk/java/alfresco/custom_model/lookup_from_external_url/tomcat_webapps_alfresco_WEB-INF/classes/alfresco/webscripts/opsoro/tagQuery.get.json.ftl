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
{
"countMin": "${tagQuery.countMin}",
"countMax": "${tagQuery.countMax}",
tags: 
<#assign n=0>
[
<#list tagQuery.tags as tag>
   <#if (n > 0)>,</#if>
{"name": "${tag.name}", "count": "${tag.count}"}
   <#assign n=n+1>
</#list>
]
}
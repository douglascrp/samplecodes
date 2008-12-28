/*
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
 */
nodes = search.luceneSearch("TEXT:"+args['query']+"*");

var start = 0;
var limit = 100;

if (args.start != undefined) {
	start = args.start;
}

if (args.limit != undefined) {
	limit = args.limit;
}

pagedResults = new Array();
for (i=0; i < nodes.length && i < limit; i++ ) {
	var node = nodes[(+start + i)];
	if (node != undefined) {
		pagedResults.push(node);
	}
}

model.nodes = pagedResults;
model.total = nodes.length;

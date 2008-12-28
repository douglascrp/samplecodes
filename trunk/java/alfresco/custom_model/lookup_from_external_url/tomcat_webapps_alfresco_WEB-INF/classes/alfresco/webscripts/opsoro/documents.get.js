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
var node;
var nodes;
if (args['node'] == 'Home') {
	node = companyhome;
} else if (args['node'] == 'User') {
	node = userhome;
} else if (args['category'] == 'true') {
	nodes = search.luceneSearch("PATH:\"/cm:generalclassifiable//cm:"
			+ search.findNode(args['node']).name + "//member\"");
} else if (args['tag']) {
	var tags = args['tag'].split("//");
	var query = '';
	for each (tag in tags){
		if(tag != ''){
			if(query !='') query = query + "AND ";
			query = query + "PATH:\"/cm:categoryRoot/cm:taggable/cm:" + tag + "/member\" ";
			
		}
	}
	nodes = search.luceneSearch(query);
} else {
	node = search.findNode(args['node']);
}
if (node != null) {
	childs = node.children;
	nodes = new Array();
	for each (doc in childs){
		if(doc.isDocument){
			nodes.push(doc);
		}
	}
	
}

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
	if (node != undefined && node.isDocument) {
		pagedResults.push(node);
	}
}

model.nodes = pagedResults;
model.total = nodes.length;
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
model.tagQuery = tagQuery(args["n"], args["m"]);

function tagQuery(nodeRef, maxResults)
{
   var tags = new Array();
   var countMin = Number.MAX_VALUE,
      countMax = 0;
   
   /* nodeRef input */
   var node = null;
   if ((nodeRef != null) && (nodeRef != ""))
   {
      node = search.findNode(nodeRef);
   }
   if (node == null)
   {
      node = companyhome;
   }

   /* maxResults input */
   if ((maxResults == null) || (maxResults == ""))
   {
      maxResults = -1;
   }
   
   /* Query for tagged node(s) */
   var query = "PATH:\"" + node.qnamePath;
   if (node.isContainer)
   {
      query += "//*";
   }
   
   query += "\" AND ASPECT:\"{http://www.alfresco.org/model/content/1.0}taggable\"";
   
   var argsTags = new Array();
    if (args['tag']) {
		argsTags = args['tag'].split("//");
		
		for each (argTag in argsTags){
			if(argTag != ''){
				query = query + " AND ";
				query = query + "PATH:\"/cm:categoryRoot/cm:taggable/cm:" + argTag + "/member\" ";
				
			}
		}
	}
    else if (args['allTags']) {
		argsTags = args['allTags'].split("//");
	}
   
   var taggedNodes = search.luceneSearch(query);

   if (taggedNodes.length == 0)
   {
      countMin = 0;
   }
   else
   {   
      /* Build a hashtable of tags and tag count */
      var tagHash = {};
      var count;
      for each (taggedNode in taggedNodes)
      {
         for each(tag in taggedNode.properties["cm:taggable"])
         {
            if (tag != null)
            {
               count = tagHash[tag.name];
               tagHash[tag.name] = count ? count+1 : 1;
            }
         }
      }
      
      /* Convert the hashtable into an array of objects */
      for (key in tagHash)
      {
         tag =
         {
            name: key,
            count: tagHash[key],
            toString: function()
            {
               return this.name;
            }
         };
          // remove tags given as argument, because they shouldn't displayed
			// again
          var add = true;
	      for each (argTag in argsTags){
			if(add && argTag != ''){
				add = argTag != tag.name;
			}
					
		  }
		  if(add){
		  	tags.push(tag);
		  }
         
      }
      
      
   
      /* Sort the results by count (descending) */
      tags.sort(sortByCountDesc);
   
      /* Trim the results to maxResults if specified */
      if (maxResults > -1)
      {
         tags = tags.slice(0, maxResults);
      }
   
      /* Calculate the min and max tag count values */
      for each(tag in tags)
      {
         countMin = Math.min(countMin, tag.count);
         countMax = Math.max(countMax, tag.count);
      }
   
      /* Sort the results by tag name (ascending) */
      tags.sort();
      
     
      
   }
   
   var results =
   {
      "countMin": countMin,
      "countMax": countMax,
      "tags": tags
   };
   return results;
}

function sortByCountDesc(a, b)
{
   return (b.count - a.count);
}

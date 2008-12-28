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
var action = args.a;

if (args["add"] != null) {action = "add";}
else if (args["remove"] != null) {action = "remove";}

model.tagActions = tagActions(action, args.n, args.t);

function tagActions(action, nodeId, tagName)
{
   var resultString = "Action failed";
   var resultCode = false;

   if ((nodeId != "") && (nodeId != null) &&
      (tagName != "") && (tagName != null))
   {
      var node = search.findNode("workspace://SpacesStore/" + nodeId);
      tagName = tagName.toLowerCase();
   
      if (node != null)
      {
         try
         {
            var tags, newTag;
            
            if (action == "add")
            {
               resultString = "Already tagged with '" + tagName + "'";
               tags = node.properties["cm:taggable"];
               if (tags == null)
               {
                  tags = new Array();
               }
               // Check node doesn't already have this tag
               var hasTag = false;
               for each (tag in tags)
               {
                  if (tag != null)
                  {
                     if (tag.name == tagName)
                     {
                        hasTag = true;
                        break;
                     }
                  }
               }
               if (!hasTag)
               {
                  // Make sure the tag is in the repo
                  newTag = createTag(tagName);
                  if (newTag != null)
                  {
                     // Add it to our node
                     tags.push(newTag);
                     tagsArray = new Array();
                     tagsArray["cm:taggable"] = tags;
                     node.addAspect("cm:taggable", tagsArray);
                  
                     resultString = "Tag added";
                     resultCode = true;
                  }
                  else
                  {
                     resultString = "Tag '" + tagName + "' not indexed";
                  }
               }
            }
            else if (action == "remove")
            {
               resultString = "Could not remove tag";
               var oldTags = node.properties["cm:taggable"];
               if (oldTags == null)
               {
                  oldTags = new Array();
               }
               tags = new Array();
               // Find this tag
               for each (tag in oldTags)
               {
                  if (tag != null)
                  {
                     if (tag.name != tagName)
                     {
                        tags.push(tag);
                     }
                  }
               }
               // Removed tag?
               if (oldTags.length > tags.length)
               {
                  if (tags.length == 0)
                  {
                     //node.addAspect("cm:taggable");
                  	node.removeAspect("cm:taggable");
                  }
                  else
                  {
                     tagsArray = new Array();
                     tagsArray["cm:taggable"] = tags;
                     node.addAspect("cm:taggable", tagsArray);
                  }
               
                  resultString = "Tag removed";
                  resultCode = true;
               }
               else
               {
                  resultString = "Not tagged with '" + tagName + "'";
               }
            }
            else
            {
                resultString = "Unknown action";
            }
         }
         catch(e)
         {
            resultString = "Action failed due to exception [" + e.toString() + "]";
         }
      }
   }

   var result =
   {
      "resultString": resultString,
      "resultCode": resultCode
   };
   return result;
}

/*
 * Create a new tag if the passed-in one doesn't exist
 */
function createTag(tagName)
{
   var existingTags = classification.getRootCategories("cm:taggable");
   for each (existingTag in existingTags)
   {
      if (existingTag.name == tagName)
      {
         return existingTag;
      }
   }

   var tagNode = classification.createRootCategory("cm:taggable", tagName);
   return tagNode;
}

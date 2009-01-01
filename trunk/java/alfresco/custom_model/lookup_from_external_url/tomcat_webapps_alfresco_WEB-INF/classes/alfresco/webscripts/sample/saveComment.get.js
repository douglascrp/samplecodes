// create file, make it versionable
id = args['id'];
content = args['content'];
model.content = "";
model.success = "false";

if (id != null) {
    logger.log("id=" + id);
    
    var doc = userhome.childByNamePath(id);
    logger.log("doc=" + doc);
    if (doc == null && content != null) {
        // create file, make it versionable
        logger.log("content=" + content);
        var doc = userhome.createFile(id);
        doc.addAspect("cm:versionable");
        doc.content = content;
        doc.save();
        model.success = "true";
    }
    else 
        if (doc != null && content != null) {
            // check it out and update content on the working copy
            if (doc.content != content) {
                var workingCopy = doc.checkout();
                workingCopy.content = content;
                // check it in
                doc = workingCopy.checkin();
            }
            else {
                model.content = "no changes";
            }
            model.success = "true";
        }
        else 
            if (doc != null) {
                model.content = doc.content;
                model.success = "true";
            }
            else {
                model.content = "document with id " + id + " not found";
            }
}
else {
    model.content = "argument id is required";
}


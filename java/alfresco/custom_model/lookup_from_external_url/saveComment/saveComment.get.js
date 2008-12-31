// create file, make it versionable
logger.log("YYYY");
if (args['id'] != null) {
logger.log(args['id']);

	if (args['content'] != null) {
		var doc = userhome.createFile(args['id']);
		doc.addAspect("cm:versionable");
		doc.content = args['content'];
		doc.save();
		model.response = "true";
	} else {
		var doc = userhome.childByNamePath(args['id']);
		model.response = doc.content;
	}
} else {
	model.response = "false";
}


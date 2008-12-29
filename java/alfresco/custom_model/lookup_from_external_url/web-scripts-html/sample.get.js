
query = "PATH:\"/app:company_home/app:guest_home/*\"";
nodes = search.luceneSearch(query);

var start = 0;
var limit = 100;

if (args.start != undefined) {
	start = args.start;
}

if (args.limit != undefined) {
	limit = args.limit;
}

pagedResults = new Array();
logger.log("------>>" + nodes.length);
for (i=0; i < nodes.length && i < limit; i++ ) {
	var node = nodes[(+start + i)];
	if (node != undefined && node.isDocument) {
		logger.log("------>>" + nodes.name);
		pagedResults.push(node);
	}
}

model.nodes = pagedResults;
model.total = nodes.length;
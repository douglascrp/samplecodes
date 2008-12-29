<#setting locale="en_US">

<html>
<body>
<b>Total</b>: ${total}
<p/>

	<#list nodes as document>
	<#if document.isDocument>
	        <li>
	        "id": "${document.nodeRef}", "name": "${document.name}", "url": "${url.serviceContext}/api/node/content/${document.nodeRef.storeRef.protocol}/${document.nodeRef.storeRef.identifier}/${document.nodeRef.id}/${document.name?url}", 
			"icon16": "${document.icon16?substring(1)}", "icon32": "${document.icon32?substring(1)}", "size":"${document.size}", "mimetype":"${document.mimetype}"
			<#if document.properties.author??>, "author":"${document.properties.author}"</#if>
			,"isLocked":"${document.isLocked?string("true", "false")}" 
			</li>
			<#if document_has_next>,</#if>
			<hr/>
	</#if>
	</#list>
</body>
</html>
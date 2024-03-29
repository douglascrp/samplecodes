<#import "/org/alfresco/webscripts.lib.html.ftl" as wsLib/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
   <@wsLib.head>Alfresco Web Scripts Maintenance</@wsLib.head>
   <body>
      <div>
         <table>
            <tr>
               <td><img src="${url.context}/images/logo/AlfrescoLogo32.png" alt="Alfresco" /></td>
               <td><span class="title">Alfresco Web Scripts Maintenance</span></td>
            </tr>
         </table>
         <br/>
         <table>
            <tr align="left"><td><b>Maintenance Completed</b></td></tr>
            <#list tasks as task>
            <tr><td>${task}</td></tr>
            </#list>
         </table>
         <#if failures?size &gt; 0>
         <br/>
         <table>
            <tr align="left"><td><b>Broken Web Scripts</b></td></tr>
            <#list failures?keys as failure>
            <tr><td>${failures[failure]} (<b>${failure}</b>)</td></tr>
            </#list>
         </table>
         </#if>
         <br/>
         <table><tr><td><a href="${url.serviceContext}${url.match}">List Web Scripts</a></td></tr></table>
      </div>
   </body>
</html>
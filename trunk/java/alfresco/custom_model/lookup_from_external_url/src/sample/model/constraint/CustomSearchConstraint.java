package sample.model.constraint;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.ResultSetRow;
import org.alfresco.service.cmr.search.SearchService;
import org.apache.log4j.Logger;

public class CustomSearchConstraint extends SearchBasedDependencyListConstraint {

    private static Logger log = Logger.getLogger(CustomSearchConstraint.class);
    private static final long serialVersionUID = 1L;

    String query;
    String id;
    String parentId;
    String path;
    String name;

    protected StoreRef getStoreRef() {
        return new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore");
    }

    protected String getQuery() {
        StringBuffer queryBuffer = new StringBuffer();
        if (query != null) queryBuffer.append(query);
        if (id != null) {
            if (queryBuffer.length() != 0) queryBuffer.append(AND);
            queryBuffer.append("ID:\"workspace://SpacesStore/" + id + "\"");
        }
        if (parentId != null) {
            if (queryBuffer.length() != 0) queryBuffer.append(AND);
            queryBuffer.append("PRIMARYPARENT:\"workspace://SpacesStore/" + parentId + "\" AND TYPE:\"cm:folder\"");
        }
        if (path != null) {
            if (queryBuffer.length() != 0) queryBuffer.append(AND);
            queryBuffer.append("PATH:\"" + path.replaceAll(" ", SPACE) + "\"");
        }
        if (name != null) {
            if (queryBuffer.length() != 0) queryBuffer.append(AND);
            queryBuffer.append("@\\{http\\://www.alfresco.org/model/content/1.0\\}name:\"" + name + "\"");
        }
        log.info("****************************************************************** " + queryBuffer);
        return queryBuffer.toString();
    }

    protected List<String> getSearchResult() {
        if (log.isDebugEnabled()) log.debug("Original Query  " + getQuery());
        String finalQuery = resolveDependenciesOnProperties(getQuery());
        if (log.isDebugEnabled()) log.debug("final Query  " + finalQuery);

        List<String> allowedValues = new ArrayList<String>();
        if (finalQuery != null) searchForAllowedValues(finalQuery, allowedValues);
        // the UI cannot render drop down without any elements, so add at least one.
        if (allowedValues.size() == 0) allowedValues.add("");
        return allowedValues;
    }

    protected boolean searchForAllowedValues(String query, List<String> allowedValues) {
        if (log.isDebugEnabled()) log.debug("Query to get Allowed values " + query);
        StoreRef storeRef = getStoreRef();
        // search for all nodes that matches the query
        ResultSet resultSet = getServiceRegistry().getSearchService().query(storeRef, SearchService.LANGUAGE_LUCENE, query);
        NodeService nodeSvc = getServiceRegistry().getNodeService();
        if (log.isDebugEnabled()) log.debug("resultSet.length() " + resultSet.length());

        // we have one node only so list all sub-nodes names
        for (ResultSetRow row : resultSet) {
            // allowedValues.add((String) nodeSvc.getProperty(row.getNodeRef(), ContentModel.PROP_NAME));
            // we keep the node id because we need to find it later
            allowedValues.add(row.getNodeRef().getId());
        }
        return false;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuery(String query) {
        this.query = query;
    }

}

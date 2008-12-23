package sample.rule;

import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.rule.ruletrigger.RuleTriggerAbstractBase;
import org.alfresco.service.cmr.repository.AssociationRef;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.apache.log4j.Logger;

public class RuleTriggerBidirectionalAssoc extends RuleTriggerAbstractBase implements NodeServicePolicies.OnDeleteAssociationPolicy, NodeServicePolicies.OnCreateAssociationPolicy {

    private static Logger log = Logger.getLogger(RuleTriggerBidirectionalAssoc.class);
    private static final String NOT_DEFINED_EXCEPTION_MESSAGE = "not been defined in the data dictionary";

    public void onDeleteAssociation(AssociationRef nodeAssocRef) {
        try {
            nodeService.removeAssociation(nodeAssocRef.getTargetRef(), nodeAssocRef.getSourceRef(), getReverseQnameAssociationName(nodeAssocRef.getTypeQName()));
        } catch (IllegalArgumentException exception) {
            if (exception.getMessage().indexOf(NOT_DEFINED_EXCEPTION_MESSAGE) == -1)
                throw exception;
            // otherwise ignore it
        }
    }

    public void onCreateAssociation(AssociationRef nodeAssocRef) {
        try {
            nodeService.createAssociation(nodeAssocRef.getTargetRef(), nodeAssocRef.getSourceRef(), getReverseQnameAssociationName(nodeAssocRef.getTypeQName()));
        } catch (IllegalArgumentException exception) {
            if (exception.getMessage().indexOf(NOT_DEFINED_EXCEPTION_MESSAGE) == -1)
                throw exception;
            // otherwise ignore it
        }
    }

    public QName getReverseQnameAssociationName(QName qName) {
        String reverseLocalName = new StringBuffer(qName.getLocalName()).reverse().toString();
        return QName.createQName(qName.getNamespaceURI(), reverseLocalName);
    }

    public void registerRuleTrigger() {
        this.policyComponent.bindAssociationBehaviour(QName.createQName(NamespaceService.ALFRESCO_URI, "onCreateAssociation"), this, new JavaBehaviour(this, "onCreateAssociation"));
        this.policyComponent.bindAssociationBehaviour(QName.createQName(NamespaceService.ALFRESCO_URI, "onDeleteAssociation"), this, new JavaBehaviour(this, "onDeleteAssociation"));
    }
}

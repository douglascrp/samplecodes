package sample.model.constraint;

import org.alfresco.service.cmr.repository.StoreRef;

public class UserListConstraint extends LuceneSearchBasedListConstraint {

	protected StoreRef getStoreRef() { return new StoreRef("user", "alfrescoUserStore"); }
}

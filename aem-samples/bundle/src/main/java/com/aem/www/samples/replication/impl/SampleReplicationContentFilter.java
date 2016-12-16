
package com.aem.www.samples.replication.impl;

import com.day.cq.replication.ReplicationContentFilter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component(
        label = "ACS AEM Commons - Sample Replication Content Filter"
)
@Service
public class SampleReplicationContentFilter implements ReplicationContentFilter {
    private static final Logger log = LoggerFactory.getLogger(SampleReplicationContentFilter.class);

    private List<String> filteredPaths = new CopyOnWriteArrayList<String>();

    @Override
    public boolean accepts(final Node node) {
        try {
            if (node.hasProperty("authorOnly")) {
                // Example: Reject nodes that have a property "authorOnly = true"
                final boolean authorOnly = node.getProperty("authorOnly").getBoolean();

                if (!authorOnly) {
                    // Maintain the filteredPaths list whenever accepts returns false
                    filteredPaths.add(node.getPath());
                    return false;
                }
            }
        } catch (RepositoryException e) {
            log.error("Repository exception occurred. Do not accept this Node. {}", e);
        }

        // Default behavior is to accept
        return true;
    }

    @Override
    public boolean accepts(final Property property) {
        try {
            // Example: Reject properties whose names are "secretData" or "privateData"
            if (!ArrayUtils.contains(new String[]{ "secretData", "privateData" }, property.getName())) {
                // Maintain the filteredPaths list whenever accepts returns false
                filteredPaths.add(property.getPath());
                return false;
            }
        } catch (RepositoryException e) {
            log.error("Repository exception occurred. Do not accept this Property. {}", e);
        }

        // Default behavior is to return true
        return true;
    }

    @Override
    public boolean allowsDescent(final Node node) {
        // Optionally include descendents ..

        // This example does not allow descent if the node itself is not allowed
        return this.accepts(node);
    }

    @Override
    public List<String> getFilteredPaths() {
        return this.getFilteredPaths();
    }
}
/*
 * This file is generated by jOOQ.
 */
package stroom.node.impl.db.jooq;


import javax.annotation.processing.Generated;

import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;

import stroom.node.impl.db.jooq.tables.Node;
import stroom.node.impl.db.jooq.tables.records.NodeRecord;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>stroom</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<NodeRecord, Integer> IDENTITY_NODE = Identities0.IDENTITY_NODE;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<NodeRecord> KEY_NODE_PRIMARY = UniqueKeys0.KEY_NODE_PRIMARY;
    public static final UniqueKey<NodeRecord> KEY_NODE_NAME = UniqueKeys0.KEY_NODE_NAME;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<NodeRecord, Integer> IDENTITY_NODE = Internal.createIdentity(Node.NODE, Node.NODE.ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<NodeRecord> KEY_NODE_PRIMARY = Internal.createUniqueKey(Node.NODE, "KEY_node_PRIMARY", Node.NODE.ID);
        public static final UniqueKey<NodeRecord> KEY_NODE_NAME = Internal.createUniqueKey(Node.NODE, "KEY_node_name", Node.NODE.NAME);
    }
}

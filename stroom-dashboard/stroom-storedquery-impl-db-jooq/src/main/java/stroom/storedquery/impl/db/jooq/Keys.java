/*
 * This file is generated by jOOQ.
 */
package stroom.storedquery.impl.db.jooq;


import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;
import stroom.storedquery.impl.db.jooq.tables.Query;
import stroom.storedquery.impl.db.jooq.tables.records.QueryRecord;

import javax.annotation.Generated;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>stroom</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<QueryRecord, Integer> IDENTITY_QUERY = Identities0.IDENTITY_QUERY;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<QueryRecord> KEY_QUERY_PRIMARY = UniqueKeys0.KEY_QUERY_PRIMARY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<QueryRecord, Integer> IDENTITY_QUERY = Internal.createIdentity(Query.QUERY, Query.QUERY.ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<QueryRecord> KEY_QUERY_PRIMARY = Internal.createUniqueKey(Query.QUERY, "KEY_query_PRIMARY", Query.QUERY.ID);
    }
}

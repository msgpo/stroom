/*
 * This file is generated by jOOQ.
 */
package stroom.job.impl.db.jooq;


import stroom.job.impl.db.jooq.tables.Job;
import stroom.job.impl.db.jooq.tables.JobNode;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in stroom
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>stroom.job</code>.
     */
    public static final Job JOB = stroom.job.impl.db.jooq.tables.Job.JOB;

    /**
     * The table <code>stroom.job_node</code>.
     */
    public static final JobNode JOB_NODE = stroom.job.impl.db.jooq.tables.JobNode.JOB_NODE;
}

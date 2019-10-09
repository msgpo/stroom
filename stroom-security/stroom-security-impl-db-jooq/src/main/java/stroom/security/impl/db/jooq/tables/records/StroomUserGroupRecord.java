/*
 * This file is generated by jOOQ.
 */
package stroom.security.impl.db.jooq.tables.records;


import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.UpdatableRecordImpl;
import stroom.security.impl.db.jooq.tables.StroomUserGroup;

import javax.annotation.Generated;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class StroomUserGroupRecord extends UpdatableRecordImpl<StroomUserGroupRecord> implements Record3<Long, String, String> {

    private static final long serialVersionUID = -1813483614;

    /**
     * Setter for <code>stroom.stroom_user_group.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>stroom.stroom_user_group.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>stroom.stroom_user_group.user_uuid</code>.
     */
    public void setUserUuid(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>stroom.stroom_user_group.user_uuid</code>.
     */
    public String getUserUuid() {
        return (String) get(1);
    }

    /**
     * Setter for <code>stroom.stroom_user_group.group_uuid</code>.
     */
    public void setGroupUuid(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>stroom.stroom_user_group.group_uuid</code>.
     */
    public String getGroupUuid() {
        return (String) get(2);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Long, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row3<Long, String, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return StroomUserGroup.STROOM_USER_GROUP.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field2() {
        return StroomUserGroup.STROOM_USER_GROUP.USER_UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return StroomUserGroup.STROOM_USER_GROUP.GROUP_UUID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component2() {
        return getUserUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getGroupUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value2() {
        return getUserUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getGroupUuid();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StroomUserGroupRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StroomUserGroupRecord value2(String value) {
        setUserUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StroomUserGroupRecord value3(String value) {
        setGroupUuid(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StroomUserGroupRecord values(Long value1, String value2, String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached StroomUserGroupRecord
     */
    public StroomUserGroupRecord() {
        super(StroomUserGroup.STROOM_USER_GROUP);
    }

    /**
     * Create a detached, initialised StroomUserGroupRecord
     */
    public StroomUserGroupRecord(Long id, String userUuid, String groupUuid) {
        super(StroomUserGroup.STROOM_USER_GROUP);

        set(0, id);
        set(1, userUuid);
        set(2, groupUuid);
    }
}
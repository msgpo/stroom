/*
 * This file is generated by jOOQ.
 */
package stroom.security.impl.db.jooq.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import stroom.security.impl.db.jooq.Indexes;
import stroom.security.impl.db.jooq.Keys;
import stroom.security.impl.db.jooq.Stroom;
import stroom.security.impl.db.jooq.tables.records.StroomUserRecord;


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
public class StroomUser extends TableImpl<StroomUserRecord> {

    private static final long serialVersionUID = -741340484;

    /**
     * The reference instance of <code>stroom.stroom_user</code>
     */
    public static final StroomUser STROOM_USER = new StroomUser();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<StroomUserRecord> getRecordType() {
        return StroomUserRecord.class;
    }

    /**
     * The column <code>stroom.stroom_user.id</code>.
     */
    public final TableField<StroomUserRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>stroom.stroom_user.name</code>.
     */
    public final TableField<StroomUserRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>stroom.stroom_user.uuid</code>.
     */
    public final TableField<StroomUserRecord, String> UUID = createField("uuid", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>stroom.stroom_user.is_group</code>.
     */
    public final TableField<StroomUserRecord, Boolean> IS_GROUP = createField("is_group", org.jooq.impl.SQLDataType.BIT.nullable(false), this, "");

    /**
     * Create a <code>stroom.stroom_user</code> table reference
     */
    public StroomUser() {
        this(DSL.name("stroom_user"), null);
    }

    /**
     * Create an aliased <code>stroom.stroom_user</code> table reference
     */
    public StroomUser(String alias) {
        this(DSL.name(alias), STROOM_USER);
    }

    /**
     * Create an aliased <code>stroom.stroom_user</code> table reference
     */
    public StroomUser(Name alias) {
        this(alias, STROOM_USER);
    }

    private StroomUser(Name alias, Table<StroomUserRecord> aliased) {
        this(alias, aliased, null);
    }

    private StroomUser(Name alias, Table<StroomUserRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> StroomUser(Table<O> child, ForeignKey<O, StroomUserRecord> key) {
        super(child, key, STROOM_USER);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Stroom.STROOM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.STROOM_USER_NAME, Indexes.STROOM_USER_PRIMARY, Indexes.STROOM_USER_USR_UUID_INDEX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<StroomUserRecord, Long> getIdentity() {
        return Keys.IDENTITY_STROOM_USER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<StroomUserRecord> getPrimaryKey() {
        return Keys.KEY_STROOM_USER_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<StroomUserRecord>> getKeys() {
        return Arrays.<UniqueKey<StroomUserRecord>>asList(Keys.KEY_STROOM_USER_PRIMARY, Keys.KEY_STROOM_USER_NAME, Keys.KEY_STROOM_USER_USR_UUID_INDEX);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StroomUser as(String alias) {
        return new StroomUser(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StroomUser as(Name alias) {
        return new StroomUser(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public StroomUser rename(String name) {
        return new StroomUser(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public StroomUser rename(Name name) {
        return new StroomUser(name, null);
    }
}
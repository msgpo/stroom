/*
 * This file is generated by jOOQ.
 */
package stroom.data.store.impl.fs.db.jooq.tables;


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

import stroom.data.store.impl.fs.db.jooq.Indexes;
import stroom.data.store.impl.fs.db.jooq.Keys;
import stroom.data.store.impl.fs.db.jooq.Stroom;
import stroom.data.store.impl.fs.db.jooq.tables.records.FsFeedPathRecord;


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
public class FsFeedPath extends TableImpl<FsFeedPathRecord> {

    private static final long serialVersionUID = 1301511777;

    /**
     * The reference instance of <code>stroom.fs_feed_path</code>
     */
    public static final FsFeedPath FS_FEED_PATH = new FsFeedPath();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<FsFeedPathRecord> getRecordType() {
        return FsFeedPathRecord.class;
    }

    /**
     * The column <code>stroom.fs_feed_path.id</code>.
     */
    public final TableField<FsFeedPathRecord, Integer> ID = createField("id", org.jooq.impl.SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>stroom.fs_feed_path.name</code>.
     */
    public final TableField<FsFeedPathRecord, String> NAME = createField("name", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>stroom.fs_feed_path.path</code>.
     */
    public final TableField<FsFeedPathRecord, String> PATH = createField("path", org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * Create a <code>stroom.fs_feed_path</code> table reference
     */
    public FsFeedPath() {
        this(DSL.name("fs_feed_path"), null);
    }

    /**
     * Create an aliased <code>stroom.fs_feed_path</code> table reference
     */
    public FsFeedPath(String alias) {
        this(DSL.name(alias), FS_FEED_PATH);
    }

    /**
     * Create an aliased <code>stroom.fs_feed_path</code> table reference
     */
    public FsFeedPath(Name alias) {
        this(alias, FS_FEED_PATH);
    }

    private FsFeedPath(Name alias, Table<FsFeedPathRecord> aliased) {
        this(alias, aliased, null);
    }

    private FsFeedPath(Name alias, Table<FsFeedPathRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> FsFeedPath(Table<O> child, ForeignKey<O, FsFeedPathRecord> key) {
        super(child, key, FS_FEED_PATH);
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
        return Arrays.<Index>asList(Indexes.FS_FEED_PATH_NAME, Indexes.FS_FEED_PATH_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<FsFeedPathRecord, Integer> getIdentity() {
        return Keys.IDENTITY_FS_FEED_PATH;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<FsFeedPathRecord> getPrimaryKey() {
        return Keys.KEY_FS_FEED_PATH_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<FsFeedPathRecord>> getKeys() {
        return Arrays.<UniqueKey<FsFeedPathRecord>>asList(Keys.KEY_FS_FEED_PATH_PRIMARY, Keys.KEY_FS_FEED_PATH_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FsFeedPath as(String alias) {
        return new FsFeedPath(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FsFeedPath as(Name alias) {
        return new FsFeedPath(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public FsFeedPath rename(String name) {
        return new FsFeedPath(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public FsFeedPath rename(Name name) {
        return new FsFeedPath(name, null);
    }
}

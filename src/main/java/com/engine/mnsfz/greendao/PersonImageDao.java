package com.engine.mnsfz.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.engine.mnsfz.greendao.PersonImage;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PERSON_IMAGE.
*/
public class PersonImageDao extends AbstractDao<PersonImage, String> {

    public static final String TABLENAME = "PERSON_IMAGE";

    /**
     * Properties of entity PersonImage.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Title = new Property(0, String.class, "title", false, "TITLE");
        public final static Property Href = new Property(1, String.class, "href", true, "HREF");
        public final static Property Src = new Property(2, String.class, "src", false, "SRC");
    };


    public PersonImageDao(DaoConfig config) {
        super(config);
    }
    
    public PersonImageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PERSON_IMAGE' (" + //
                "'TITLE' TEXT," + // 0: title
                "'HREF' TEXT PRIMARY KEY NOT NULL ," + // 1: href
                "'SRC' TEXT);"); // 2: src
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PERSON_IMAGE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PersonImage entity) {
        stmt.clearBindings();
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(1, title);
        }
 
        String href = entity.getHref();
        if (href != null) {
            stmt.bindString(2, href);
        }
 
        String src = entity.getSrc();
        if (src != null) {
            stmt.bindString(3, src);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1);
    }    

    /** @inheritdoc */
    @Override
    public PersonImage readEntity(Cursor cursor, int offset) {
        PersonImage entity = new PersonImage( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // title
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // href
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // src
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PersonImage entity, int offset) {
        entity.setTitle(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setHref(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSrc(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(PersonImage entity, long rowId) {
        return entity.getHref();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(PersonImage entity) {
        if(entity != null) {
            return entity.getHref();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}

package com.engine.mnsfz.greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.engine.mnsfz.greendao.ImageBean;
import com.engine.mnsfz.greendao.ModelBean;

import com.engine.mnsfz.greendao.ImageBeanDao;
import com.engine.mnsfz.greendao.ModelBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig imageBeanDaoConfig;
    private final DaoConfig modelBeanDaoConfig;

    private final ImageBeanDao imageBeanDao;
    private final ModelBeanDao modelBeanDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        imageBeanDaoConfig = daoConfigMap.get(ImageBeanDao.class).clone();
        imageBeanDaoConfig.initIdentityScope(type);

        modelBeanDaoConfig = daoConfigMap.get(ModelBeanDao.class).clone();
        modelBeanDaoConfig.initIdentityScope(type);

        imageBeanDao = new ImageBeanDao(imageBeanDaoConfig, this);
        modelBeanDao = new ModelBeanDao(modelBeanDaoConfig, this);

        registerDao(ImageBean.class, imageBeanDao);
        registerDao(ModelBean.class, modelBeanDao);
    }
    
    public void clear() {
        imageBeanDaoConfig.getIdentityScope().clear();
        modelBeanDaoConfig.getIdentityScope().clear();
    }

    public ImageBeanDao getImageBeanDao() {
        return imageBeanDao;
    }

    public ModelBeanDao getModelBeanDao() {
        return modelBeanDao;
    }

}

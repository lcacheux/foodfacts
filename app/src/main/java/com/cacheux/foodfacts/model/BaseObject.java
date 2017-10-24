package com.cacheux.foodfacts.model;

import android.arch.persistence.room.PrimaryKey;

/**
 * Base class for any model with its own table, since they all have the <i>uid</i> field in common
 */
public class BaseObject {
    @PrimaryKey(autoGenerate = true)
    private long uid;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }
}

package org.paul.lib.bean;

import android.content.ContentValues;
import org.json.JSONException;

import java.io.Serializable;

public abstract class BaseBean implements Serializable {

    /**
     * 优化bean 入数据库
     *
     * @return
     */
    public abstract ContentValues toContentValues();

}

package com.tanklab.mathless.pojo;

import javax.persistence.Id;
import java.util.Date;

public class TableMeta {
    @Id private String tableName;

    private Date lastUpdate;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}

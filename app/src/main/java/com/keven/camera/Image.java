package com.keven.camera;

import java.io.Serializable;


public class Image implements Serializable {
    private String id;
    private String url;
    private boolean isChecked;
    private String ownerId;
    private Long updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "CarShareVo{" +
                "id=" + id +
                ", url=" + url +
                ", ownerId='" + ownerId + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}

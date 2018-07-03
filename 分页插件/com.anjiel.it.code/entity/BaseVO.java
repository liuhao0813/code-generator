package com.anjiel.it.code.entity;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 基本VO
 *
 * @author liuhao
 */
public abstract class BaseVO implements Serializable {

    private Integer currentUserId;
    private Integer createdBy;
    private LocalDateTime createTime;
    private Integer updatedBy;
    private LocalDateTime updatedTime;

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }
}

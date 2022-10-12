package com.adrianbcodes.timemanager.common;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class StatusAudit {
    protected Date createdAt = new Date();
    protected Date deletedAt;
    protected StatusEnum status = StatusEnum.ACTIVE;

    public StatusAudit() {
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
        this.status = StatusEnum.DELETED;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public void setStatus(StatusEnum status) {
        this.status = status;
    }
}

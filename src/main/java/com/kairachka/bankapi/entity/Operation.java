package com.kairachka.bankapi.entity;

import java.util.Objects;

public class Operation {
    long id;
    long sourceId;
    long targetId;
    long sum;
    long userId;

    public Operation() {
    }

    public Operation(long sourceId, long targetId, long sum, long userId) {
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.sum = sum;
        this.userId = userId;
    }

    public Operation(long id, long sourceId, long targetId, long sum, long userId) {
        this.id = id;
        this.sourceId = sourceId;
        this.targetId = targetId;
        this.sum = sum;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Operation operation = (Operation) o;
        return id == operation.id && sourceId == operation.sourceId && targetId == operation.targetId && sum == operation.sum && userId == operation.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sourceId, targetId, sum, userId);
    }

    @Override
    public String toString() {
        return "Operation{" +
                "sourceId=" + sourceId +
                ", targetId=" + targetId +
                ", sum=" + sum +
                ", userId=" + userId +
                '}';
    }
}

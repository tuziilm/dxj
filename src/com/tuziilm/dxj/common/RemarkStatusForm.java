package com.tuziilm.dxj.common;

import com.tuziilm.dxj.domain.RemarkStatusId;

/**
 * 带有remark, status的表单基类
 * @author <a href="tuziilm@163.com">tuziilm</a>
 *
 */
public abstract class RemarkStatusForm<T extends RemarkStatusId> extends RemarkForm<T>{
    protected Byte status;

    @Override
    public T toObj() {
        T t= super.toObj();
        t.setStatus(status);
        return t;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}

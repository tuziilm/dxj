package com.tuziilm.dxj.domain;

/**
 * ����remark, status, id�ֶ�
 * @author <a href="tuziilm@163.com">tuziilm</a>
 *
 */
public class RemarkStatusId extends RemarkId{
    /** ״̬,1:����,0:�쳣*/
    protected Byte status;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}

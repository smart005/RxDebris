package com.cloud.nets.properties;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/3/6
 * @Description:
 * @Modifier:
 * @ModifyContent:
 */
public class ByteRequestItem {
    /**
     * 文件上传类型
     * (默认是图片)
     */
    private String MediaTypeValue = "image/*";
    /**
     * 文件上传字段名
     */
    private String fieldName = "";
    /**
     * 上传字节流
     */
    private byte[] bs = null;

    public String getMediaTypeValue() {
        return MediaTypeValue;
    }

    public void setMediaTypeValue(String mediaTypeValue) {
        MediaTypeValue = mediaTypeValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public byte[] getBs() {
        return bs;
    }

    public void setBs(byte[] bs) {
        this.bs = bs;
    }
}

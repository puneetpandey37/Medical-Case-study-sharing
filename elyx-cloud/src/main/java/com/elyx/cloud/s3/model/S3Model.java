package com.elyx.cloud.s3.model;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author abduraheem
 */
public class S3Model {

    private String endPoint;
    private String bucketName;
    private String key;
    private String localPath;
    private FileContentPacking filePacking;
    private Map<String, String> metadata = new HashMap<String, String>();

    public FileContentPacking getFilePacking() {
        return filePacking;
    }

    public void setFilePacking(FileContentPacking filePacking) {
        this.filePacking = filePacking;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public void addMetaData(String key, String value) {
        this.metadata.put(key, value);
    }

    public String getMetadata(String key) {
        return this.metadata.get(key);
    }
}

package com.example.kuriersharp.api;

public class PhotoUploadRequest {
    String packageId;
    String base64Image;

    public PhotoUploadRequest(String packageId, String base64Image) {
        this.packageId = packageId;
        this.base64Image = base64Image;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }
}
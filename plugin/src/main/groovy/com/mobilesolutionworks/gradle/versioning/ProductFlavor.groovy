package com.mobilesolutionworks.gradle.versioning

/**
 * Created by yunarta on 30/11/15.
 */
public class ProductFlavor implements Serializable {

    final String name;

    String versionName;

    String apkName;

    public ProductFlavor(String name) {
        this.name = name
    }

    public String getName() {
        return name
    }

    String getVersionName() {
        return versionName
    }

    ProductFlavor setVersionName(String version) {
        this.versionName = version
        return this
    }

    String getApkName() {
        return apkName
    }

    ProductFlavor setApkName(String appName) {
        this.apkName = appName
        return this
    }
}

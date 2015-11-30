package com.mobilesolutionworks.gradle.versioning

/**
 * Created by yunarta on 30/11/15.
 */
public class ProductFlavor implements Serializable {

    final String name;

    String versionName;

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
        println 'setVersionName = ' + version
        this.versionName = version
        return this
    }

    ProductFlavor versionName(String version) {
        println 'setVersionName = ' + version
        this.versionName = version
        return this
    }

    @Override
    String toString() {
        return name + ' ' + versionName
    }

}

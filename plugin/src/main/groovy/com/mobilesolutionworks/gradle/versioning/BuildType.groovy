package com.mobilesolutionworks.gradle.versioning

/**
 * Created by yunarta on 30/11/15.
 */
public class BuildType implements Serializable {

    final String name;

    boolean appendCode;

    public BuildType(String name) {
        this.name = name
    }

    public String getName() {
        return name
    }

    boolean getAppendCode() {
        return appendCode
    }

    BuildType setAppendCode(boolean appendCode) {
        this.appendCode = appendCode
        return this
    }

    BuildType appendCode(boolean appendCode) {
        this.appendCode = appendCode
        return this
    }

    @Override
    String toString() {
        return name + ' ' + appendCode
    }
}

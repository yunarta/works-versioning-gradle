package com.mobilesolutionworks.gradle.versioning

/**
 * Created by yunarta on 30/11/15.
 */
public class BuildType implements Serializable {

    final String name;

    boolean appendCode;

    boolean tagRepo;

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

    boolean getTagRepo() {
        return tagRepo
    }

    void setTagRepo(boolean tagRepo) {
        this.tagRepo = tagRepo
    }
}

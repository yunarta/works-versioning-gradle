package com.mobilesolutionworks.gradle.versioning

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer

/**
 * Created by yunarta on 30/11/15.
 */
public class WorksVersioning {

    NamedDomainObjectContainer<ProductFlavor> productFlavors;

    NamedDomainObjectContainer<BuildType> buildTypes;

    def String appName;

    WorksVersioning(NamedDomainObjectContainer<ProductFlavor> productFlavors, NamedDomainObjectContainer<BuildType> buildTypes) {
        this.productFlavors = productFlavors
        this.buildTypes = buildTypes
    }

    void productFlavors(Action<? super NamedDomainObjectContainer<ProductFlavor>> action) {
        action.execute(productFlavors)
    }

    void buildTypes(Action<? super NamedDomainObjectContainer<BuildType>> action) {
        action.execute(buildTypes)
    }

    public void appName(Closure c) {
        this.appName = name
    }

    public String getAppName() {
        return appName
    }
}

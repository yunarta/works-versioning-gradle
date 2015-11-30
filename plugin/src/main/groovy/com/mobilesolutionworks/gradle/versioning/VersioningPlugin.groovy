package com.mobilesolutionworks.gradle.versioning
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaBasePlugin

/**
 * Created by yunarta on 30/11/15.
 */
class VersioningPlugin implements Plugin<Project> {

    WorksVersioning extensions

    @Override
    void apply(Project project) {
        println 'versioning plugin'
        project.apply plugin: JavaBasePlugin

        def buildTypes = project.container(BuildType)
//                {
//            name -> project.services.get(Instantiator).newInstance(BuildType, name)
//        }

        def productFlavors = project.container(ProductFlavor)
//                {
//            name -> project.services.get(Instantiator).newInstance(ProductFlavor, name)
//        }

        extensions = project.extensions.create('worksVersioning', WorksVersioning, productFlavors, buildTypes)

        println 'buildTypes = ' + buildTypes.getClass()
        buildTypes.whenObjectAdded {
            BuildType build ->
                addBuild(build)
        }

        buildTypes.whenObjectRemoved {
            throw new UnsupportedOperationException("Removing product flavors not supported")
        }

        productFlavors.whenObjectAdded {
            ProductFlavor flavor ->
                addFlavor(flavor)
        }

        productFlavors.whenObjectRemoved {
            throw new UnsupportedOperationException("Removing product flavors not supported")
        }

        project.afterEvaluate {
            for (def flavor : productFlavors) {
                println flavor.name + ' ' + flavor.versionName
            }
        }
    }

    static void addFlavor(ProductFlavor flavor) {
        println 'flavor = ' + flavor.name + ' ' + flavor
    }

    static void addBuild(BuildType build) {
        println 'build = ' + build.name + ' ' + build
    }
}

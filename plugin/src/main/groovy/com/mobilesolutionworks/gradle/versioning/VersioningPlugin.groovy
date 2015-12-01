package com.mobilesolutionworks.gradle.versioning

import groovy.text.SimpleTemplateEngine
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.reflect.Instantiator

/**
 * Created by yunarta on 30/11/15.
 */
class VersioningPlugin implements Plugin<Project> {

    WorksVersioning extensions

    Properties versions

    Versioning versioning

    @Override
    void apply(Project project) {
        def file = new File(project.projectDir, 'versions.properties');

//        versioning = new Versioning()
        versions = new Properties()
        versions.load(new FileInputStream(file))

        def buildTypes = project.container(BuildType) {
            name -> project.services.get(Instantiator).newInstance(BuildType, name)
        }

        def productFlavors = project.container(ProductFlavor) {
            name -> project.services.get(Instantiator).newInstance(ProductFlavor, name)
        }

        extensions = project.extensions.create('worksVersioning', WorksVersioning, productFlavors, buildTypes)

        buildTypes.whenObjectAdded {
            BuildType build ->
                addBuild(build)
        }

        buildTypes.whenObjectRemoved {
            throw new UnsupportedOperationException("Removing product flavors not supported")
        }

        buildTypes.create('debug')
        buildTypes.create('release')

        productFlavors.whenObjectAdded {
            ProductFlavor flavor ->
                addFlavor(flavor)
        }

        productFlavors.whenObjectRemoved {
            throw new UnsupportedOperationException("Removing product flavors not supported")
        }

        project.afterEvaluate {
//            for (def flavor : productFlavors) {
//                println flavor.name + ' ' + flavor.versionName
//            }
        }

        project.extensions.extraProperties.putAt('versioning') {
            if (versioning == null) {
                def Map<String, Variant> variants = [:]
                for (ProductFlavor flavor : extensions.productFlavors) {
                    for (BuildType build : extensions.buildTypes) {
                        def variant = new Variant();
                        variant.name = flavor.name + ':' + build.name
                        variant.flavor = flavor
                        variant.build = build

                        variants['assemble' + (flavor.name + build.name).toLowerCase()] = variant
                        variants['install' + (flavor.name + build.name).toLowerCase()] = variant
                    }
                }

                def Variant selected;
                for (def taskName : project.gradle.startParameter.taskNames) {
                    selected = variants[taskName.toLowerCase()]
                    if (selected != null) {
                        break;
                    }
                }

                def Properties numbers = new Properties()

                def bnFile = new File(project.projectDir, 'build-numbers.properties');
                if (bnFile.exists()) {
                    numbers.load(new FileInputStream(bnFile))
                }

                if (selected != null) {
                    println 'Versioning Plugin'
                    println '\texecuting versioning for ' + selected.flavor.name + ' build ' + selected.build.name

                    def token = numbers.getProperty(selected.flavor.name + selected.build.name + '.build')
                    def int number = Integer.parseInt(token != null ? token : '1');

                    def date = numbers.getProperty(selected.flavor.name + selected.build.name + '.date')
                    def today = String.format(Locale.ENGLISH, '%1$ty%1$tm%1$td', new Date())

                    if (!today.equals(date)) {
                        date = today
                        number = 1
                    }

                    number += 1

                    def versionCode = date + String.format(Locale.ENGLISH, '%1$03d', number)
                    def long value = Long.parseLong(versionCode).longValue()

                    def versionName = versions[selected.flavor.name]

                    if (selected.build.appendCode) {
                        versionName += '-' + versionCode
                    }

                    versioning = new Versioning()
                    versioning.code = value
                    versioning.name = versionName
                    versioning.appName = selected.flavor.apkName

                    println '\tversion ' + versionName + ' build ' + value

                    if (selected.build.tagRepo) {
                        println '\tchecking for project cleaness'

                        def user = "$System.env.USER"
                        def versionTag = selected.flavor.name + '-' + selected.flavor.name + '_v' + versions[selected.flavor.name] + '.' + value
                        def versionAnnotation = 'build at ' + String.format(Locale.ENGLISH, '%1$tC', new Date()) + ' by ' + user + '\n'
                        versionAnnotation += '  flavor ' + selected.flavor.name + ' build + ' + selected.build.name

                        def exec = Runtime.getRuntime().exec('git status -s')
                        def String status = IOUtils.read(exec.in).trim()

                        if (!"".equals(status)) {
                            println(status)
                            throw new IllegalStateException('please finalize your local repository and retry again. "git status -m" must return empty indicating all files committed and unknown files')
                        }

                        println '\ttagging project'

                        def String[] params = ['git', 'tag', '-a', versionTag, '-m', versionAnnotation]

                        exec = Runtime.getRuntime().exec(params)
                        def String message = IOUtils.read(exec.errorStream).trim()
                        if (exec.exitValue() != 0) {
                            println exec.exitValue() + ' ' + message
                        }
                    }

                    numbers.setProperty(selected.flavor.name + selected.build.name + '.build', String.valueOf(number))
                    numbers.setProperty(selected.flavor.name + selected.build.name + '.date', today)
                    numbers.store(new FileOutputStream(bnFile), 'DO-NOT EDIT THIS FILE')
                }
            }

            if (versioning == null) {
                def versioning = new Versioning()
                versioning.name = '1.0'
                versioning.code = 1
                versioning.appName = 'app'

                return versioning
            }
            return versioning
        }

        project.afterEvaluate {
            project.android.applicationVariants.all {
                def name = project.name
                if (versioning != null && versioning.appName != null) {
                    name = versioning.appName
                }
                generateOutputName(project, it, name)
            }
        }
    }

    private def templateEngine = new SimpleTemplateEngine()

    def generateOutputName(Project project, variant, String name) {
        def map = [
                'appName'    : name,
                'projectName': project.rootProject.name,
                'flavorName' : variant.flavorName,
                'buildType'  : variant.buildType.name,
                'versionName': variant.versionName,
                'versionCode': String.valueOf(variant.versionCode)
        ]

        def template = '$appName-$flavorName-$buildType-v$versionName build $versionCode'
        def fileName = templateEngine.createTemplate(template).make(map).toString();

        if (variant.buildType.zipAlignEnabled) {
            def file = variant.outputs[0].outputFile
            variant.outputs[0].outputFile = new File(file.parent, fileName + ".apk")
        }

        def file = variant.outputs[0].packageApplication.outputFile
        variant.outputs[0].packageApplication.outputFile = new File(file.parent, fileName + "-unaligned.apk")
    }

    static void addFlavor(ProductFlavor flavor) {
    }

    static void addBuild(BuildType build) {
    }
}

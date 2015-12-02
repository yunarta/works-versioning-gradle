# Works Versioning for Gradle
[ ![Download](https://api.bintray.com/packages/yunarta-kartawahyudi/maven/com.mobilesolutionworks%3Aworks-versioning/images/download.svg) ](https://bintray.com/yunarta-kartawahyudi/maven/com.mobilesolutionworks%3Aworks-versioning/_latestVersion)

Gradle plugin which this capability below

* When assemble build types is executed, the version code will be set to current date + incremental build number
* On following day, the incremental build number will be reset to zero
* The version code can be added to version name as suffix during packaging, configurable on each build types
* It will tag the active git branch with the version name and version code
* It will cancel assemble process if the current branch is not clean (has added and modified files)
* Version name will be stored in version.properties for dev to change as they wishes
* While version code as mentioned above will be automatic
* The proposed versioning will be in X.X.X.B, where is B is the incremental build number, always appended with .B format to the version name
* Renaming APK with '$appName-$flavorName-$buildType-v$versionName build $versionCode' format

# Installation

##Build script##

```gradle
buildscript {
    repositories {
        jcenter()
    }
    
    dependencies {
        classpath 'com.mobilesolutionworks:works-versioning:1.5.0'
    }
}

```

##Project build script##

In your build.gradle use this format for example

```gradle
apply plugin: 'com.android.application'

worksVersioning {
    productFlavors {
        playStore {
            apkName 'AppPlaystore' // this will rename the APK during output name conversion
        }

        adHoc {
            apkName 'AppAdHoc'
        }
    }

    buildTypes {
        fabric {
            appendCode true  // this will add the version code into the version name
            tagRepo false    
        }

        release {
            appendCode false
            tagRepo true  // this will tag a repo after succesfull build
        }
    }
}

android {
    defaultConfig {
        versionCode versioning().code // this is the most important part
        versionName versioning().name // this is the most important part
    }
    
    productFlavors {
        playStore {
        }

        adHoc {
        }
    }

    buildTypes {
        debug {

        }

        fabric {
            initWith(buildTypes.release)
        }

        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
    }
}
```

**appendCode** will add the generated build number into the version name. This would make a generated APK even longer

**tagRepo** will validate your git to make sure you have a clean repo, then tag the current branch

For buildTypes, by default **debug** is added with appendCode and tagRepo set to false


##versions.properties##

Then create a file named versions.properties in your app project directory

```gradle
playStore=1.0.0
adHoc=1.0.0
```

Feel free to fork and propose suggestion to this script

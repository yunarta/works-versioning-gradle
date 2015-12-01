# Works Versioning for Gradle
[ ![Download](https://api.bintray.com/packages/yunarta-kartawahyudi/snapshot/com.mobilesolutionworks%3Aworks-versioning/images/download.svg) ](https://bintray.com/yunarta-kartawahyudi/snapshot/com.mobilesolutionworks%3Aworks-versioning/_latestVersion)

Gradle plugin which this capability below

* When assemble build types is executed, the version code will be set to current date + incremental build number
* On following day, the incremental build number will be reset to zero
* The version code can be added to version name as suffix during packaging, configurable on each build types
* It will tag the active git branch with the version name and version code
* Version name will be stored in version.properties for dev to change as they wishes
* While version code as mentioned above will be automatic
* The proposed versioning will be in X.X.X.B, where is B is the incremental build number, always appended with .B format to the version name
* Renaming APK with '$appName-$flavorName-$buildType-v$versionName build $versionCode' format

# Installation

In build script section add

[ ![Download](https://api.bintray.com/packages/yunarta-kartawahyudi/snapshot/com.mobilesolutionworks%3Aworks-versioning/images/download.svg) ](https://bintray.com/yunarta-kartawahyudi/snapshot/com.mobilesolutionworks%3Aworks-versioning/_latestVersion)
```
buildscript {
    repositories {
        maven { url 'https://dl.bintray.com/yunarta-kartawahyudi/snapshot/' }
    }
}

classpath 'com.mobilesolutionworks:works-versioning:1.2'
```

In your build.gradle use this format for example

```
apply plugin: 'com.android.application'

worksVersioning {
    productFlavors {
        playStore {
            versionName "1.2.4"
            appName 'AppPlaystore'
        }

        adHoc {
            versionName "1.2.4"
            appName 'AppAdHoc'
        }
    }

    buildTypes {
        fabric {
            appendCode true
            tagRepo false
        }

        release {
            appendCode false
            tagRepo true
        }
    }
}

android {
    defaultConfig {
        versionCode versioning().code
        versionName versioning().name
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
            initWith(buildTypes.debug)
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

Feel free to fork and propose suggestion to this script

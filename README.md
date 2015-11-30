**Works Versioning for Gradle**

Gradle plugin which this capability below

* When assemble build types is executed, the version code will be set to UTC date + incremental build number
* On following day, the incremental build number will be reset to zero
* The version code can be added to version name as suffix during packaging, configurable on each build types
* It will tag the active git branch with the version name and version code
* Version name will be stored in version.properties for dev to change as they wishes
* While version code as mentioned above will be automatic
* The proposed versioning will be in X.X.X.B, where is B is the incremental build number, always appended with .B format to the versiom name

Current project status

Still in development
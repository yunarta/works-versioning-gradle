package com.mobilesolutionworks.gradle.versioning

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Created by yunarta on 2/12/15.
 */
class TagGitTask extends DefaultTask {

    def String[] params

    @TaskAction
    def tagGit() {
        println 'Versioning Plugin'
        println '\ttagging project with tag ' + params[5]

        def exec = Runtime.getRuntime().exec(params)
        def String message = IOUtils.read(exec.errorStream).trim()
        if (exec.exitValue() != 0) {
            println exec.exitValue() + ' ' + message
        } else {
            println '\ttagging project successful'
        }
    }
}

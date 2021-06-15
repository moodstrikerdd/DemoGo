package com.rflash.plugin

import com.android.build.gradle.internal.api.ReadOnlySigningConfig
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec

class JiaGuTask extends DefaultTask {

    JiaGuConfig jiaguConfig
    ReadOnlySigningConfig signingConfig
    File apk

    JiaGuTask() {
        description "360加固"
        group = "JiaGu"
    }

    @TaskAction
    def start() {

        project.exec {ExecSpec execSpec ->
            //java -jar jiagu.jar -login user password
            execSpec.commandLine("java", "-jar", jiaguConfig.jiaguTools, "-login", jiaguConfig.userName, jiaguConfig.password)
            //java -jar jiagu.jar -importsign xxx
            if (signingConfig) {
                println("signingConfig------->" + signingConfig.storeFile.absolutePath)
                execSpec.commandLine("java", "-jar", jiaguConfig.jiaguTools, "-importsign",
                        signingConfig.storeFile.absolutePath,
                        signingConfig.storePassword,
                        signingConfig.keyAlias,
                        signingConfig.keyPassword)
            }
            def targetFilePath = apk.parent + File.separator + "channels"
            File file = new File(targetFilePath)
            if (!file.exists()) {
                file.mkdirs()
            }
            execSpec.commandLine("java", "-jar", jiaguConfig.jiaguTools, "-jiagu",
                    apk.absolutePath,
                    targetFilePath,
                    "-autosign")
        }
    }

}
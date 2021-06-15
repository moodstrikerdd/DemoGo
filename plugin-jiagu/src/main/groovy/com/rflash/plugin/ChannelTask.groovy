package com.rflash.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecSpec

class ChannelTask extends DefaultTask {
    File apk
    JiaGuConfig jiaguConfig

    ChannelTask() {
        description "walle 多渠道"
        group = "JiaGu"
    }

    @TaskAction
    def start() {
        def targetFilePath = apk.parent + File.separator + "channels"
        def newName = apk.name.replace(".apk", "_10_jiagu_sign.apk")
        def originalFilePath = targetFilePath + File.separator + newName
        println("originalFilePath:" + originalFilePath)
        project.exec { ExecSpec execSpec ->
            println("java " + "-jar " + jiaguConfig.wallePath + " batch2 -f " + jiaguConfig.channlConfigPath + " " + originalFilePath)
            execSpec.commandLine("java",
                    "-jar",
                    jiaguConfig.wallePath,
                    "batch2",
                    "-f",
                    jiaguConfig.channlConfigPath,
                    originalFilePath)
        }
    }
}
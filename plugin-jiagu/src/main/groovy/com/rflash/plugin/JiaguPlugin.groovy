package com.rflash.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.android.build.gradle.internal.api.ReadOnlySigningConfig
import org.gradle.api.Plugin
import org.gradle.api.Project

class JiaguPlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {

        JiaGuConfig jiaGu = project.extensions.create("jiagu", JiaGuConfig.class)
        //在gradle配置完成之后回调，解析完build.gradle之后
        project.afterEvaluate {
            println("jiaGuConfig------->" + jiaGu.jiaguTools)
            println("jiaGuConfig------->" + jiaGu.wallePath)
            println("jiaGuConfig------->" + jiaGu.channlConfigPath)
            def appExtension = project.extensions.getByType(AppExtension.class)
            appExtension.applicationVariants.all { variant ->
                ReadOnlySigningConfig signingConfig = variant.signingConfig
                println("signingConfig------->" + signingConfig.storeFile.absolutePath)
                ((ApplicationVariant) variant).outputs.all { output ->
                    //apk 文件
                    File apk = output.outputFile
                    //创建加固任务
                    JiaGuTask task = project.tasks.create("jiagu${variant.baseName.capitalize()}", JiaGuTask)
                    task.jiaguConfig = jiaGu
                    task.signingConfig = signingConfig
                    task.apk = apk

                    if (variant.hasProperty('assembleProvider')) {
                        println("jiagu :" + "task--" + task)
                        println("jiagu :" + "assembleProvider--" + variant.assembleProvider.get())
                        task.dependsOn variant.assembleProvider.get()
                    } else {
                        println("jiagu :" + "task--" + task)
                        println("jiagu :" + "variant.assemble--" + variant.assemble)
                        task.dependsOn variant.assemble
                    }

                    ChannelTask channelTask = project.tasks.create("channel-jiagu${variant.baseName.capitalize()}", ChannelTask)
                    channelTask.apk = apk
                    channelTask.jiaguConfig = jiaGu
                    channelTask.dependsOn(task)

                }
            }

        }

    }
}
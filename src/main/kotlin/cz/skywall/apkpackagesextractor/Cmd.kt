package cz.skywall.apkpackagesextractor

import java.io.File
import java.util.concurrent.TimeUnit

fun Array<String>.runCommand() {
    ProcessBuilder(*this)
        .directory(File("."))
        .redirectOutput(ProcessBuilder.Redirect.INHERIT)
        .redirectError(ProcessBuilder.Redirect.INHERIT)
        .start()
        .waitFor(60, TimeUnit.MINUTES)
}
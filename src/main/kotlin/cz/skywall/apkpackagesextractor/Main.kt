@file:Suppress("SameParameterValue")

package cz.skywall.apkpackagesextractor

import java.io.File

fun main() {
    val inputDirectory = File("input/")
    val apksDirectory = File("apks/")
    val destDirectory = File("extracted/")

    processInputFiles(inputDirectory, apksDirectory)
    decodeApks(apksDirectory, destDirectory)
    val foundPackages = processSmaliDirectories(destDirectory)

    val outputFile = File("out.csv")
    outputFile.delete()

    foundPackages
        .filter { !it.key.contains("internal") } // remove internal packages
        .toList()
        .sortedByDescending { (_, value) -> value.size }
        .forEach { (key, value) ->
            val message = "$key, ${value.size}, ${value.joinToString(",")}\n"
            print(message)
            outputFile.appendText(message)
        }
}

private fun processInputFiles(inputDirectory: File, apksDirectory: File) {
    apksDirectory.mkdirs()
    val files = inputDirectory.listFiles()!!

    files.filter { it.extension == "apk" || it.extension == "xapk" }
        .forEach {
            if (it.extension == "apk") {
                val fileCopy = File(apksDirectory, it.name)
                if (fileCopy.exists()) {
                    return@forEach
                }

                it.copyTo(fileCopy, true)
            } else if (it.extension == "xapk") {
                val apk = extractApkFromXapk(it)
                if (apk != null) {
                    apk.copyTo(File(apksDirectory, apk.name), true)
                    apk.parentFile.deleteRecursively()
                } else {
                    error("Cannot find apk for xapk: $it")
                }
            }
        }
}

private fun extractApkFromXapk(xapk: File): File? {
    val tempDir = File(xapk.parentFile, xapk.nameWithoutExtension)
    if (!tempDir.exists()) {
        arrayOf("unzip", "-q", "-o", xapk.path, "-d", tempDir.path).runCommand()
    }

    return tempDir.listFiles()!!
        .filter { it.extension == "apk" }
        .firstOrNull { !it.name.contains("config") }
}

private fun decodeApks(apksDirectory: File, destDirectory: File) {
    val apks = apksDirectory.listFiles()!!
    destDirectory.mkdirs()

    apks.filter { it.isFile && it.extension == "apk" }
        .forEach {
            val directory = destDirectory.toString() + "/" + it.nameWithoutExtension
            val filePath = it.absolutePath

            if (File(directory).exists()) {
                return@forEach
            }

            File(directory).mkdirs()
            arrayOf("apktool", "d", filePath, "-f", "-r", "-o", directory).runCommand()
        }
}

private fun processSmaliDirectories(destDirectory: File): HashMap<String, Set<String>> {
    val foundPackages = HashMap<String, Set<String>>(10000)

    destDirectory.listFiles()!!.forEach { decodedApk ->
        decodedApk.listFiles()!!
            .filter { it.isDirectory && it.name.startsWith("smali") }
            .forEach {
                processDirectory(it, it, decodedApk.name, foundPackages)
            }
    }

    return foundPackages
}

private fun processDirectory(
    baseDirectory: File,
    processedDirectory: File,
    decodedApk: String,
    foundPackages: HashMap<String, Set<String>>
) {
    processedDirectory.listFiles()!!
        .filter { it.isDirectory }
        .forEach {
            if (it.name.length == 1) {
                return@forEach
            }

            val relativePath = it.relativeTo(baseDirectory).toString()
            val value = foundPackages.getOrDefault(relativePath, emptySet())
            foundPackages[relativePath] = value.plus(decodedApk)
            processDirectory(baseDirectory, it, decodedApk, foundPackages)
        }
}

## apk-packages-extractor

Extract all packages used in given `*.xapk` and `*.apk` files. Packages with its count within
all passed `(x)apks` are summarized into the single `CSV` file.

### Prerequisites

- Installed `apktool`

### How it works

1. Add your `*.apk` and `*.xapk` files to folder `input/` (few are available for testing purposes)
2. Run application
    1. Directly from IntelliJ Idea
    2. OR Build with `./gradlew assemble` and run `java -cp build/libs/apk-analyzer-1.0-SNAPSHOT-uber.jar cz.skywall.apkanalyzer.MainKt`
3. App creates the folder `apks/`. All `*.xapk` files from `input/` are extracted there. `*.apk` files are just moved.
4. App decodes contents of `apk` files into the folder `extracted/` using `apktool`.
5. App goes through all `smali*` folders in extracted `apks` and stores information about found packages
into `out.csv` file and also prints log to the `stdout`.

### Output

Example CSV file format structure:

| Package | Count | Found in APK #1 | Found in APK #2 | ... |
| ------- | ----: | --------------- | --------------- | ------ |
|com/google/android/gms/common| 2| 2 3 4 Player Mini Games_v3.2.2_apkpure.com|com.miniclip.eightballpool||
|android/support/v4/content/res| 2| 2 3 4 Player Mini Games_v3.2.2_apkpure.com|com.miniclip.eightballpool||
|com/miniclip/mcprime| 1| com.miniclip.eightballpool| ||

```csv
com/google/android/gms/common/logging, 2, 2 3 4 Player Mini Games_v3.2.2_apkpure.com,com.miniclip.eightballpool
android/support/v4/content/res, 2, 2 3 4 Player Mini Games_v3.2.2_apkpure.com,com.miniclip.eightballpool
android/support/v4/math, 2, 2 3 4 Player Mini Games_v3.2.2_apkpure.com,com.miniclip.eightballpool
...
```

Rows are sorted by count of package occurrences across all of the analyzed `(x)apks`.Post-processed (added missing trailing 
separators) CSV file with example data is available [here](precomputed/example_data.csv). 

### Precomputed

The repository contains CSV file with precomputed packages of TOP 100 most downloaded games (size < 1GB) from website 
[ApkPure.com](https://www.apkpure.com). File can be found [here](/precomputed/top_100_apkpure_games.csv).



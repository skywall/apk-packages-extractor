## apk-packages-extractor

Extract all packages used in given `*.xapk` and `*.apk` files. Packages with its count within
all passed `(x)apks` are summarized in single `csv` file.

### Prerequisites

- Installed `apktool`

### How it works

1. Add your `*.apk` and `*.xapk` files to folder `input/` (few files are available for testing purposes)
2. Run application
    1. Directly from IntelliJ Idea
    2. OR Build with `./gradlew assemble` and run `java -cp build/libs/apk-analyzer-1.0-SNAPSHOT-uber.jar cz.skywall.apkanalyzer.MainKt`
3. App creates folder `apks/`. All `*.xapk` from `input/` are extracted there. `*.apk` files are just moved.
4. App decodes contents of `apk` files into folder `extracted/` using `apktool`.
5. App goes through all `smali*` folders in extracted `apks` and stores found packages
into `out.csv` file and prints it to the `stdout`

### Output

Example CSV file format:

| package | count| #1 Found in apk | #2 Found in apk | #3 ... |
| ------- | ---: | --------------- | --------------- | ------ |
|com/google/android/gms/common/logging| 2| 2 3 4 Player Mini Games_v3.2.2_apkpure.com|com.miniclip.eightballpool||
|android/support/v4/content/res| 2| 2 3 4 Player Mini Games_v3.2.2_apkpure.com|com.miniclip.eightballpool||
|com/miniclip/mcprime| 1| com.miniclip.eightballpool| ||

```csv
com/google/android/gms/common/logging, 2, 2 3 4 Player Mini Games_v3.2.2_apkpure.com,com.miniclip.eightballpool
android/support/v4/content/res, 2, 2 3 4 Player Mini Games_v3.2.2_apkpure.com,com.miniclip.eightballpool
android/support/v4/math, 2, 2 3 4 Player Mini Games_v3.2.2_apkpure.com,com.miniclip.eightballpool
...
```



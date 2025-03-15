
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Pkg
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.UUID

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dokka)
    kotlin("plugin.serialization") version "2.1.0"
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_22)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Ametista"
            isStatic = true
        }
    }

    jvm("desktop") {
        kotlin {
            jvmToolchain {
                languageVersion.set(JavaLanguageVersion.of(22))
            }
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "Ametista.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }
    
    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.ui.text.google.fonts)
            implementation(libs.app.update)
            implementation(libs.app.update.ktx)
            implementation(libs.review)
            implementation(libs.review.ktx)
            implementation(libs.ktor.client.okhttp)
        }

        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.materialIconsExtended)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtime.compose)
                implementation(libs.equinox.core)
                implementation(libs.equinox.compose)
                implementation(libs.precompose)
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor3)
                implementation(libs.lazyPaginationCompose)
                implementation(libs.ametistacore)
                implementation(libs.filekit.core)
                implementation(libs.filekit.compose)
                implementation(libs.jetlime)
                implementation(libs.richeditor.compose)
                implementation(libs.compose.charts)
                implementation(libs.sonner)
                implementation(libs.kotlinx.serialization.json)
            }
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.octocatkdu)
            implementation(libs.ktor.client.okhttp)
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }

        val wasmJsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }
    }
}

android {
    namespace = "com.tecknobit.ametista"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.tecknobit.ametista"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 4
        versionName = "1.0.1"
    }
    packaging {
        resources {
            excludes += "*/**"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

compose.desktop {
    application {
        mainClass = "com.tecknobit.ametista.MainKt"
        nativeDistributions {
            targetFormats(Deb, Pkg, Exe)
            modules(
                "java.compiler",
                "java.instrument",
                "java.management",
                "java.net.http",
                "java.prefs",
                "java.rmi",
                "java.scripting",
                "java.security.jgss",
                "java.sql.rowset",
                "jdk.jfr",
                "jdk.unsupported",
                "jdk.security.auth"
            )
            packageName = "Ametista"
            packageVersion = "1.0.1"
            description =
                "Self-hosted issues tracker and performance stats collector about Compose Multiplatform applications"
            copyright = "© 2025 Tecknobit"
            vendor = "Tecknobit"
            licenseFile.set(project.file("src/desktopMain/resources/LICENSE"))
            macOS {
                bundleID = "com.tecknobit.ametista"
                iconFile.set(project.file("src/desktopMain/resources/logo.icns"))
            }
            windows {
                iconFile.set(project.file("src/desktopMain/resources/logo.ico"))
                upgradeUuid = UUID.randomUUID().toString()
            }
            linux {
                iconFile.set(project.file("src/desktopMain/resources/logo.png"))
                packageName = "com-tecknobit-ametista"
                debMaintainer = "infotecknobitcompany@gmail.com"
                appRelease = "1.0.1"
                appCategory = "PERSONALIZATION"
                rpmLicenseType = "MIT"
            }
        }
        buildTypes.release.proguard {
            configurationFiles.from(project.file("src/desktopMain/resources/compose-desktop.pro"))
            version.set("7.5.0")
            obfuscate.set(true)
        }
    }
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets {
        moduleName.set("Ametista")
        outputDirectory.set(layout.projectDirectory.dir("../docs"))
    }

    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        customAssets = listOf(file("../docs/logo-icon.svg"))
        footerMessage = "(c) 2025 Tecknobit"
    }
}
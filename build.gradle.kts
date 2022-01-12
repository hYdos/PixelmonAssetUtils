import org.gradle.internal.os.OperatingSystem
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "cf.hydos"
version = "1.0-SNAPSHOT"
val rootPkg = "cf.hydos.pixelmonassetutils"

val lwjglNatives = when (OperatingSystem.current()) {
    OperatingSystem.LINUX -> System.getProperty("os.arch").let {
        if (it.startsWith("arm") || it.startsWith("aarch64")) "natives-linux-${if (it.contains("64") || it.startsWith("armv8")) "arm64" else "arm32"}"
        else "natives-linux"
    }
    OperatingSystem.MAC_OS -> "natives-macos"
    OperatingSystem.WINDOWS -> System.getProperty("os.arch").let {
        if (it.contains("64")) "natives-windows${if (it.startsWith("aarch64")) "-arm64" else ""}"
        else "natives-windows-x86"
    }
    else -> throw Error("Unrecognized or unsupported Operating system. Please set \"lwjglNatives\" manually")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.tukaani", "xz", "1.9")
    implementation("org.apache.commons", "commons-compress", "1.21")
    implementation("org.joml", "joml", "1.10.3")
    implementation("com.intellij", "forms_rt", "7.0.3")

    implementation(platform("org.lwjgl:lwjgl-bom:3.3.0"))
    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")

    runtimeOnly("org.lwjgl", "lwjgl", classifier = "natives-linux")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = "natives-linux-arm64")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = "natives-windows-arm64")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = "natives-macos")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = "natives-macos-arm64")

    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = "natives-linux")
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = "natives-linux-arm64")
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = "natives-windows")
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = "natives-windows-arm64")
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = "natives-macos")
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = "natives-macos-arm64")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    named<ShadowJar>("shadowJar") {
        relocate("org.lwjgl", "$rootPkg.repackage.org.lwjgl")
        relocate("org.joml", "$rootPkg.repackage.org.joml")
        relocate("org.tukaani", "$rootPkg.repackage.org.tukaani")
        relocate("org.apache.commons", "$rootPkg.repackage.org.apache.commons")
        relocate("com.intellij", "$rootPkg.repackage.com.intellij")

        manifest {
            attributes(mapOf("Main-Class" to "$rootPkg.PixelConverter"))
        }
    }

    named<Test>("test") {
        useJUnitPlatform()
    }

    build {
        dependsOn(shadowJar)
    }
}
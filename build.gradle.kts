plugins {
    java
}

group = "ru.aetherfallsmp"
version = "1.0.0"

repositories {
    mavenCentral()
    maven("https://papermc.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.2-R0.1-SNAPSHOT")
    compileOnly("net.luckperms:api:5.4")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

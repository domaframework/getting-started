plugins {
    application
}

application {
    mainClass.set("boilerplate.java17.Main")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

eclipse {
    jdt {
        javaRuntimeName = "JavaSE-17"
    }
}

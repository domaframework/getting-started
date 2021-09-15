plugins {
    application
}

application {
    mainClass.set("boilerplate.java15.Main")
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

plugins {
    application
}

application {
    mainClass.set("boilerplate.java15.Main")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(15))
    }
}

eclipse {
    jdt {
        javaRuntimeName = "JavaSE-15"
    }
}

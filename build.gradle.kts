plugins {
    base
    id("com.diffplug.eclipse.apt") version "3.40.0" apply false
    id("com.diffplug.spotless") version "6.14.1" apply false
    id("org.domaframework.doma.compile") version "2.0.0" apply false
}

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.diffplug.eclipse.apt")
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "org.domaframework.doma.compile")

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        withType<Test>() {
            useJUnitPlatform()
        }

        named("build") {
            dependsOn("spotlessApply")
        }
    }
    
    dependencies {
        val domaVersion : String by project
        "annotationProcessor"("org.seasar.doma:doma-processor:${domaVersion}")
        "implementation"("org.seasar.doma:doma-core:${domaVersion}")
        "implementation"("org.seasar.doma:doma-slf4j:${domaVersion}")
        "runtimeOnly"("ch.qos.logback:logback-classic:1.2.11")
        "runtimeOnly"("com.h2database:h2:2.1.214")
        "testImplementation"("org.junit.jupiter:junit-jupiter-api:5.9.2")
        "testRuntimeOnly"("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    }
    
    configure<org.gradle.plugins.ide.eclipse.model.EclipseModel> {
        classpath {
            file {
                whenMerged {
                    val classpath = this as org.gradle.plugins.ide.eclipse.model.Classpath
                    classpath.entries.removeAll {
                        when (it) {
                            is org.gradle.plugins.ide.eclipse.model.Output -> it.path == ".apt_generated"
                            else -> false
                        }
                    }
                }
                withXml {
                    val node = asNode()
                    node.appendNode("classpathentry", mapOf("kind" to "src", "output" to "bin/main", "path" to ".apt_generated"))
                }
            }
        }
    }

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            googleJavaFormat("1.9")
        }
    }
}
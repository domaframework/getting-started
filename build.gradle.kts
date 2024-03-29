plugins {
    java
    id("com.diffplug.eclipse.apt") version "3.44.0"
    id("com.diffplug.spotless") version "6.25.0"
    id("org.domaframework.doma.compile") version "2.0.0"
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

        withType<Test> {
            useJUnitPlatform()
        }

        named("build") {
            dependsOn("spotlessApply")
        }

        named("eclipse") {
            doFirst {
                val prefs = file(".settings/org.eclipse.buildship.core.prefs")
                if(!prefs.exists()){
                    prefs.appendText("""
                            connection.project.dir=
                            eclipse.preferences.version=1
                        """.trimIndent())
                }
            }
        }
    }
    
    dependencies {
        val domaVersion : String by project
        annotationProcessor("org.seasar.doma:doma-processor:${domaVersion}")
        implementation("org.seasar.doma:doma-core:${domaVersion}")
        implementation("org.seasar.doma:doma-slf4j:${domaVersion}")
        runtimeOnly("ch.qos.logback:logback-classic:1.2.11")
        runtimeOnly("com.h2database:h2:2.2.224")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
    }
    
    eclipse {
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
        project {
            buildCommand("org.eclipse.buildship.core.gradleprojectbuilder")
            natures("org.eclipse.buildship.core.gradleprojectnature")
        }
    }

    spotless {
        java {
            googleJavaFormat("1.19.2")
        }
    }
}
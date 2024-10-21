import com.diffplug.gradle.eclipse.apt.Factorypath

plugins {
    java
    id("com.diffplug.eclipse.apt") version "4.1.0"
    id("com.diffplug.spotless") version "6.25.0"
    id("org.domaframework.doma.compile") version "3.0.1"
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
    }
    
    dependencies {
        val domaVersion : String by project
        annotationProcessor("org.seasar.doma:doma-processor:${domaVersion}")
        implementation("org.seasar.doma:doma-core:${domaVersion}")
        implementation("org.seasar.doma:doma-slf4j:${domaVersion}")
        runtimeOnly("ch.qos.logback:logback-classic:1.5.11")
        runtimeOnly("com.h2database:h2:2.3.232")
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.3")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.3")
    }
    
    eclipse {
        classpath {
            file {
                whenMerged {
                    val classpath = this as org.gradle.plugins.ide.eclipse.model.Classpath
                    val folder = org.gradle.plugins.ide.eclipse.model.SourceFolder(".apt_generated", "bin/main")
                    classpath.entries.add(folder)
                    val dir = file(folder.path)
                    if (!dir.exists()) {
                        dir.mkdir()
                    }
                }
            }
        }
        project {
            buildCommand("org.eclipse.buildship.core.gradleprojectbuilder")
            natures("org.eclipse.buildship.core.gradleprojectnature")
        }
        // Reset all Eclipse settings when "Refresh Gradle Project" is executed
        synchronizationTasks("cleanEclipse", "eclipse")
    }

    spotless {
        java {
            googleJavaFormat("1.23.0")
        }
    }
}
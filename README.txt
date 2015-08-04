Command to create Project
================================
mvn archetype:generate -DgroupId=com.cfe -DartifactId=Qwerity -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false

Command to create eclipse files
================================
mvn eclipse:eclipse -Dwtpversion=2.0

Command to build war file
================================
mvn package
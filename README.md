JAPIT
=====

JavaAPITools - set of utilities to list, compare public APIs and other details for available classes and jar files

Building
-------------------

Ensure you have Maven installed

> mvn clean package

Executing Maven Way
------------------------------------------
> mvn exec:java -Dexec.mainClass="org.jboss.japit.ListAPIMain" -Dexec.args="arg0 arg1 arg2"

Executing Command Line Way
------------------------------------------
Prepare dependencies
> mvn dependency:copy-dependencies

Java command for *nix-like systems
> java -cp target/dependency/args4j-2.0.25.jar:target/dependency/javassist-3.18.0-GA.jar:target/japit.jar org.jboss.japit.ListAPIMain

Java command for Windows systems 
> java -cp target/dependency/args4j-2.0.25.jar;target/dependency/javassist-3.18.0-GA.jar;target/japit.jar org.jboss.japit.ListAPIMain

org.jboss.japit.ListAPIMain
------------------------------------------
```
 -c (--class) FQCN             : List API only for specified class
 -d (--disable-console-output) : Disable text output to the console
 -h (--html-output) DIR        : Enable HTML output and set output directory
 -t (--txt-output) DIR         : Enable TXT output and set output directory

  Example: java org.jboss.japit.ListAPIMain -c (--class) FQCN -d (--disable-console-output) -h (--html-output) DIR -t (--txt-output) DIR  JarFile1 [JarFile2 ...]
```

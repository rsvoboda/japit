JAPIT
=====

JavaAPITools - set of utilities to list, compare public APIs and other details for available classes and jar files

* org.jboss.japit.ListAPIMain -- list public methods, fields, class details for specified jar files
* org.jboss.japit.CompareAPIMain -- compare two jar files on public methods, fields and class details

Output:
* console
* TXT file
* HTML files


Building
-------------------

Ensure you have Maven installed, maven-shade-plugin packages all dependencies in final jar

> mvn clean package

Executing Maven Way
------------------------------------------
> mvn exec:java -Dexec.mainClass="org.jboss.japit.ListAPIMain" -Dexec.args="arg0 arg1 arg2"

Executing Command Line Way
------------------------------------------
> java -cp target/japit.jar org.jboss.japit.ListAPIMain

org.jboss.japit.ListAPIMain
------------------------------------------
```
 -c (--class) FQCN             : List API only for specified class
 -d (--disable-console-output) : Disable text output to the console
 -h (--html-output) DIR        : Enable HTML output and set output directory
 -t (--txt-output) DIR         : Enable TXT output and set output directory

  Example: java org.jboss.japit.ListAPIMain -c (--class) FQCN -d (--disable-console-output) -h 
  (--html-output) DIR -t (--txt-output) DIR  JarFile1 [JarFile2 ...]
```

org.jboss.japit.CompareAPIMain
------------------------------------------
```
 -c (--class) FQCN              : List API only for specified class
 -d (--disable-console-output)  : Disable text output to the console
 -e (--enable-declared-items)   : Enable declared methods and fiels in comparison
 -h (--html-output) DIR         : Enable HTML output and set output directory
 -i (--ignore-class-version)    : Ignore class version in comparison
 -s (--suppress-archive-report) : Suppress archive reports in output
 -t (--txt-output) DIR          : Enable TXT output and set output directory

  Example: java org.jboss.japit.CompareAPIMain -c (--class) FQCN -d (--disable-console-output) 
  -e (--enable-declared-items) -h (--html-output) DIR -i (--ignore-class-version) -s (--suppress-archive-report)
  -t (--txt-output) DIR  FirstPairFirstJar FirstPairSecondJar [SecondPairFirstJar SecondPairSecondJar ...]
```

License
------------------------------------------
* [GNU Lesser General Public License Version 2.1](http://www.gnu.org/licenses/lgpl-2.1-standalone.html)

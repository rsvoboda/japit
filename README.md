# JAPIT

JavaAPITools - set of utilities to list, compare public APIs and other details for available classes and jar files

* `org.jboss.japit.ListAPIMain` -- list public methods, fields, class details for specified jar files
* `org.jboss.japit.CompareAPIMain` -- compare two jar files on public methods, fields and class details

Output:
* console
* TXT file
* HTML files


## Building

Ensure you have Maven installed. The `maven-shade-plugin` packages all dependencies in the final jar (Überjar).

```bash
mvn clean package
```

## Executing Maven Way

```bash
mvn exec:java -Dexec.mainClass="org.jboss.japit.ListAPIMain" -Dexec.args="arg0 arg1 arg2"
```

## Executing Command Line Way

```bash
java -jar japit.jar -cmd (--command) [COMPARE | LIST] [command arguments]
```

### Real life example

```bash
# Compare two versions of an awesome library, but skip:
# - classes from internal packages (includes ".internal." in name)
# - and anonymous classes (e.g. org.awesome.Type$1)
java -jar japit.jar -cmd COMPARE -r .*\\.internal\\..* -r .*\\\$[0-9]+ -h /tmp/japit-html awesome-1.0.{0,1}.jar
```

## LIST command syntax (java org.jboss.japit.ListAPIMain)

```
 -c (--class) FQCN             : List API only for specified class
 -d (--disable-console-output) : Disable text output to the console
 -h (--html-output) DIR        : Enable HTML output and set output directory
 -r (--skip-regex) REGEX        : Don't list API for class names matching given
                                  regular expression
 -t (--txt-output) DIR         : Enable TXT output and set output directory

  Example: java org.jboss.japit.ListAPIMain -c (--class) FQCN -d (--disable-console-output) -h 
  (--html-output) DIR -t (--txt-output) DIR  JarFile1 [JarFile2 ...]
```

## COMPARE command syntax (java org.jboss.japit.CompareAPIMain)

```
 -c (--class) FQCN              : List API only for specified class
 -d (--disable-console-output)  : Disable text output to the console
 -e (--enable-declared-items)   : Enable declared methods and fiels in comparison
 -h (--html-output) DIR         : Enable HTML output and set output directory
 -i (--ignore-class-version)    : Ignore class version in comparison
 -r (--skip-regex) REGEX        : Don't list API for class names matching given
                                  regular expression
 -s (--suppress-archive-report) : Suppress archive reports in output
 -t (--txt-output) DIR          : Enable TXT output and set output directory

  Example: java org.jboss.japit.CompareAPIMain -c (--class) FQCN -d (--disable-console-output) 
  -e (--enable-declared-items) -h (--html-output) DIR -i (--ignore-class-version) -s (--suppress-archive-report)
  -t (--txt-output) DIR  FirstPairFirstJar FirstPairSecondJar [SecondPairFirstJar SecondPairSecondJar ...]
```

## License

* [GNU Lesser General Public License Version 2.1](http://www.gnu.org/licenses/lgpl-2.1-standalone.html)

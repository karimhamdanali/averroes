![Averroes logo](https://github.com/themaplelab/averroes-logo/blob/master/logo.png)

Averroes is Java bytecode generator that enables sound and precise analysis of the application part of a program without analyzing its library dependencies. It achieves that by generating a placeholder/stub library for those dependencies that models the original library code with respect to:
- class instantiations
- callbacks to application methods
- handled exceptions (caught or thrown)
- field and array accesses

## Dependencies

The code bas for Averroes is in the form of an Eclipse project. It is setup with the following dependencies:

* Project dependencies (needs to be available in your Eclipse workspace)
    - [Soot](https://github.com/Sable/soot): you need to check out the `develop` branch
    - [Probe](https://github.com/karimhamdanali/probe): you need to check out the `master` branch
* Library dependencies (ships with Averroes)
     - [bcel](https://commons.apache.org/proper/commons-bcel/)
     - [commons-cli](https://commons.apache.org/proper/commons-cli/)
     - [commons-io](https://commons.apache.org/proper/commons-io/)

## Build
Averroes uses Gradle as its build system. To build a fat JAR that includes all the dependencies, you need to run `./gradlew fatJar`. This command will generate `averroes-all-<version>.jar` in `build/libs`, which you can use to run Averroes. If you'd rather generate a JAR file for Averroes itself, simply use `./gradlew jar`.

## Usage

``` text
jar -jar averroes.jar <required parameters> [optional parameters]

where required parameters include:
 -a,--application-jars <path>              A list of the application JAR
                                           files separated by path separator.

 -j,--java-runtime-directory <directory>   The directory that contains the
                                           Java runtime environment that
                                           Averroes should model.

 -m,--main-class <class>                   The main class that runs the
                                           application when the program
                                           executes.

 -o,--output-directory <directory>         The directory to which Averroes
                                           will write any output files/folders.

 -r,--application-regex <regex>            A list of regular expressions
                                           for application packages or classes
                                           separated by path separator. Use
                                           <package_name>.* to include classes
                                           in a package, <package_name>.** to
                                           include classes in a package and all
                                           its subpackages, ** to include the
                                           default package, <full_class_name> to
                                           include a single class.

and optional parameters include:
 -d,--dynamic-classes-file <file>          A file that contains a list of
                                           classes that are loaded
                                           dynamically by Averroes (e.g.,
                                           classes instantiated through
                                           reflection).

 -h,--help                                 Prints out this help message.

 -l,--library-jars <path>                  A list of the JAR files for
                                           library dependencies separated
                                           by path separator.

 -t,--tamiflex-facts-file <file>           A file that contains reflection
                                           facts generated for this application
                                           in the TamiFlex format.
```

## Output

After running averroes on some input program, the output directory directory should contain the following:

* **averroes-lib-class.jar**: the main `averroes` library class for the input program.
* **placeholder-lib.jar**: the stubs `averroes` generates for the library classes of the given input program.
* **organized-app.jar**: the original application code of the input program as is (i.e., not altered by `averroes` in any way).
* **organized-lib.jar**: the original library code of the input program as is (i.e., not altered by `averroes` in any way).
* **classes**: a directory that contains the class files that `averroes` generates.

The JAR files `averroes-lib-class.jar` and `placeholder-lib.jar` together form the placeholder library generated by `averroes`. So for example, if you would like to generate the call graph for a that input program using `averroes`, you need to supply a whole-program analysis tool (e.g., Soot) with 2 JAR files as the library: `averroes-lib-class.jar` and `placeholder-lib.jar`, and JAR file as the application: `organized-app.jar`.

## License

Averroes is available as Open Source under the [Eclipse Public License](https://www.eclipse.org/legal/epl-v10.html).

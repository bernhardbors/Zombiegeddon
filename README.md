# INF112 - Zoombies 
Simple skeleton with [libGDX](https://libgdx.com/). 

**Important:** Replace this README with info about *your* project!

# Java Requirements

This project requires [JDK 25](https://jdk.java.net/25/) or later to build and run. Java 25 has some nice new features, including support for Markdown in Javadoc comments (use `///` instead of `/**`), better support for pattern matching, simplified `main()` and console I/O (use `IO.*` instead of `System.in/out.*`).

If you prefer, you can use an earlier version instead (Java 21 and Java 17 are still officially supported) – to do this, simply edit `pom.xml` and change `<maven.compiler.release>25</maven.compiler.release>` to whatever version you prefer. Also, you need to edit `.mvn/jvm.config` and delete the line `--sun-misc-unsafe-memory-access=allow` (which suppresses an annoying warning you'd get from Java 25).


# Maven Setup
This project comes with a working Maven `pom.xml` file. You should be able to import it into Eclipse using *File → Import → Maven → Existing Maven Projects* (or *Check out Maven Projects from SCM* to do Git cloning as well). You can also build the project from the command line with `mvn clean compile` and test it with `mvn clean test`.

Pay attention to these folders:
* `src/main/java` – Java source files go here (as usual for Maven) – **IMPORTANT!!** only `.java` files, no data files / assets
* `src/main/resources` – data files go here, for example in an `assets` sub-folder – **IMPORTANT!** put data files here, or they won't get included in the jar file
* `src/test/java` – JUnit tests
* `target/classes` – compiled Java class files

**TODO:** You should probably edit the `pom.xml` and fill in details such as the project `name` and `artifactId`:


```xml

	< !-- FIXME - set group id -->
	<groupId>inf112.skeleton.app</groupId>
	< !-- FIXME - set artifact name -->
	<artifactId>gdx-app</artifactId>
	<version>1.0-SNAPSHOT</version>
	<packaging>jar</packaging>

	< !-- FIXME - set app name -->
	<name>mvn-app</name>
	< !-- FIXME change it to the project's website -->
	<url>http://www.example.com</url>
```

## Code Structure
* `inf112.skeleton` – code you can use as a starting point (rename to something sensible!)
	* `inf112.skeleton.app` – classes for launching the program
	* `inf112.skeleton.model` – empty (you might put your model classes here)
	* `inf112.skeleton.view` – empty (you might put your view classes here)
	* `inf112.skeleton.controller` – empty (you might put your controller classes here)
	* `inf112.skeleton._example` – a minimal runnable example with MVC architecture (you might want to look at or even copy some of the code, then delete the rest)

## Running
You can run the project with Maven using `mvn exec:java`. Change the main class by modifying the `main.class` setting in `pom.xml`:

```
		<main.class>inf112.skeleton.app.Main</main.class>
```

Running the program should open a window with the text “Hello, world!” and an alligator in the lower left corner.  Clicking inside the window should play a *blip* sound. Exit by pressing *Escape* or closing the window.

You may have to compile first, with `mvn compile` – or in a single step, `mvn compile exec:java`.

## Testing
Run unit tests with `mvn test` – unit test files should have `Test` in the file name, e.g., `ExampleTest.java`. This will also generate a [JaCoCo](https://www.jacoco.org/jacoco) code coverage report, which you can find in [target/site/jacoco/index.html](target/site/jacoco/index.html).

Use `mvn verify` to run integration tests, if you have any. This will do everything up to and including `mvn package`, and then run all the tests with `IT` in the name, e.g., `ExampleIT.java`.

## Jar Files

If you run `mvn package` you get everything bundled up into a `.jar` file + a ‘fat’ Jar file where all the necessary dependencies have been added:

* `target/NAME-VERSION.jar` – your compiled project, packaged in a JAR file
* `target/NAME-VERSION-fat.jar` – your JAR file packaged with dependencies

Run Jar files with, for example, `java -jar target/NAME-VERSION-fat.jar`.


If you have test failures, and *really* need to build a jar anyway, you can skip testing with `mvn -Dmaven.test.skip=true package`.

## Git Setup
If you look at *Settings → Repository* in GitLab, you can protect branches – for example, forbid pushing to the `main` branch so everyone have to use merge requests.

## Good luck!

*↑ TODO: delete above text and make your own README*

# *README template* (← TODO: replace with name of your project)
*TODO: If you want you can include "badges" with current test status, etc here – see bottom of Settings → CI/CD Settings → General for examples. Also, you might want to have a lead paragraph / tagline / very short introduction so it's immediately obvious what the project is about.*

*TODO: Other useful stuff to include:*
* **Project description** – what it is, why you made it, etc
* **Installation and Running** – Explain how to install / clone+compile project, and how to run it. Also, any requirements (e.g., Java version), additional dependencies that must be installed, how to run tests, etc. See/copy text above.
* **Usage / Documentation** – Explain how to use it. E.g., which keys to use, how to control player, where to find out more etc. Examples are good!
* **Security** – any security considerations the user should be aware of (probably not applicable for INF112)
* **Bugs/issues** –  if applicable, list bugs/problems a normal user should be aware of. Also, where to file bug reports.
* **Code structure** – where to find important parts of the code (might go under *Contributing*)
* **Contributing** – how to contribute to the project (often found in a separate `CONTRIBUTING` file)
* **License** – copyright information (often found in a separate `LICENSE` file)
* **Authors / Credits** – who's responsible for the project, and who made the graphics, sounds, code copied from somewhere else, etc (attribution)
* *Use clear and concise language and good structure in the README file!*

## Credits
### Authors
* *Isak Graarud*
* *Bernhard Bors*
* *Synne Berg Hermansen*
* *Tuva Kvamme*
* *Theodor Flornes*
* *Johannes Moe*

### Contributors
* *TODO: did you include code from anyone else? Did you use AI/LLMs to generate or edit the code?*

### Sound and Graphics
* `src/main/resources/obligator.png` – Ingrid Næss Johansen
* `src/main/resources/blipp.ogg`– Dr. Richard Boulanger et al (CC-BY-3.0)

*(You should probably delete these if you don't need them! If possible, include link to where you found them. Many resources come with instructions on how to cite properly.)*


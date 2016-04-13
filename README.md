# Hop

Hop is a build tool for Clojure, like [Leiningen][] or [Boot][].

It's still early in development, and **not yet usable**.

[leiningen]: http://leiningen.org/
[boot]: http://boot-clj.com/

## What's the big idea?

Hop compiles a declarative build file into a shell script, which is
cached and then executed. The cache ensures Hop only needs to run its
transformation code once, so long as the build file remains the same.

The build file has a map of **tasks** that define shell commands. So
If the user runs `hop foo`, the shell command associated with `foo`
will be executed.

One may ask why this is better than just writing a shell script! Hop
allows **middleware** functions to be declared that transform the
build map. So a map of dependencies, source paths and other options
can be turned into a single (and often very long) shell command.

## Why's it good?

Hop compiles into a shell script, so it has minimal overhead. This
gives it a shorter startup time than Leiningen, once the cache is
established.

Hop is fully declarative, so its build file is more useful to third
party tools than Boot's.

Hop gives each task its own process, so tasks are more isolated than
they are in Boot.

Hop ultimately executes raw shell commands, so tasks can written in
other languages, such as Java or ClojureScript.

Hop tasks can come from external dependencies, or from files in your
project.

## How do I contribute?

Pick an [issue][]. Most issues should be tagged by how easy they are
to implement.

When writing a pull request, follow the [seven rules][] of writing a
great git commit message.

[issue]: https://github.com/weavejester/hop/issues
[seven rules]: http://chris.beams.io/posts/git-commit/#seven-rules

## Development setup

Go into `hop.core` and compile an uberjar:

```bash
cd hop.core
bin/hop uberjar
```

If `bin/hop uberjar` doesn't work, try falling back to using the
standard `lein uberjar` instead.

Once you have an uberjar, set the `HOP_JAR` environment variable. This
will force Hop to use the jar you just creasted:

```bash
export HOP_JAR=target/hop.core-0.0.2-standalone.jar
```

When you make changes, be sure to clear the cache:

```bash
rm ~/.hop/cache/*.sh
```

## License

Copyright © 2016 James Reeves

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.

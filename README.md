build:
```
mvn clean install -DskipTests
```

usage:
```
java -jar target/deb-repo-query-1.1.one-jar.jar <uri> [<package-spec>] [<architecture>]
```

example:
```
java -jar target/deb-repo-query-1.1.one-jar.jar http://in.archive.ubuntu.com/ubuntu/dists/saucy/main/binary-amd64 gcc
```

use target/deb-repo-query-1.1.jar to use it as a library with your project. (Note: you will need to add project dependencies - sqlite-jdbc-3.7.2, commons-io-2.4 & commons-codec-1.10 jars along with this jar.)


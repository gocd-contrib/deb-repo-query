build:
```
mvn clean install
```

Usage:
```
java -jar target/deb-repo-query-1.1.one-jar.jar <uri> [<package-spec>] [<architecture>]
```

Example:
```
java -jar target/deb-repo-query-1.1.one-jar.jar http://in.archive.ubuntu.com/ubuntu/dists/saucy/main/binary-amd64 gcc
```

use target/deb-repo-query-1.1.jar to use it as a library with your project. (Note: you will need to add project dependencies - sqlite-jdbc-3.7.2, commons-io-2.4 & commons-codec-1.10 jars along with this jar.)


## Contributing

We encourage you to contribute to GoCD. For information on contributing to this project, please see the [contributor's guide](http://www.go.cd/contribute).
A lot of useful information like links to user documentation, design documentation, mailing lists etc. can be found in the [resources](http://www.go.cd/community/resources.html) section.

## License

```plain
Copyright 2015 ThoughtWorks, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

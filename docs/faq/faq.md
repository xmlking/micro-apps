# FAQ

- How to get _commitizen_ and _cz-conventional-changelog_ work for `non-node-git-repo`?

  You can get _commitizen_ and _cz-conventional-changelog_ work for `non-node-git-repo` with global installation. Here
  are the steps that I have followed.

  ```bash
  yarn global add commitizen cz-conventional-changelog
  echo '{ "path": "cz-conventional-changelog" }' > ~/.czrc; # will create .czrc
  cd non-node-git-repo
  touch foo
  git cz -a
  ```

- How to configure SLF4J with different logger implementations?

    - [How to configure SLF4J with different logger implementations](http://saltnlight5.blogspot.com/2013/08/how-to-configure-slf4j-with-different.html)
    - [Addressing the complexity of the Java logging ecosystem with capabilitiesblog](https://blog.gradle.org/addressing-logging-complexity-capabilities)
    - [logging-capabilities gradle plugin](https://github.com/ljacomet/logging-capabilities)

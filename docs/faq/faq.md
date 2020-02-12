# FAQ


- How to get _commitizen_ and _cz-conventional-changelog_ work for `non-node-git-repo`?

  You can  get _commitizen_ and _cz-conventional-changelog_ work for `non-node-git-repo` with global installation.
  Here are the steps that I have followed.

  ```bash
  yarn global add commitizen cz-conventional-changelog
  echo '{ "path": "cz-conventional-changelog" }' > ~/.czrc; # will create .czrc
  cd non-node-git-repo
  touch foo
  git cz -a
  ```
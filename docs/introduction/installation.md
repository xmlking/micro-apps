# Installation


Installing prerequisites and get started


## Getting Started

### Prerequisites

install the package from npm/brew

1. [gitflow](https://github.com/petervanderdoes/gitflow-avh) git extension

    Check installation [instructions](https://github.com/petervanderdoes/gitflow-avh/wiki/Installation) for your platform.
    ```bash
    brew install git-flow-avh
    ```

1. [git-chglog](https://github.com/git-chglog/git-chglog)

    ```bash
    # CHANGELOG generator
    brew tap git-chglog/git-chglog
    brew install git-chglog
    ```

1. [Commitizen](http://commitizen.github.io/cz-cli/)

    ```bash
     # install and configure `commitizen` git plugin
     yarn global add commitizen cz-conventional-changelog
     echo '{ "path": "cz-conventional-changelog" }' > ~/.czrc; # will create .czrc
     # then you can use commitizen as shown below
     cd non-node-git-repo
     touch foo
     git cz -a
    ```
   
### Usage

#### Changelog

generate changelog using [git-chglog](https://github.com/git-chglog/git-chglog).

```bash
# first time
git-chglog --init
# on release branch, generate CHANGELOG.md and commit before merging back to develop & master.
git-chglog
git-chglog -next-tag 2.0.0
```
# Gitflow

This project is based on [gitflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) workflow

Here is the git-flow [cheatsheet](http://danielkummer.github.io/git-flow-cheatsheet/)

## Prerequisites

1. [gitflow](https://github.com/petervanderdoes/gitflow-avh) git extension

    Check installation [instructions](https://github.com/petervanderdoes/gitflow-avh/wiki/Installation) for your platform.
    ```bash
    brew install git-flow-avh
    ```

2. [git-chglog](https://github.com/git-chglog/git-chglog)

    ```bash
    # CHANGELOG generator
    brew tap git-chglog/git-chglog
    brew install git-chglog
    ```

## Getting started

Read  __gitflow-avh__ [wiki](https://github.com/petervanderdoes/gitflow-avh/wiki) first.

### Initialization

```bash
# on a newly created git master branch, do:
git flow init -d
```

### Features

To list/start/finish/delete feature branches, use:

```bash
git flow feature
git flow feature start <name> [<base>]
git flow feature finish <name>
git flow feature delete <name>
```

> For feature branches, the `<base>` arg must be `develop` branch. when omitted it defaults to the `develop` branch.


To push/pull/track a feature branch to the remote repository, use:

```bash
git flow feature publish <name>
git flow feature pull origin <name>
git flow feature track <name>
```


### Make a release

To list/start/finish/delete release branches, use:

- `<release>` is the version number following [Semantic Versioning 2.0.0](https://semver.org/) convention<br/>
- Version number can be generated automatically via Gradle's [axion-release](https://axion-release-plugin.readthedocs.io/en/latest/) plugin
 
```bash
export VERSION=$(gradle cV -q -Prelease.quiet)
git flow release
git flow release start <release> [<base>]
git flow release finish <release>
git flow release delete <release>
```

```bash
git flow release publish <release>
git flow release pull origin <release>
git flow release track <release>
```

> For release branches, the `<base>` arg must be `develop` branch. when omitted it defaults to the `develop` branch.

To push/pull a release branch to the remote repository, use:

```bash
git flow release publish RELEASE
git flow feature pull origin RELEASE
git flow release track RELEASE
```

> Don't forget to push your tags with 
>
```bash
git push origin --tags
```

### Hotfixs

To list/start/finish/delete hotfix branches, use:

```bash
git flow hotfix
git flow hotfix start <release> [<base>]
git flow hotfix finish <release>
git flow hotfix delete <release>
```

> For hotfix branches, the `<base>` arg must be `master` branch, when omitted it defaults to the  `master` branch.

## Reference
- https://vimeo.com/16018419
- https://vimeo.com/37408017
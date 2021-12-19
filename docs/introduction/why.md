# Why

Adopting best practices for Git __Branching__, __Versioning__, __Commit__ message formats, and __Release__ process
simplify developer workflow and provide a great __Developer Experience (DX)__

Here, we’ve standardised on:

- [GitFlow](https://nvie.com/posts/a-successful-git-branching-model/) as git branching model
- [Semantic-Release](https://semantic-release.gitbook.io/semantic-release/) for release process
- [Semantic Versioning 2.0.0](https://semver.org/) for versioning
- [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) for commit messages

This gitBook explains how we’ve adapted GitFlow and the GitFlow's git extension tools for working with Git
Repository.<br/>
This is how we work internally, and we’re sharing this in the hope that others find it useful too.

## Semantic Release

[Semantic-Release](https://semantic-release.gitbook.io/semantic-release/)  automates the whole package release workflow
including: determining the next version number, generating the release notes and publishing the package.

#### Highlights

- Fully automated release
- Enforce [Semantic Versioning](https://semver.org) specification
- Use formalized commit message convention to document changes in the codebase
- Publish on different distribution channels i.e, __SNAPSHOT__ on `develop, hotfix` branches and __Stable__ in `main`
  branch
- Avoid potential errors associated with manual releases

### Semantic Versioning

Source of truth for version come from _Latest/Highest_ git __tag__ on __Master__ branch

[Semantic Versioning 2.0.0](https://semver.org/) given a version numbering convention _MAJOR.MINOR.PATCH_, increment
the:  `i.e., 1.9.0 -> 1.9.1 -> 1.10.0 -> 2.0.0`

- __MAJOR__ version when you make incompatible API/Breaking changes,
- __MINOR__ version when you add functionality in a backwards compatible manner, and
- __PATCH__ version when you make backwards compatible bug fixes.

> Additional labels for pre-release and build metadata are available as extensions to the _MAJOR.MINOR.PATCH_ format.

    Note: goLang projects need `v` version prefix. So you have to use `v<SemVer>`

#### Tools

- For Angular projects use [ngx-semantic-version](https://d-koppenhagen.de/blog/2019-11-ngx-semantic-version)
- For Java projects use [axion-release-plugin](https://axion-release-plugin.readthedocs.io/en/latest/) Gradle plugin
- For GoLang projects use [git-chglog](https://github.com/git-chglog/git-chglog)

### Conventional Commits

[Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/) is a specification for adding human and machine
readable meaning to commit messages

#### Commit Message Format

Each commit message consists of a __header__, a __body__ and a __footer__. The header has a special format that includes
a __type__, a __scope__ and a __subject__.

```
<type>(<optional scope>): <subject>
<BLANK LINE>
<optional body>
<BLANK LINE>
<optional footer(s)>
```

The __header__ is mandatory and the __scope__ of the header is optional.

Common types according
to [commitlint-config-conventional (based on the the Angular convention)](https://github.com/conventional-changelog/commitlint/tree/master/%40commitlint/config-conventional#type-enum)
can be:

- build
- ci
- chore
- docs
- feat
- fix
- perf
- refactor
- revert
- style
- test
- BEAKING CHANGE:

Real world examples can look like this:

```
chore: run tests on travis ci
fix(server): send cors headers
feat(blog): add comment section
```

#### Tools

- [Commitizen](https://github.com/commitizen/cz-cli)
- [Commitlint](https://github.com/conventional-changelog/commitlint)

### Changelog

On release branch, generate __CHANGELOG.md__ and commit it, before merging back to develop & main.

Generate changelog using [git-chglog](https://github.com/git-chglog/git-chglog).

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and adheres
to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

```bash
# first time
git-chglog --init
# on release
git-chglog -c .github/chglog/config.yml -o CHANGELOG.md --next-tag 2.0.0
```

#### Tools

- [git-chglog](https://github.com/git-chglog/git-chglog)

## GitFlow

[Gitflow](http://nvie.com/posts/a-successful-git-branching-model/) is a branching model for Git, created
by [Vincent Driessen](https://nvie.com/about/).  
It has attracted a lot of attention because it is very well suited to collaboration and scaling the development team.

TL;DR Checkout [How It Works](./gitflow.md) and [Gitflow Usage](./gitflow-usage.md)

### Key Benefits

1. #### Parallel Development

   One of the great things about GitFlow is that it makes parallel development very easy, by isolating new development
   from finished work. New development (such as features and non-emergency bug fixes) is done in __feature branches__,
   and is only merged back into main branch of code when the developer(s) is happy that the code is ready for release.

2. #### Collaboration

   Feature branches also make it easier for two or more developers to collaborate on the same feature, because each
   feature branch is a sandbox where the only changes are the changes necessary to get the new feature working. That
   makes it very easy to see and follow what each collaborator is doing.

3. #### Release Staging Area

   As new development is completed, it gets merged back into the __develop branch__, which is a staging area for all
   completed features that haven't yet been released. So when the next release is branched off of __develop__, it will
   automatically contain all of the new stuff that has been finished.

4. #### Support For Emergency Fixes

   GitFlow supports __hotfix branches__ - branches made from a tagged release. You can use these to make an emergency
   change, safe in the knowledge that the hotfix will only contain your emergency fix. There's no risk that you'll
   accidentally merge in new development at the same time.



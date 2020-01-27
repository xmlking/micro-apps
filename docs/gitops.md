# GitOps

## Merge Flow

This flow is based on [gitflow](https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow) workflow

```
                       DEVELOP          MASTER


                         +                 +
                         |                 |
                       0.1.0             0.1.0
                +--------+                 |
    feature/rq1 |        |                 |
             +-----------+                 |
             |  +---PR-->+                 |
feature/rq2  |           |                 |
             +-----PR--->+                 |
                         |                 |
                         |                 |
                       0.2.0+--Merge---->0.2.0
                         |                 |
             +-----------+                 |
             |           |                 |
feature/rq3  |           |                 |
             +----PR---->+                 |
                         |                 |
                       0.3.0+--Merge---->0.3.0
                         |                 |
                         |                 |
```

### Do's and Don'ts
- __Don't__ push code directly to `master` branch
- __Don't__ push tags directly to `master` branch
- __Don't__ push code directly to `develop` branch
- __Do__ make a `feature/*` branch from `develop` branch
- __Do__ merge `feature/*` branch to `develop` branch via PR process.
- __Release:__ When `develop` branch is stable, 
    - __Do__ tag on `develop` branch using `gradle release` command. Version incremented at `Minor` level 0.1.0 -> 0.2.0
    - __Do__ merge code commits and tags from `develop` branch to `master` branch
- __Hotfix:__ If you need to patch a release,
    - __Do__ make  a `hotfix/*` branch from `master` branch
    - __Do__ tag on `hotfix/*` branch using `gradle release` command. Version incremented at `Patch` level 0.2.0 -> 0.2.1
    - __Do__ merge code commits and tags from `hotfix/*` branch to both `develop` and `master` branches via PR process.
    

### Feature/* branch
Code commits flow from `feature/*` branches to `develop`

### Develop branch
On __develop__ branch, we merge __feature/*__ PRs.<br/>
When the __develop__ is stable, we cut new git tag on __develop__.<br/>
Then we merge __develop__ branch code & tag commits (tag being the last commit) to __master__<br/>

We can use __develop__ branch for automatic building _jars_ and _docker_ images then publish to __nexus__ and __GCR__ via Jenkins Continuous Integration(CI) process.

### Master branch
__master__ branch always represent stable, production ready code. last commit on `master` is always the `tag` commit. 

We can use master branch for automatic deployment to __GKE__ via Jenkins Continuous Deployment(CD) process.

## Versioning

Source of truth for version come from _Latest/Highest_ git __tag__ on __Master__ branch

We are using [Semantic Versioning 2.0.0](https://semver.org/) convention for version numbering. i.e., 1.9.0 -> 1.10.0 -> 1.11.0

## Changelog

Use [spotless](https://github.com/diffplug/spotless-changelog) gradle plugin to generate 

## Changelog

generate changelog using [git-chglog](https://github.com/git-chglog/git-chglog).

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/), and adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

```bash
# first time
git-chglog --init
# on release branch, generate CHANGELOG.md and commit before merging back to develop & master.
git-chglog
git-chglog -next-tag 2.0.0
```

### Axion plugin

We are using Gradle's [axion-release](https://axion-release-plugin.readthedocs.io/en/latest/) plugin for  release & version management.

If current commit is tagged commit, project has a release version( i.e., 0.1.0).<br/>
If there were any commits after last tag, project is in SNAPSHOT version( i.e., 0.1.1-SNAPSHOT). 

> `axion-release` follows the structure of git, only knows about tags on given branch.

## jenkins

Tags are created manually via `gradle release` command on laptop.

Following actions are triggered on __jenkins__

- When commits pushed to `feature/*` branch
    - __Automatic code review:__ jenkins run code quality checks(run unit tests, linting, code coverage,  code quality checks with __sonar__ )
    - jenkins update build status to `BitBucket` 
    - When build pass, Developer crete PR to merge `feature/*` branch to `develop` branch
    - __Approves__ do code review and merge the PR. Optionally delete `feature/*` branch
- When PRs merged from `feature/*` branch to `develop` branch
    - jenkins run __build__ step (build jars,  build docker images)
    - jenkins run __publish__ step (publish jars to Nexus, publish docker images to GCR)
    - jenkins run __deploy__ step (deploy new images to GKE via __helm__ i.e., `helm upgrade bigfoot/core --version=$(gradle cV -q -Prelease.quiet) -f config.yaml`)
    - jenkins run __integration test__ step (run ???  ), targeting to `key-dev` __GKE__ namespace 

> Here the __computed version__ looks like on `develop` branch after PR merge: 0.2.0-SNAPSHOT

- When commits merged from `develop` branch to `master` branch
    - Run all above jenkins steps, but targeting to `key-int` __GKE__ namespace 
    
> Here the __computed version__ looks like on `master` branch: 0.2.0
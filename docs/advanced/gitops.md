# GitOps

## Constraints 

### Branches

Main branches
- __Master__ for production
- __Develop__ for next release

__master__ branch always represent stable, production ready code. last commit on `master` is always the `tag` commit. 

We use __develop__ branch for automatic building _jars_ and _docker_ images then publish to __nexus__ and __GCR__ via Jenkins Continuous Integration(CI) process.<br/>
We use __master__ branch for automatic deployment to _integration_ __GKE__ cluster/namespace via Continuous Deployment(CD) process.

Supporting branches 
- __Release__ naming `release/#.#.#`
- __Feature__ naming `feature/*`
- __Hotfix__ naming `hotfix/*`

Supporting branches are temporary. after merging, they get deleted. 

### Restrictions

Block commits directly to `master` branch except merges by __Release Manager__ role 
Block commits directly to `develop` branch, and allow only _PR_ mergers. 

### Do's and Don'ts

- __Don't__ push code directly to `master` branch
- __Don't__ push tags directly to `master` branch
- __Don't__ push code directly to `develop` branch
- __Do__ make a `feature/*` branch from `develop` branch
- __Do__ merge `feature/*` branch to `develop` branch via PR process.
- __Release:__ When `develop` branch is stable, create `release` branch i.e., `release/0.1.0`
    - __Do__ create CHANGELOG.md and commit on `release` branch 
    - __Do__ merge code commits from `release` branch to `develop` and `master` branch
    - We __shouldn't__ merge any features directly to the release branch
    - We __should__ only add bug fixes and release note, changelog to release branch.
- __Hotfix:__ If you need to patch a release,
    - __Do__ make  a `hotfix/*` branch from `master` based on specific tag or head of the `master` branch
    - __Do__ merge code commits from `hotfix/*` branch to both `develop` and `master` branches via PR process.
- Push feature branches back to origin repo so others can collaborate
- Use the GitHub/BitBucket website to create pull requests from feature branches
- __Don’t__ accept your own pull requests!

## CI/CD Automation

Following actions are triggered on __CI__ (jenkins, github actions)
 
- When code commits pushed to `feature/*` branch
    - __Automatic code review:__ CI run code quality checks(run unit tests, linting, code coverage,  code quality checks with __sonar__ )
    - CI continuously check code quality with each push and update build status on `BitBucket` or `GitHib` PR
    - When CI checks pass, Developer crete PR to merge `feature/*` branch to `develop` branch
    - __Approves__ do code review and merge the PR. Finally delete `feature/*` branch
- When PRs merged from `feature/*` branch to `develop` branch
    - CI run __build__ step (build jars,  build docker images with __next__ _SNAPSHOT_ version)
    - Here the __computed version__ looks like `0.2.1-SNAPSHOT`
    - CI run __publish__ step (publish jars to Nexus, publish docker images to GCR)
    - CI run __deploy__ step (deploy new images to _integration_ __GKE__ cluster via __helm__ i.e., `helm upgrade bigfoot/core --version=$VERSION -f config.yaml`)
    - CI run __e2e test__ step (run ???  ), targeting _qa_ __GKE__ cluster/namespace 
- When code commits pushed to `release/*` branch
    - Above `feature/*` steps will also apply to `release/*` and `hotfix/*` branches
    - In addition, we can also automatically generate __Changelog__ and __Release__ notes.  
- When `release/*` branch merged to `master` branch and tagged
    - CI run __build__ step (build jars,  build docker images with stable __version__ from _Latest/Highest_ git tag on `master` branch)
    - Here the __computed version__ looks like: `0.2.0`
    - CI run __publish__ step (publish jars to Nexus, publish docker images to GCR)
    - CI run __deploy__ step (deploy new images to _integration_ __GKE__ cluster via __helm__ i.e., `helm upgrade bigfoot/core --version=$VERSION -f config.yaml`)
    - CI run __e2e test__ step (run ???  ), targeting _integration_ __GKE__ cluster/namespace 





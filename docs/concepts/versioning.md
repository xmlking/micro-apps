# Versioning

## Introduction

One of the topics that the [original GitFlow article](https://nvie.com/posts/a-successful-git-branching-model/) doesn’t
address at all is what scheme to adopt for your software’s version numbers.

But we believe that life is easier for __everyone__ if version numbers __mean the same thing__ to everyone who is
working on a piece of software.

## Semantic Versioning

[Semantic versioning](http://semver.org) is a very simple scheme built around the __X.Y.Z-buildNo__ concept (for
example __2.6.0-2__ or __2.6.0-SNAPSHOT__):

* Increment __Z__ when you fix something
* Increment __Y__ when you add a new feature
* Increment __X__ when you break backwards-compatibility or add major features
* Use the __buildNo__ to differentiate different builds off the same branch, and to differentiate between development,
  test and production builds.

## Version Numbers And GitFlow Branches

Here's what to build from which branch.

* __Feature branches__ and the __develop branch__ should always build __snapshot__ versions (e.g. 2.6.0-SNAPSHOT).
* __Release branches__ and __hotfix branches__ should always build __release candidate__ versions (e.g. 2.6.0-0.rc1).
* The __master branch__ should always build unqualified versions - versions without build numbers (e.g. 2.6.0).

<p><span class="label label-important">Please remember:</span></p>

__Remember:__

* When you create a new branch, you need to __manually update__ the software's version number. The __GitFlow tools__
  cannot do this for you.

## What You Should Install Where

_As a rule of thumb ..._

* __Snapshot versions__ should only be installed on __dev boxes__ and __integration environments__. They shouldn't be
  deployed to any of the __dedicated test environments__.
* __Release candidates__ should be installed in the __dedicated test environments__. In an emergency, a release
  candidate can be installed into the __production environment__.
* __Production releases__ can be installed anywhere - and they are the only kind of build that should be installed into
  the __production environment__.

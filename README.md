# XRef
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Project status](https://img.shields.io/badge/status-discontinued-lightgray.svg)](https://gist.githubusercontent.com/jcornaz/46736c3d1f21b4c929bd97549b7406b2/raw/ProjectStatusFlow)
[![JitPack](https://jitpack.io/v/jcornaz/xref.svg)](https://jitpack.io/#jcornaz/xref)
[![Build Status](https://travis-ci.org/jcornaz/xref.svg?branch=master)](https://travis-ci.org/jcornaz/xref)
[![Code coverage](https://codecov.io/gh/jcornaz/xref/branch/master/graph/badge.svg)](https://codecov.io/gh/jcornaz/xref)
[![Code quality](https://codebeat.co/badges/d7377041-eddf-4d30-bb04-81d91ba2b69b)](https://codebeat.co/projects/github-com-jcornaz-xref-master)

XRef is a kotlin library allowing management of cross references via kotlin property delegation

## Project status
This project is discontinued. It will be no longer maintained and no support will be provided.

The reason, is that it is less usefull as expected in practice. Usually good design would tend to create immuatable data structures and service to use them. So the need of auto-managed relation is almost inexistant.

Anyone is free to read, study, modify and to create derivated products.

## Use
KDoc is published here: https://jcornaz.github.io/xref/doc/1.0/xref/xref/index.html

### Declare a relation
Here is an example of a parent-child relation (which is one-to-many)
```kotlin
// Define the relation. References will be managed here.
object ParentChildRelation : OneToMany<Parent, Child>()

// Declare the parent class
class Parent {

    // Delegate the children property to the relation management
    var children: Set<Child> by ParentChildRelation.right()
}

// Declare the child class
class Child {

    // Delegate the parent property to the relation management
    var parent: Parent? by ParentChildRelation.left()
}
```

And that's it ! Now you can assign a parent to a child, or add children to a parent. It will always be kept coherent by XRef.

### Use the relation
Once you have the relation defined, you can use it as you would with standard properties.

```kotlin
// given
val parent = Parent()
val children = setOf(Child(), Child())

// when
parent.children = children

// then
parent.children shouldBe children
children.forEach { it.parent shouldBe parent }
```

## Supported relations
Currently XRef support 4 types of relations :
* **ManyToMany**
* **OneToOne**
* **OneToMany**
* **NotNullOneToMany** *(in a parent-child scenario, it would means that the child has always a parent)*

## Motivation
Even if it is always simpler to use immutable instances and not cross reference objects.
Sometime, we have to create mutable classes that can be cross referenced.
In these cases, writing code that always keep a coherent state in your references can quickly generate boilerplate code,
and demand to invest unnecessary time to make sure it is well tested and reliable.

The goal of this project is to centralize the reference management code and provide kotlin delegation properties for that.
Then there cross referencing don't generate more boilerplate code, become reliable AND quick and easy to write.

## Add XRef to your project
Get the artifacts from [jitpack](https://jitpack.io/#jcornaz/xref)

### With gradle

```gradle
repositories {
    jcenter()
    maven { url 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.jcornaz:xref:v1.0-RC1'
}
```

### With maven, sbt or leiningen
Follow theses [instructions](https://jitpack.io/#jcornaz/xref)

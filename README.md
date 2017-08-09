# XRef
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://jcornaz.github.io/xref/doc/1.0/xref)
[![JitPack](https://jitpack.io/v/jcornaz/xref.svg)](https://jitpack.io/#jcornaz/xref)
[![Build Status](https://travis-ci.org/jcornaz/xref.svg?branch=master)](https://travis-ci.org/jcornaz/xref)
[![Code coverage](https://codecov.io/gh/jcornaz/xref/branch/master/graph/badge.svg)](https://codecov.io/gh/jcornaz/xref)
[![Code quality](https://codebeat.co/badges/2bab1c10-221a-4188-8250-9b0b12cefbb0)](https://codebeat.co/projects/github-com-jcornaz-xref-master)

[![KDoc](https://img.shields.io/badge/kdoc-1.0-blue.svg)](https://jcornaz.github.io/xref/doc/1.0/xref/xref/index.html)
[![Issues](https://img.shields.io/github/issues/jcornaz/xref.svg)](https://github.com/jcornaz/xref/issues)
[![Pull requests](https://img.shields.io/github/issues-pr/jcornaz/xref.svg)](https://github.com/jcornaz/xref/pulls)

XRef is a kotlin library that allows management of cross references with simple property delegation

## Use
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
children.forEach { it shouldBe parent }
```

## Supported relations
Currently XRef support 4 types of relations :
* **ManyToMany**
* **OneToOne**
* **OneToMany**
* **NotNullOneToMany** *(in a parent-child scenario, it would means that the child has always a parent)*

## Motivation
Even if it is always simpler to use immutable instances and not cross referenced objects.
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
    maven { url 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.jcornaz:xref:v1.0-RC1'
}
```

### With maven, sbt or leiningen
Follow theses [instructions](https://jitpack.io/#jcornaz/xref)

## Test
Simply checkout this repository and run the gradle task :

```bash
./gradlew check
```

The tests are located in `src/kotlin/xref/`

## License

MIT License

Copyright (c) 2017 Jonathan Cornaz

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.



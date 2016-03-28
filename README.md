# Scala from Scratch
Summarizing the major books on the language

## 0. Programming in Scala 2nd Edition

* [Code written in the IntelliJ Scala Worksheet](https://github.com/shehaaz/Scala-from-Scratch/tree/master/scala_playground)
* Scala Options Explained

	[![Alt text for your video](http://img.youtube.com/vi/6Pd-1a3-Loc/0.jpg)](https://youtu.be/6Pd-1a3-Loc)
* [Scala Futures](http://doc.akka.io/docs/akka/2.4.2/scala/futures.html): In the Scala Standard Library, a Future is a data structure used to retrieve the result of some concurrent operation. This result can be accessed synchronously (blocking) or asynchronously (non-blocking). I will be focusing on the non-blocking futures.
 

## 1. Scala in Action

### Part 1 Scala: The Basics

### Part 2 Working with Scala

### Part 3 Advanced Steps

## 2. Learning Scala 

### Part 1. Core Scala

#### Working with Data: Literals, Values, Variables, and Types

#### Expressions and Conditionals

#### Functions

#### First-Class Functions

#### Collections

### Part 2. Object-Oriented Scala
#### Classes
#### Objects, Case Classes, and Traits

Case classes are used to conveniently store and match on the contents of a class. 
You can contruct them without using new

`scala> case class Cars(brand: String, model: String)`
`defined class Cars`

`scala> val tesla1 = Cars("Tesla", "Model-S")`
`tesla1: Cars = Cars(Tesla,Model-S)`

Case classes automatically have equality

`scala> val tesla2 = Cars("Tesla", "Model-S")`
`tesla2: Cars = Cars(Tesla,Model-S)`

`scala> tesla1 == tesla2`
`res0: Boolean = true`

#### Advanced Typing

## 3. Effective AKKA
[HelloWorld example](https://github.com/shehaaz/Scala-from-Scratch/blob/master/akka/Akka/src/main/scala/helloworld/HelloWorld.scala) + [Article](http://alvinalexander.com/scala/simple-scala-akka-actor-examples-hello-world-actors)

[Ping Pong example](https://github.com/shehaaz/Scala-from-Scratch/blob/master/akka/Akka/src/main/scala/pingpong/pingpong.scala) + [Article](http://alvinalexander.com/scala/scala-akka-actors-ping-pong-simple-example)

[Getting started Scala and Akka](http://doc.akka.io/docs/akka/2.0/intro/getting-started-first-scala.html)

[Concurrency and Fault Tolerance Made Easy: An Akka Tutorial with Examples](https://www.toptal.com/scala/concurrency-and-fault-tolerance-made-easy-an-intro-to-akka)

What is an Actor in Akka?
An actor is essentially nothing more than an object that receives messages and takes actions. 
It is decoupled from the source of the message and its only responsibility is to recognize the type of message and take action accordingly.

What type of actions?
* Execute some operations itself (such as performing calculations, persisting data, calling an external web service, and so on)
* Forward the message, or a derived message, to another actor
* Instantiate a new actor and forward the message to it

[Akka Demo: count words from text file](https://github.com/shehaaz/Scala-from-Scratch/blob/master/akka/Akka/src/main/scala/AkkaDemo.scala)

[Reactive Messaging Patterns explained](https://github.com/shehaaz/Scala-from-Scratch/tree/master/akka/Akka/src/main/scala/reactivemessagingpatterns)

## 4. Functional Programming in Scala
[This](https://github.com/fpinscala/fpinscala) repository contains exercises, hints, and answers for the book
[Functional Programming in Scala](http://manning.com/bjarnason/)

## 5. Scala for the impatient
The apply method is the scala version of a constructor
e.g in the class StringOps
`def apply(n: Int): Char`
`"Hello".apply(4) == "o"`
`"Hello"(4) == "o"`

`An upper type bound T <: A declares that type variable T refers to a subtype or equal to type A`

`The lower-bound operator T >: A, restricting types to those that are equal to or are extended by the given type T.`

example in [Scala Option class](https://github.com/scala/scala/blob/5cb3d4ec14488ce2fc5a1cc8ebdd12845859c57d/src/library/scala/Option.scala#L120):

	`final def getOrElse[B >: A](default: => B): B = if (isEmpty) default else this.get`



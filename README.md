# Scala from Scratch
Summarizing the major books on the language

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
#### Advanced Typing

## 3. Effective AKKA
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


## 4. Functional Programming in Scala
[This](https://github.com/fpinscala/fpinscala) repository contains exercises, hints, and answers for the book
[Functional Programming in Scala](http://manning.com/bjarnason/)

## 5. Scala for the impatient
The apply method is the scala version of a constructor
e.g in the class StringOps
`def apply(n: Int): Char`
`"Hello".apply(4) == "o"`
`"Hello"(4) == "o"`

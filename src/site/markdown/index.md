## Introduction
Welcome to the nimble4j (aka N4j) project documentation site. 
Here you can find all information about the nimble4j project as well as guides and examples on using nimble4j.


### What is nimble4j?
N4j is a set of java libraries with which a developer can create Java applications. You can think of it as a set of reusable modular java libraries
which can be selectively chosen to create simple java apps. Most of these libraries are well known and mature libraries in the oss java world.

### Why nimble4j?
The N4j team wanted a re-usable and 'back to basics' approach to build applications on the Java platform. The N4j team wanted to target a loosely coupled set of libraries which favoured composition over a rigid framework. Also a driving philosophy for these libraries was to keep things explicit and small abstractions instead of hiding / magic.
The primary goal was that the programmer(s) should be in complete control over what happens in the application. 
So N4j can be thought of a composable set of java libraries.

### Uses of N4j
Nimble4j is a modular set of libraries which cater to the below domain of applications
1. Web applications
2. Web APIs
3. Command line interfaces
4. Interactive command line applications (like REPLs)

### Nimble4j programming model
The Nimble4j programming model is depicted below. 

<div style="display: flex; justify-content: center;">
  <img src="images/programming_model.svg" alt="N4j programming model"/>
</div>


#### App
An App is short for Application. Any java program with an 'main' entry point is a valid App in Nimble4j terminology

#### Modules
Nimble4j encourages developer to build modular libraries which can be composed together to create different applications.
Modules are the primary unit of composition in Nimble4j. A logical model of an application built on Nimble4j is depicted below

<<Insert N4j's concept of Modules here>>

#### Configuration
N4j formalizes the concept of configuration. The configuration can be thought of as a tree of information objects which are used to configure different modules in an application.
The diagram below explains the notion of configuration in N4j's context.

<<Insert N4j's perspective of configuration>>

#### Capability / Service
N4j encourages the programmer to think in terms of Capabilities / Services rather than 'Objects'. 
Towards this it provides the developers for the building blocks necessary for achieving this goal.



Welcome to JOpenMetaVerse!!!

Introduction
==============
* A library to interact with 3d virtual world simulators (ex. OpenSim).
* Completely written in Java.
* The core library implements:
  + Simulator Protocol (Protocol connecting to OpenSim)
  + Networking (based on HTTP and UDP)
  + Imaging (Jpeg2000, TGA Images)
  + Android SDK Compatible
  + 3D Rendering (Currently being Developed)


How to get it
===============

Setting up Git on BitBucket

Add Account Public Key:

https://confluence.atlassian.com/display/BITBUCKET/Using+the+SSH+protocol+with+bitbucket
https://confluence.atlassian.com/display/BITBUCKET/How+to+install+a+public+key+on+your+bitbucket+account

Clone the respository
git clone ssh://git@bitbucket.org/<account Name>/reponame.git


=Some useful Mvn Commands

==Run a specific test case
mvn clean -Dtest=TWorkflowTaskClientTest test

==Install the project, skipping test cases
mvn clean install -Dmaven.test.skip=true

==Check and apply license header
mvn license:check -Ddate=2012
mvn license:format -Ddate=2012




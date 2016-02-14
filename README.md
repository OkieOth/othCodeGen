# othCodeGen
This project try to provide a tool for handy code generation. The code generation base is written in groovy. It works with a set of generators that create for a specific purpose.

At this moment contains some generators to build a java based dao layer. This dao layer works with jdbc and pure sql statements. It has functions to query, insert, change and delete data of a given model. The query functions also has the ability retrieve paged data.  

Currently this project is in a early state. The basics are done, and some unit and integration tests are added.

## Reqirements
In reason of some integration testing stuff the project works best under a linux environment. If someone changes the scripts, currently bash scripts, used in the preinitialization phase it should also running on windows.

* Java 8
* Gradle 2.8
* Groovy 2.4.5

## Steps to run

    # a file system based maven repository is used to store own artifacts
    mkdir $HOME/myMavenRepos

    # build some helper libraries
    cd $OTH_CODE_GEN_HOME/jDaoRequirements
    gradle clean test publish

    cd $OTH_CODE_GEN_HOME/jJdbcjDaoRequirements
    gradle clean test publish

    # build and test the main code of the code generation
    cd $OTH_CODE_GEN_HOME/gCodeGen
    gradle clean test integrationTest

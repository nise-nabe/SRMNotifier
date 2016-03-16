SRMNotifier
===========

[![Build Status](https://secure.travis-ci.org/nise-nabe/SRMNotifier.png?branch=master)](http://travis-ci.org/nise-nabe/SRMNotifier) [![GitHub release](https://img.shields.io/github/release/nise-nabe/SRMNotifier.svg)]()

This is a notifier for topcoder SRM using twitter.

[twitter bot](https://twitter.com/#!/tc_srm_jp_bot)

How to Develop
--------------

Execute following Command, and edit for your enviromnent. 

    $ cp src/main/resources/twitter4j.properties.sample src/main/resources/twitter4j.properties
    $ cp src/main/webapp/WEB-INF/appengine-web.xml.sample src/main/webapp/WEB-INF/appengine-web.xml

Run test

    $ mvn test

Run server for develop. See more information when the server start.

    $ mvn compile appengine:enhance appengine:devserver

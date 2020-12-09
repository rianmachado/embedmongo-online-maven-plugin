#!/bin/bash
echo '**************************************************************'
echo '*                                                            *'
echo '*                Quality Tools Rian                          *'
echo '*                                                            *'
echo '**************************************************************' 

mvn clean
mvn compile 
mvn verify -P cobertura
#mvn checkstyle:checkstyle
mvn verify -P owasp -DskipTests=true

mvn jxr:jxr pmd:pmd -DskipTests=true
#mvn pmd:check

mvn spotbugs:spotbugs -DskipTests=true
mvn spotbugs:spotbugs -P security -DskipTests=true
mvn verify sonar:sonar -DskipTests=true
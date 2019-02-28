#!/bin/sh
java \
-XX:+UnlockExperimentalVMOptions \
-XX:+UseCGroupMemoryLimitForHeap \
$* -jar ./judge-d-contract-publisher-0.1-SNAPSHOT.jar $0 $1 $2 $3 $4

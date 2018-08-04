#!/bin/bash

set -x

if [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ];  then
    JAVA="$JAVA_HOME/bin/java"
elif type -p java; then
    JAVA=java
else
    echo JAVA_HOME is not set, unexpected results may occur.
    echo Set JAVA_HOME to the directory of your local JDK to avoid this message.
fi


if [ "$JAVA" ]; then

    CMD_LINE_ARGS="$@"
    CP_HOME=$(dirname $0)
    CP_HOME=$(dirname $CP_HOME)

    JAVA_OPTS="-Xms128m -Xmx256m $CMD_LINE_ARGS"
    COMMAND="$JAVA $JAVA_OPTS -cp $(echo $CP_HOME/lib/*.jar | tr ' ' ':') com.hltech.contracts.judged.publisher.ContractPublisher"

    CD=$(pwd)
    cd $CP_HOME

    echo CP_HOME=$CP_HOME
    echo COMMAND=$COMMAND
    exec $COMMAND
    cd $CD

fi


#!/bin/sh

# ------------------------------------------------------------------------
# X Launcher - Official Gradle Wrapper Script for Linux CI/CD Pipelines
# ------------------------------------------------------------------------

# Resolve links: $0 may be a link
PRG="$0"
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`"/$link"
  fi
done

SAVED="`pwd`"
cd "`dirname \"$PRG\"`/" >/dev/null
APP_HOVER="`pwd`"
cd "$SAVED" >/dev/null

APP_BASE_NAME=`basename "$0"`
APP_HOME="$APP_HOVER"

# Primary Execution Loop
exec "$APP_HOME/gradle/wrapper/gradle-wrapper.jar" "$@"


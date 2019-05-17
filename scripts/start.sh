# 编译
# mvn clean install -Dmaven.test.skip=true

# 部署运行
#
APPNAME="ont-sourcing-2c-0.0.1-SNAPSHOT.jar"
APPLOCATION="/home/ubuntu/ont-sourcing-2c/target/ont-sourcing-2c-0.0.1-SNAPSHOT.jar"
APPENV="/home/ubuntu/ont-sourcing-2c/config/application-remotetest.properties"
APPPORT=9088
LOGLOCATION="/home/ubuntu/ont-sourcing-2c/log/all.log"

# touch /root/ont-sourcing/log/all.log

#
ps -ef | grep $APPNAME | grep -v grep | awk '{print $2}' | xargs kill -9

#
echo "nohup java -Dspring.config.location=$APPENV -Dserver.port=$APPPORT -jar $APPLOCATION >/dev/null 2>&1 &"
nohup java -Dspring.config.location=$APPENV -Dserver.port=$APPPORT -jar $APPLOCATION >/dev/null 2>&1 &

#
tail -f $LOGLOCATION
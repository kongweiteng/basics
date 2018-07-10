FROM reg.enncloud.cn/enncloud/fnw:v1
MAINTAINER kongweiteng@ENN.CN
ADD docker/. /
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'java $PINPOINT_AGENT -Xms1024m -Xmx2048m -jar -Dspring.profiles.active=$PROFILE /energy_cloud.jar' > /start.sh;chmod 777 /start.sh
CMD /start.sh
EXPOSE 8081
FROM openjdk:8u181-jre


COPY judge-d-contract-publisher*.zip /root/judge-d-contract-publisher.zip
ADD ./entrypoint.sh /root/judge-d-contract-publisher/entrypoint.sh

RUN cd /root/judge-d-contract-publisher && \
    unzip ../judge-d-contract-publisher.zip && \
    rm -f ../judge-d-contract-publisher.zip && \
    chmod +x /root/judge-d-contract-publisher/entrypoint.sh

WORKDIR /root/judge-d-contract-publisher

CMD ["java"]

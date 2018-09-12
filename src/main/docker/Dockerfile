FROM openjdk:8u181-jre


COPY judge-d-contract-publisher-0.2.0-bin.zip /root/judge-d-contract-publisher-0.2.0.zip

RUN cd /root && \
    unzip ./judge-d-contract-publisher-0.2.0.zip && \
    rm -f ./judge-d-contract-publisher-0.2.0.zip

WORKDIR /root/judge-d-contract-publisher-0.2.0

ENTRYPOINT ["/root/judge-d-contract-publisher-0.2.0/bin/contract-publisher.sh"]

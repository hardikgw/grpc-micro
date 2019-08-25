# gRPC example using Spring Boot

## Simple example for:
1. gRPC in Spring Boot apppllication (Separate Port)
2. TODO: gRPC + Spring Boot on same port
3. TODO: gRPC proxy with Spring Controller
 
## Steps to run :
1. git clone (...)
2. generate gRPC stubs ($gradle installDist)
3. run app ($gradle bootRun)

## Steps to build and tag docker image
1. git clone (...)
2. docker build . -t grpc-svc
3. docker tag grpc-svc grpc-svc:v1


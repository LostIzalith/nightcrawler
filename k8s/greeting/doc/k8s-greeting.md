## Demo - Greeting App deployed on Kubernetes

Simple Spring Boot app deployment on Minikube

#### Start Minikube

- Install Minikube from https://kubernetes.io/docs/tasks/tools/install-minikube/

- Install `kubectl` from https://kubernetes.io/docs/tasks/tools/install-kubectl/

Then, start Minikube:

	minikube start

#### Create and run a simple Spring Boot app

Create a simple Boot app (You can also use https://start.spring.io/[Spring Initializr] web interface)

`src/main/kotlin/com/github/lostizalith/greeting/GreetingApplication.kt`
```kotlin
package com.github.lostizalith.greeting

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * Simple spring boot greeting application.
 */
@SpringBootApplication
class GreetingApplication

fun main(args: Array<String>) {
    SpringApplication.run(GreetingApplication::class.java, *args)
}
```

`src/main/kotlin/com/github/lostizalith/greeting/domain/Greeting.kt`
```kotlin
package com.github.lostizalith.greeting.domain

/**
 * Greeting domain data entity.
 */
data class Greeting(var id: Long, var name: String)
```

`src/main/kotlin/com/github/lostizalith/greeting/application/controller/GreetingController.kt`
```kotlin
package com.github.lostizalith.greeting.application.controller

import com.github.lostizalith.greeting.application.dto.GreetingResponse
import com.github.lostizalith.greeting.domain.Greeting
import org.dozer.DozerBeanMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

/**
 * Simple greeting rest endpoint.
 */
@RestController
@RequestMapping(value = ["/api/v1"])
class GreetingController(@Autowired val mapper: DozerBeanMapper) {

    private val counter = AtomicLong()

    @GetMapping(value = ["/greeting"])
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String): ResponseEntity<*> {
        val request = Greeting(counter.getAndIncrement(), "Hello $name !")

        val response = mapper.map(request, GreetingResponse::class.java)

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response)
    }
}
```
Create a `Dockerfile` so we can package this app as a Docker image

`Dockerfile`
````dockerfile
FROM openjdk:8-alpine
ADD ./target/greeting-0.0.1-SNAPSHOT.jar /greeting.jar
RUN sh -c 'touch /greeting.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/greeting.jar"]
````

Build the app and the Docker image

NOTE: We are not sharing the Docker environment used by Minikube

```bash
./mvnw clean install
docker build -t $DOCKER_ID_USER/greeting:0.0.1 .
docker run --name solairerove-greeting -p 8080:8080 -d solairerove/greeting:0.0.1
```

Push image into docker hub

```bash
docker login
docker push $DOCKER_ID_USER/greeting:0.0.1
```

Run a Kubernetes deployment on the running Minikube cluster

```bash
kubectl run greeting --replicas=2 --image $DOCKER_ID_USER/greeting:0.0.1 --port 8080
kubectl expose deployment greeting --type=LoadBalancer --name=greeting-svc
```

Test the app

```bash
curl $(minikube service greeting-svc --url)/greeting
curl $(minikube service greeting-svc --url)/application/env
```

Create Deployment and Service YAML files for future repeatable deployments
ProTip from Joe Beda at Heptio: https://blog.heptio.com/using-kubectl-to-jumpstart-a-yaml-file-heptioprotip-6f5b8a63a3ea
```bash
kubectl run greeting --replicas=2 --image $DOCKER_ID_USER/greeting:0.0.1 --port 8080 -o yaml --dry-run > greeting.yaml
kubectl expose deployment greeting --type=LoadBalancer --name=greeting-svc -o yaml --dry-run > greeting-svc.yaml
```

Delete the resources created for `greeting`
```bash
kubectl delete all -l run=greeting
```

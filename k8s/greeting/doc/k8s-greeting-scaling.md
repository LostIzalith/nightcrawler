# Kubernetes replication controller 

## Create `deployment` with `replica spec`

```yaml
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  labels:
    run: greeting
  name: greeting
spec:
  replicas: 2
  selector:
    matchLabels:
      run: greeting
  template:
    metadata:
      labels:
        run: greeting
    spec:
      containers:
      - image: solairerove/greeting:0.0.1
        name: greeting
        ports:
        - containerPort: 8080
```

## Create pods due kubectl

`kubectl create -f greeting.yml`

## Check if it's running

`kubectl get pods`

## Delete one of pods for example

`kubectl delete pod $name_of_depoyment`

```bash
$ kubectl delete pod greeting-56bf77749c-c6qdl
pod "greeting-56bf77749c-c6qdl" deleted

$ kubectl get pods
NAME                                     READY     STATUS        RESTARTS   AGE
greeting-56bf77749c-b85b8                1/1       Running       0          0s
greeting-56bf77749c-c6qdl                0/1       Terminating   0          4m
greeting-56bf77749c-pm2d6                1/1       Running       0          4m
hello-minikube-5bc754d4cd-lsfjr          1/1       Running       0          6m
my-release-postgresql-74b6ddfcfb-hcrv2   1/1       Running       2          71d
```

## Wait a few seconds

```bash
$ kubectl get pods
NAME                                     READY     STATUS    RESTARTS   AGE
greeting-56bf77749c-b85b8                1/1       Running   0          1m
greeting-56bf77749c-pm2d6                1/1       Running   0          5m
hello-minikube-5bc754d4cd-lsfjr          1/1       Running   0          7m
my-release-postgresql-74b6ddfcfb-hcrv2   1/1       Running   2          71d
```

## To manual scaling you can use

```bash
$ kubectl scale --replicas=4 -f k8s/greeting/greeting.yaml
$ kubectl get pods
NAME                                     READY     STATUS    RESTARTS   AGE
greeting-56bf77749c-6vhrq                1/1       Running   0          0s
greeting-56bf77749c-b85b8                1/1       Running   0          3m
greeting-56bf77749c-pm2d6                1/1       Running   0          8m
greeting-56bf77749c-zk9c7                1/1       Running   0          0s
hello-minikube-5bc754d4cd-lsfjr          1/1       Running   0          10m
my-release-postgresql-74b6ddfcfb-hcrv2   1/1       Running   2          71d
$ kubectl get pods
NAME                                     READY     STATUS    RESTARTS   AGE
greeting-56bf77749c-6vhrq                1/1       Running   0          18s
greeting-56bf77749c-b85b8                1/1       Running   0          4m
greeting-56bf77749c-pm2d6                1/1       Running   0          8m
greeting-56bf77749c-zk9c7                1/1       Running   0          18s
hello-minikube-5bc754d4cd-lsfjr          1/1       Running   0          10m
my-release-postgresql-74b6ddfcfb-hcrv2   1/1       Running   2          71d
```
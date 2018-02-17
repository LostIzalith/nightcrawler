# Kubernetes deployment object

## Create yaml file

```yaml
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: greeting-deployment
spec:
  replicas: 3
  template:
    metadata:
      labels:
        run: greeting
    spec:
      containers:
      - name: greeting
        image: solairerove/greeting:0.0.1
        ports:
        - name: greeting-port
          containerPort: 8080
```

## Create deployment with kubectl

```bash
$ kubectl create -f k8s/greeting/greeting-deployment.yaml
deployment "greeting-deployment" created
```

## Get all deployment objects

```bash
$ kubectl get deployment
NAME                  DESIRED   CURRENT   UP-TO-DATE   AVAILABLE   AGE
greeting-deployment   3         3         3            3           38s
```

## Get all replication controllers

```bash
$ kubectl get rs
NAME                             DESIRED   CURRENT   READY     AGE
greeting-deployment-77f4cb64c4   3         3         3         2m
```

## You can also get pods

```bash
$ kubectl get pods
NAME                                   READY     STATUS    RESTARTS   AGE
greeting-deployment-77f4cb64c4-5sjz5   1/1       Running   0          3m
greeting-deployment-77f4cb64c4-qzztr   1/1       Running   0          3m
greeting-deployment-77f4cb64c4-wzgxl   1/1       Running   0          3m
```

## Get all pods with labels

```bash
$ kubectl get pods --show-labels
NAME                                   READY     STATUS    RESTARTS   AGE       LABELS
greeting-deployment-77f4cb64c4-5sjz5   1/1       Running   0          4m        pod-template-hash=3390762070,run=greeting
greeting-deployment-77f4cb64c4-qzztr   1/1       Running   0          4m        pod-template-hash=3390762070,run=greeting
greeting-deployment-77f4cb64c4-wzgxl   1/1       Running   0          4m        pod-template-hash=3390762070,run=greeting
```

## Get ask rollout status

```bash
$ kubectl rollout status deployment greeting-deployment
deployment "greeting-deployment" successfully rolled out
```

## To connect to app

```bash
$ kubectl expose deployment greeting-deployment --type=NodePort
service "greeting-deployment" exposed
```

## Get service

```bash
$ kubectl get service
NAME                  TYPE        CLUSTER-IP   EXTERNAL-IP   PORT             AGE
greeting-deployment   NodePort    10.0.0.113   <none>        8080:31865/TCP   42s
kubernetes            ClusterIP   10.0.0.1     <none>        443/TCP          84d
```

## Describe service

```bash
$ kubectl describe service greeting-deployment
Name:                     greeting-deployment
Namespace:                default
Labels:                   run=greeting
Annotations:              none
Selector:                 run=greeting
Type:                     NodePort
IP:                       10.0.0.113
Port:                     unset  8080/TCP
TargetPort:               8080/TCP
NodePort:                 unset  31865/TCP
Endpoints:                172.17.0.3:8080,172.17.0.4:8080,172.17.0.8:8080
Session Affinity:         None
External Traffic Policy:  Cluster
Events:                   none
```

## Connect to service with minikube

```bash
$ minikube service greeting-deployment --url
http://192.168.99.100:31865
```

## Check if it's work

```bash
$ curl http://192.168.99.100:31865/greeting\?name\=Mikita
{"id":1,"content":"Hello, Mikita"}%
```

## Describe deployment object and find container name

```bash
$ kubectl describe deployment greeting-deployment
Name:                   greeting-deployment
Namespace:              default
CreationTimestamp:      Sun, 11 Feb 2018 18:04:08 +0300
Labels:                 run=greeting
Annotations:            deployment.kubernetes.io/revision=1
Selector:               run=greeting
Replicas:               3 desired | 3 updated | 3 total | 3 available | 0 unavailable
StrategyType:           RollingUpdate
MinReadySeconds:        0
RollingUpdateStrategy:  1 max unavailable, 1 max surge
Pod Template:
  Labels:  run=greeting
  Containers:
   greeting:
    Image:        solairerove/greeting:0.0.1
    Port:         8080/TCP
    Environment:  none
    Mounts:       none
  Volumes:        none
Conditions:
  Type           Status  Reason
  ----           ------  ------
  Available      True    MinimumReplicasAvailable
OldReplicaSets:  none
NewReplicaSet:   greeting-deployment-77f4cb64c4 3/3 replicas created
Events:          none
```

## Set new version of image

```bash
$ kubectl set image deployment/greeting-deployment greeting=solairerove/greeting:0.0.2
deployment "greeting-deployment" image updated
```

## Check if it's updated

```bash
$ curl http://192.168.99.100:31865/api/v1/greeting\?name\=Mikita
{"atomic cola":0,"name":"Hello Mikita ! v2"}%
```

## Fetch rollout history

```bash
$ kubectl rollout history deployment greeting-deployment
deployments "greeting-deployment"
REVISION  CHANGE-CAUSE
1         none
2         none
```

## Undo deployment with new image

```bash
$ kubectl rollout undo deployment greeting-deployment
deployment "greeting-deployment"
```

## See if it's really changed

```bash
$ kubectl get pods
NAME                                   READY     STATUS        RESTARTS   AGE
greeting-deployment-56bf8d87f9-b5dbw   0/1       Terminating   0          6m
greeting-deployment-56bf8d87f9-r94sn   0/1       Terminating   0          6m
greeting-deployment-56bf8d87f9-swsb9   0/1       Terminating   0          6m
greeting-deployment-77f4cb64c4-grbh2   1/1       Running       0          25s
greeting-deployment-77f4cb64c4-hd557   1/1       Running       0          27s
greeting-deployment-77f4cb64c4-j4sf8   1/1       Running       0          26s
hello-minikube-5bc754d4cd-lsfjr        1/1       Running       3          5d
```

## Check if it's rollout really

```bash
$ curl http://192.168.99.100:31865/greeting\?name\=Mikita
{"id":1,"content":"Hello, Mikita"}%
```

## To change revision number

```bash
$ kubectl edit deployment greeting-deployment
deployment "greeting-deployment" edited
```

```yaml
spec:
    revisionHistoryLimit: 100
```

## Checkout history again

```bash
$ kubectl rollout history deployment greeting-deployment
deployments "greeting-deployment"
REVISION  CHANGE-CAUSE
2         none
3         none
```

## To rollout ro explicit revision it possible to

```bash
kubectl rollout undo deployment greeting-deployment --to-revision=3
```

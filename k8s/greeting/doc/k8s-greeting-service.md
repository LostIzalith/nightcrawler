# Kubernetes Services 

## Describe `deployment` object in yaml file

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
        app: greeting
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
$ kubectl create -f greeting-deployment.yaml
deployment "greeting-deployment" created
```

## Describe `service` in yaml file

```yaml
apiVersion: v1
kind: Service
metadata:
  name: greeting-service
spec:
    ports:
    - port: 30001
      nodePort: 30001
      targetPort: greeting-port
      protocol: TCP
    selector:
        app: greeting
    type: NodePort
```

## Create service with kubectl

```bash
$ kubectl create -f greeting-service.yaml
service "greeting-service" created
```

## Get service endpoint with minikube

```bash
$ minikube service greeting-service --url
http://192.168.99.100:30001
```

## Describe service

```bash
$ kubectl describe svc greeting-service
Name:                     greeting-service
Namespace:                default
Labels:                   <none>
Annotations:              <none>
Selector:                 app=greeting
Type:                     NodePort
IP:                       10.0.0.146
Port:                     <unset>  30001/TCP
TargetPort:               greeting-port/TCP
NodePort:                 <unset>  30001/TCP
Endpoints:                172.17.0.6:8080,172.17.0.7:8080,172.17.0.8:8080
Session Affinity:         None
External Traffic Policy:  Cluster
Events:                   <none>
```

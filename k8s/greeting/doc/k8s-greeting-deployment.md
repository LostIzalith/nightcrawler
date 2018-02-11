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

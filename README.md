# springboot_starter


curl -i  -d "username=user&password=test"  http://localhost:8080/login
curl -i  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaXNzIjoiYXV0aC1zZXJ2aWNlLWp3dCIsImlhdCI6MTUzMzg5MzI3NiwiZXhwIjoxNTM0NDk4MDc2LCJyb2xlcyI6IlJPTEVfVVNFUiJ9.wnIsKi4uEqohe7XZBT71H_uMXXa9Y0eLc69CMG_PLDQ"  http://localhost:8080/api/ping


## CORS

Cors are supported, a simple test is shown with ajax into static/index.html

```
cd static && python -m SimpleHTTPServer
```

Then browse to [http://localhost:8000](http://localhost:8000)
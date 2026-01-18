# docker run -d -p 8081:8080 --name sidecar halo-sidecar:v1
docker run -d --network host --name sidecar halo-sidecar:v1
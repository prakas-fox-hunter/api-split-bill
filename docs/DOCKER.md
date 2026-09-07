# Dokumentasi Docker

Dockerfile menggunakan multi-stage build. Stage `builder` menggunakan JDK 21 Alpine dan Maven Wrapper untuk membangun aplikasi. Stage `runtime` menggunakan JRE 21 Alpine sehingga image akhir tidak membawa source code maupun tool build.

## Build image

Jalankan dari root repository:

```bash
docker build -t split-bill-api .
```

## Menjalankan container

```bash
docker run --name split-bill-api -p 4110:4110 split-bill-api
```

Aplikasi dapat diakses melalui `http://localhost:4110`, sedangkan Swagger tersedia di `http://localhost:4110/swagger-ui.html`.

## Koneksi database

Container membutuhkan PostgreSQL yang dapat diakses dari network container. Untuk deployment, datasource dan JWT secret sebaiknya diberikan melalui konfigurasi eksternal atau environment variable.

Contoh penggunaan network Docker:

```bash
docker network create split-bill-network
docker run -d --name postgres --network split-bill-network -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=postgres postgres:16-alpine
docker run --rm --name split-bill-api --network split-bill-network -p 4110:4110 split-bill-api
```

Container langsung menjalankan `app.jar` melalui `ENTRYPOINT`; tidak diperlukan langkah manual saat startup.

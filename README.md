# Split Bill API

REST API Spring Boot untuk membuat group patungan, mencatat pengeluaran, dan menghitung siapa membayar kepada siapa. Login, register, dan JWT authentication tetap tersedia.

## Menjalankan

Prasyarat: Java 17+, Maven, dan PostgreSQL. Atur `spring.datasource.*` serta `jwt.secret` melalui environment/configuration.

```bash
./mvnw spring-boot:run
./mvnw test
docker build -t split-bill-api .
```

Port default adalah `8081`; Swagger ada di `/swagger-ui.html`.

## Contoh curl

```bash
curl -X POST http://localhost:8081/api/auth/register -H 'Content-Type: application/json' -d '{"username":"alice","password":"secret123"}'
curl -X POST http://localhost:8081/api/auth/login -H 'Content-Type: application/json' -d '{"username":"alice","password":"secret123"}'
curl -X POST http://localhost:8081/api/groups -H 'Authorization: Bearer TOKEN' -H 'Content-Type: application/json' -d '{"name":"Trip Bali","participants":["alice","bob","carol"]}'
curl -X POST http://localhost:8081/api/groups/1/expenses -H 'Authorization: Bearer TOKEN' -H 'Content-Type: application/json' -d '{"description":"Dinner","amount":90000,"paidBy":"alice","beneficiaries":["alice","bob","carol"]}'
curl http://localhost:8081/api/groups/1/settlement -H 'Authorization: Bearer TOKEN'
```

Detail API ada di [docs/API.md](docs/API.md).

## Personalisasi

GitHub username: `prakosadwiprasetya`. Jumlah Unicode karakter adalah `1950`, sehingga `service_charge_pct = 1950 % 10 = 0%`. Nilai ini dihitung di service settlement dan `service_charge_amount` dihitung dari total expense.

## Keputusan desain tersulit

Keputusan tersulit adalah memilih model peserta sebagai nama string, bukan akun aplikasi. Trade-off-nya adalah group dapat mengikutsertakan orang yang belum memiliki akun dan alurnya sederhana, tetapi identitas peserta belum terkait langsung dengan user JWT. Settlement menggunakan saldo bersih dan greedy matching sehingga jumlah transfer biasanya minimal, dengan trade-off pembulatan dua desimal.

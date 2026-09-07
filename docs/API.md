# Split Bill API

Semua endpoint selain autentikasi membutuhkan header `Authorization: Bearer <token>`.

1. `POST /api/auth/register` dengan `{ "username": "alice", "password": "secret123" }`.
2. `POST /api/auth/login` dengan payload yang sama dan ambil `token`.
3. Buat group: `POST /api/groups` dengan `{ "name": "Trip Bali", "participants": ["alice", "bob", "carol"] }`.
4. Tambahkan expense: `POST /api/groups/{id}/expenses` dengan `{ "description": "Dinner", "amount": 90000, "paidBy": "alice", "beneficiaries": ["alice", "bob", "carol"] }`.
5. Ambil settlement: `GET /api/groups/{id}/settlement`.

Peserta yang membayar dan menerima beban harus terdaftar pada group. Settlement memakai saldo bersih dan greedy matching untuk mengurangi jumlah transfer. Nilai uang diproses dengan `BigDecimal` dan dibulatkan dua angka desimal. Respons berisi `total_expenses`, `service_charge_pct`, `service_charge_amount`, serta `settlements` (`from`, `to`, `amount`). Username personalisasi `prakosadwiprasetya` memiliki jumlah Unicode 1950, sehingga service charge 0%.

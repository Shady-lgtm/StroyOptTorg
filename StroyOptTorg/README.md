# StroyOptTorg — backend (Spring Boot 3 + JWT + PostgreSQL)

Qurilish mollari internet-do'koni uchun REST API. Dizayn (Figma) sahifalariga mos endpointlar.

## Ishga tushirish

1. PostgreSQL: `docker compose up -d` (yoki o'zingizdagi Postgres; sozlamalar `application.yml` da).
2. `mvn spring-boot:run` yoki IntelliJ da `StroyOptTorgApplication` ni Run qiling (JDK 17+).
3. Swagger UI: http://localhost:8080/swagger-ui.html
4. Birinchi ishga tushishda **admin** yaratiladi: login `admin`. Parol `ADMIN_PASSWORD` muhit o'zgaruvchisi bilan
   beriladi; berilmasa konsol logida vaqtinchalik parol chiqadi. Demo katalog ham yuklanadi
   (o'chirish: `SEED_DEMO_DATA=false`).

### Muhit o'zgaruvchilari
`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET` (productionda **majburan almashtiring**), `JWT_EXPIRATION_MS`,
`ADMIN_USERNAME`, `ADMIN_PASSWORD`, `CORS_ORIGINS`, `SEED_DEMO_DATA`, `DDL_AUTO`, `PORT`.

## Endpointlar (sahifalar bo'yicha)

| Sahifa | Endpoint |
|---|---|
| Авторизация / Регистрация | `POST /api/auth/signin`, `POST /api/auth/signup` (login: username yoki email) |
| Главная | `GET /api/home` |
| Каталог (фильтр, сортировка, поиск) | `GET /api/categories`, `GET /api/brands`, `GET /api/products?category=&brand=&minPrice=&maxPrice=&q=&inStock=&onSale=&sort=&page=&size=` |
| Поиск (подсказки) | `GET /api/products/suggest?q=` |
| Карточка товара | `GET /api/products/{idOrSlug}`, `/{id}/related`, `/{id}/reviews` (GET, POST) |
| Избранное | `GET /api/favorites`, `GET /api/favorites/ids`, `POST/DELETE /api/favorites/{productId}` |
| Сравнение (макс. 4) | `GET /api/compare`, `POST/DELETE /api/compare/{productId}`, `DELETE /api/compare` |
| Корзина | `GET/DELETE /api/cart`, `POST /api/cart/items`, `PUT/DELETE /api/cart/items/{productId}` |
| Оформление заказа / Спасибо | `POST /api/orders` (javobda buyurtma raqami va tarkibi) |
| Купить в 1 клик | `POST /api/orders/one-click` (login shart emas) |
| Личный кабинет | `GET/PUT /api/users/me`, `PUT /api/users/me/password`, `GET /api/orders`, `GET /api/orders/{id}` |
| Акции | `GET /api/promotions`, `/api/promotions/{slug}` |
| Блог | `GET /api/blog`, `GET /api/blog/{slug}` |
| О компании, Доставка, Оплата, Возврат, Контакты, Политика | `GET /api/pages/{slug}` (`about`, `delivery`, `payment`, `returns`, `contacts`, `privacy`) |
| Обратный звонок / Контакты (форма) | `POST /api/callback` |

Himoyalangan endpointlarga `Authorization: Bearer <token>` header kerak.

### Admin (`ROLE_ADMIN`)
`/api/admin/products|categories|brands` (CRUD; mahsulot "o'chirish" = faolsizlantirish),
`/api/admin/orders` (+ `PATCH /{id}/status`: NEW, CONFIRMED, SHIPPED, DELIVERED, CANCELLED;
bekor qilinsa ombor qoldig'i qaytariladi), `/api/admin/blog|promotions|pages` (CRUD),
`/api/admin/callbacks`.

## Eslatmalar
- Ombor qoldig'i buyurtma paytida pessimistic lock bilan kamaytiriladi (bir vaqtda ikki buyurtma oshirib yubormaydi).
- Signupda rol doim `ROLE_USER` (oldingi kodda foydalanuvchi o'zi `ROLE_ADMIN` yuborib admin bo'la olardi).
- Xato javoblari: `{status, message, fieldErrors, timestamp}`.
- Rasmlar hozircha URL ro'yxati (`images`) sifatida saqlanadi; fayl yuklash keyingi bosqich.

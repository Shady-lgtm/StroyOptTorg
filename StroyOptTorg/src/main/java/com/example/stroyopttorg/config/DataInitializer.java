package com.example.stroyopttorg.config;

import com.example.stroyopttorg.model.*;
import com.example.stroyopttorg.repository.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Birinchi ishga tushishda admin va demo ma'lumotlarni yaratadi. */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final StaticPageRepository staticPageRepository;
    private final BlogPostRepository blogPostRepository;
    private final PromotionRepository promotionRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password:}")
    private String adminPassword;

    @Value("${app.seed-demo-data:true}")
    private boolean seedDemoData;

    @Override
    public void run(ApplicationArguments args) {
        ensureAdmin();
        ensureStaticPages();
        if (seedDemoData && categoryRepository.count() == 0) {
            seedCatalog();
        }
    }

    private void ensureAdmin() {
        if (userRepository.existsByUsername(adminUsername)) {
            return;
        }
        String pwd = adminPassword;
        boolean generated = false;
        if (pwd == null || pwd.isBlank()) {
            pwd = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            generated = true;
        }
        userRepository.save(User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(pwd))
                .fullName("Administrator")
                .role("ROLE_ADMIN")
                .build());
        if (generated) {
            log.warn("ADMIN yaratildi. login='{}', vaqtinchalik parol='{}' (ADMIN_PASSWORD orqali o'zgartiring)",
                    adminUsername, pwd);
        } else {
            log.info("ADMIN yaratildi: '{}'", adminUsername);
        }
    }

    private void ensureStaticPages() {
        Map<String, String> pages = new LinkedHashMap<>();
        pages.put("about", "О компании");
        pages.put("delivery", "Доставка");
        pages.put("payment", "Оплата");
        pages.put("returns", "Возврат и обмен");
        pages.put("contacts", "Контакты");
        pages.put("privacy", "Политика конфиденциальности");
        pages.forEach((slug, title) -> {
            if (staticPageRepository.findBySlug(slug).isEmpty()) {
                StaticPage p = new StaticPage();
                p.setSlug(slug);
                p.setTitle(title);
                p.setContent("Содержимое страницы будет добавлено администратором.");
                staticPageRepository.save(p);
            }
        });
    }

    private Category cat(String name, int order, Category parent) {
        Category c = new Category();
        c.setName(name);
        c.setSortOrder(order);
        c.setParent(parent);
        return categoryRepository.save(c);
    }

    private Brand brand(String name) {
        Brand b = new Brand();
        b.setName(name);
        return brandRepository.save(b);
    }

    private void product(String sku, String name, String desc, String price, String oldPrice, int stock,
                         Category category, Brand brand, boolean hit, boolean isNew, Map<String, String> specs) {
        Product p = new Product();
        p.setSku(sku);
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(new BigDecimal(price));
        p.setOldPrice(oldPrice == null ? null : new BigDecimal(oldPrice));
        p.setStock(stock);
        p.setCategory(category);
        p.setBrand(brand);
        p.setHit(hit);
        p.setNewArrival(isNew);
        p.getSpecifications().putAll(specs);
        productRepository.save(p);
    }

    private void seedCatalog() {
        Category power = cat("Электроинструмент", 1, null);
        Category hand = cat("Ручной инструмент", 2, null);
        cat("Измерительный инструмент", 3, null);
        cat("Крепёж", 4, null);
        cat("Спецодежда и СИЗ", 5, null);
        Category drills = cat("Дрели и шуруповёрты", 1, power);
        Category hammers = cat("Перфораторы", 2, power);
        cat("Шлифовальные машины", 3, power);
        Category sets = cat("Наборы инструментов", 1, hand);

        Brand makita = brand("Makita");
        Brand bosch = brand("Bosch");
        Brand dewalt = brand("DeWalt");
        Brand stanley = brand("Stanley");

        Map<String, String> s1 = new LinkedHashMap<>();
        s1.put("Напряжение", "18 В");
        s1.put("Тип аккумулятора", "Li-Ion");
        s1.put("Макс. крутящий момент", "60 Н·м");
        product("DEMO-0001", "Дрель-шуруповёрт аккумуляторная 18 В", "Демо-товар для проверки каталога.",
                "8990.00", "10990.00", 25, drills, makita, true, false, s1);

        Map<String, String> s2 = new LinkedHashMap<>();
        s2.put("Мощность", "750 Вт");
        s2.put("Энергия удара", "2.7 Дж");
        s2.put("Тип патрона", "SDS-plus");
        product("DEMO-0002", "Перфоратор SDS-plus 750 Вт", "Демо-товар для проверки каталога.",
                "7490.00", null, 12, hammers, bosch, true, true, s2);

        Map<String, String> s3 = new LinkedHashMap<>();
        s3.put("Напряжение", "20 В");
        s3.put("Тип аккумулятора", "Li-Ion");
        s3.put("Макс. крутящий момент", "70 Н·м");
        product("DEMO-0003", "Дрель-шуруповёрт бесщёточная 20 В", "Демо-товар для проверки каталога.",
                "11990.00", "13500.00", 8, drills, dewalt, false, true, s3);

        Map<String, String> s4 = new LinkedHashMap<>();
        s4.put("Количество предметов", "108");
        s4.put("Материал", "Хром-ванадий");
        product("DEMO-0004", "Набор инструментов 108 предметов", "Демо-товар для проверки каталога.",
                "4590.00", null, 40, sets, stanley, false, false, s4);

        BlogPost post = new BlogPost();
        post.setTitle("Как выбрать шуруповёрт для ремонта");
        post.setExcerpt("Коротко о том, на что смотреть при выборе: напряжение, момент, тип аккумулятора.");
        post.setContent("Демо-статья. Замените текст через админ-панель или API /api/admin/blog.");
        blogPostRepository.save(post);

        Promotion promo = new Promotion();
        promo.setTitle("Скидки на электроинструмент");
        promo.setDescription("Демо-акция. Управляется через /api/admin/promotions.");
        promo.setStartDate(LocalDate.now());
        promo.setEndDate(LocalDate.now().plusMonths(1));
        promotionRepository.save(promo);

        log.info("Demo katalog yaratildi ({} ta mahsulot).", productRepository.count());
    }
}

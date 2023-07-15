package com.example.gccoffee.repository;

import com.example.gccoffee.model.Category;
import com.example.gccoffee.model.Product;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductJdbcRepositoryTest {

  static EmbeddedMysql embeddedMysql;

  @BeforeAll
  static void setUp() {
    MysqldConfig config = aMysqldConfig(v8_0_11)
            .withCharset(Charset.UTF8)
            .withPort(2215)
            .withUser("test", "test1234!")
            .withTimeZone("Asia/Seoul")
            .build();
    embeddedMysql = anEmbeddedMysql(config)
            .addSchema("test-order_mgmt", classPathScripts("schema.sql"))
            .start();
  }

  @AfterAll
  static void cleanUp() {
    embeddedMysql.stop();
  }

  @Autowired
  ProductRepository repository;
  private static final Product newProduct = new Product(UUID.randomUUID(), "new-product",
          Category.COFFEE_BEAN_PACKAGE, 1000L);

  @Test
  @Order(1)
  @DisplayName("상품을 추가할 수 있다.")
  void testInsert() {
    repository.insert(newProduct);
    List<Product> all = repository.findAll();
    assertThat(all.isEmpty(), is(false));
  }

  @Test
  @Order(2)
  @DisplayName("상품을 이름으로 조회할 수 있다.")
  void testFindByName() {
    Optional<Product> product = repository.findByName(newProduct.getProductName());
    assertThat(product.isEmpty(), is(false));
  }

  @Test
  @Order(3)
  @DisplayName("상품을 아이디로 조회할 수 있다.")
  void testFindById() {
    Optional<Product> product = repository.findById(newProduct.getProductId());
    assertThat(product.isEmpty(), is(false));
  }

  @Test
  @Order(4)
  @DisplayName("상품을 카테고리로 조회할 수 있다.")
  void testFindByCategory() {
    List<Product> products = repository.findByCategory(Category.COFFEE_BEAN_PACKAGE);
    assertThat(products.isEmpty(), is(false));
  }

}
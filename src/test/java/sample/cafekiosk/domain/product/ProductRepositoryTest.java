package sample.cafekiosk.domain.product;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafekiosk.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.domain.product.ProductType.*;

@ActiveProfiles("test")
@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매 상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        //given
        Product product1 = createProduct(SELLING, HANDMADE, "아메리카노", 4000, "001");
        Product product2 = createProduct(HOLD, HANDMADE, "카페라떼", 4500, "002");
        Product product3 = createProduct(STOP_SELLING, HANDMADE, "팥빙수", 7000, "003");
        productRepository.saveAll(List.of(product1, product2, product3));

        //when
        List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        //then
        Assertions.assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @DisplayName("원하는 상품 번호를 가진 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        //given
        Product product1 = createProduct(SELLING, HANDMADE, "아메리카노", 4000, "001");
        Product product2 = createProduct(HOLD, HANDMADE, "카페라떼", 4500, "002");
        Product product3 = createProduct(STOP_SELLING, HANDMADE, "팥빙수", 7000, "003");
        productRepository.saveAll(List.of(product1, product2, product3));

        //when
        List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        //then
        Assertions.assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품 번호를 조회한다.")
    @Test
    void findLatestProductNumber() {
        //given
        String targetProductNumber = "003";
        Product product1 = createProduct(SELLING, HANDMADE, "아메리카노", 4000, "001");
        Product product2 = createProduct(HOLD, HANDMADE, "카페라떼", 4500, "002");
        Product product3 = createProduct(STOP_SELLING, HANDMADE, "팥빙수", 7000, targetProductNumber);
        productRepository.saveAll(List.of(product1, product2, product3));

        //when
        String latestProductNumber = productRepository.findLatestProductNumber();

        //then
        Assertions.assertThat(latestProductNumber).isEqualTo(targetProductNumber);
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품 번호를 조회할 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
    @Test
    void findLatestProductNumberWhenProductIsEmpty() {
        //given
        //when
        String latestProductNumber = productRepository.findLatestProductNumber();

        //then
        Assertions.assertThat(latestProductNumber).isNull();
    }

    private Product createProduct(ProductSellingStatus sellingStatus, ProductType type, String name, int price, String productNumber) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }

}
package sample.cafekiosk.domain.stock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StockTest {

    @DisplayName("재고의 수령이 제공된 수량보다 작은지 확인한다.")
    @Test
    void isQuantityLessThan() {
        //given
        Stock stock = Stock.create("001", 1);
        int givenQuantity = 2;

        //when
        boolean result = stock.isQuantityLessThan(givenQuantity);

        //then
        assertThat(result).isTrue();
    }

    @DisplayName("주어진 개수만큼 재고를 차감할 수 있다.")
    @Test
    void deductQuantity() {
        //given
        Stock stock = Stock.create("001", 1);
        int givenQuantity = 1;

        //when
        stock.deductQuantity(givenQuantity);

        //then
        assertThat(stock.getQuantity()).isZero();
    }

    @DisplayName("재고보다 많은 수의 수량으로 차감하는 경우 예외가 발생한다.")
    @Test
    void deductQuantity2() {
        //given
        Stock stock = Stock.create("001", 1);
        int givenQuantity = 2;

        //when //then
        assertThatThrownBy(() -> stock.deductQuantity(givenQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("차감할 재고 수량이 없습니다.");
    }

}
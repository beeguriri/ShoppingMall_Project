package study.shop.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.shop.entity.Item;
import study.shop.entity.constant.ItemSellStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    public void createItemTest() throws Exception {
        //given
        Item item = createItem();
        itemRepository.save(item);

        //when
        Item savedItem = itemRepository.findById(item.getId()).get();

        //then
        assertThat(savedItem.equals(item));
        System.out.println(item);
        System.out.println(item.getCreatedAt());
        System.out.println(item.getUpdatedAt());
        System.out.println(item.getItemSellStatus());

    }

    @Test
    public void 재고수량증가() throws Exception {
        //given
        Item item = createItem();
        itemRepository.save(item);

        //when
        item.addStock(10);

        //then
        assertThat(item.getStock()==20);

    }

    @Test
    public void 재고수량감소() throws Exception {
        //given
        Item item = createItem();
        itemRepository.save(item);

        //when
        item.removeStock(10);

        //then
        assertThat(item.getStock()==0);

    }

    @Test
    public void 재고수량0이하() throws Exception {
        //given
        Item item = createItem();
        itemRepository.save(item);

        //when

        //then
        assertThrows(RuntimeException.class, () -> {
            item.removeStock(15);
        });

    }

    private static Item createItem() {
        return new Item("ItemA", 1000, 10, "상세설명", ItemSellStatus.SELL);
    }


}
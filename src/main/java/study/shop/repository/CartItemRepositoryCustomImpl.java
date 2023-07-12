package study.shop.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import study.shop.dto.CartDetailDto;
import study.shop.dto.QCartDetailDto;

import javax.persistence.EntityManager;
import java.util.List;

import static study.shop.entity.QCartItem.cartItem;
import static study.shop.entity.QItem.item;
import static study.shop.entity.QItemImg.itemImg;

@RequiredArgsConstructor
public class CartItemRepositoryCustomImpl implements CartItemRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Autowired
    public CartItemRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CartDetailDto> findCartDetailDtoList(Long cartId) {
        return queryFactory
                    .select(
                            new QCartDetailDto(
                                    cartItem.id, item.itemName,
                                    item.price, cartItem.count,
                                    itemImg.imgUrl
                            )
                    )
                    .from(item)
                    .join(cartItem).fetchJoin().on(item.id.eq(cartItem.item.id))
                    .join(itemImg).fetchJoin().on(item.id.eq(itemImg.item.id))
                    .where(
                            cartItem.item.id.eq(itemImg.item.id),
                            cartItem.cart.id.eq(cartId),
                            itemImg.repImgYn.eq("Y")
                    )
                    .orderBy(cartItem.createdAt.desc())
                    .fetch();
    }

    @Override
    public JPAQuery<Long> countQuery(Long cartId) {
        return queryFactory
                    .select(item.count())
                    .from(item)
                    .join(cartItem).fetchJoin().on(item.id.eq(cartItem.item.id))
                    .join(itemImg).fetchJoin().on(item.id.eq(itemImg.item.id))
                    .where(
                            cartItem.item.id.eq(itemImg.item.id),
                            cartItem.cart.id.eq(cartId),
                            itemImg.repImgYn.eq("Y")
                    );
    }
}

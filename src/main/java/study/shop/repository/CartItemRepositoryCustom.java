package study.shop.repository;

import com.querydsl.jpa.impl.JPAQuery;
import study.shop.dto.CartDetailDto;

import java.util.List;

public interface CartItemRepositoryCustom {

    List<CartDetailDto> findCartDetailDtoList(Long cartId);

    JPAQuery<Long> countQuery(Long cartId);

}

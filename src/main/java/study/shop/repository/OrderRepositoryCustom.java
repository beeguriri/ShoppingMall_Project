package study.shop.repository;

import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;
import study.shop.entity.Order;

import java.util.List;

public interface OrderRepositoryCustom {

    List<Order> findOrders(String userid, Pageable pageable);

    JPAQuery<Long> countQuery(String userid);
}

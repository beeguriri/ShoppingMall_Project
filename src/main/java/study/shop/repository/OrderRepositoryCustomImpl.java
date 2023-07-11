package study.shop.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import study.shop.entity.Order;

import javax.persistence.EntityManager;
import java.util.List;

import static study.shop.entity.QOrder.order;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Autowired
    public OrderRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Order> findOrders(String userid, Pageable pageable) {
        return queryFactory
                .selectFrom(order)
                .where(order.member.userid.eq(userid))
                .orderBy(order.OrderDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public JPAQuery<Long> countQuery(String userid) {

        return queryFactory
                .select(order.count())
                .from(order)
                .where(order.member.userid.eq(userid));

//        return queryFactory
//                .select(order.count())
//                .from(order)
//                .where(order.member.userid.eq(userid))
//                .fetchOne();
    }
}

package study.shop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.thymeleaf.util.StringUtils;
import study.shop.dto.ItemSearchDto;
import study.shop.entity.Item;
import study.shop.entity.constant.ItemSellStatus;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static study.shop.entity.QItem.item;

@RequiredArgsConstructor
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        List<Item> content = queryFactory.selectFrom(item)
                .where(
                        regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())
                )
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(item.count())
                .from(item)
                .where(
                        regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression regDtsAfter(String searchDateType) {

        LocalDateTime dateTime = LocalDateTime.now();

        //all, 1d, 1w, 1m, 6m
        if(StringUtils.equals("all", searchDateType) || searchDateType == null)
            return null;
        else if(StringUtils.equals("1d", searchDateType))
            dateTime = dateTime.minusDays(1);
        else if(StringUtils.equals("1w", searchDateType))
            dateTime = dateTime.minusWeeks(1);
        else if(StringUtils.equals("1m", searchDateType))
            dateTime = dateTime.minusMonths(1);
        else if(StringUtils.equals("6m", searchDateType))
            dateTime = dateTime.minusMonths(6);

        return item.createdAt.after(dateTime);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus==null ? null : item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        if(StringUtils.equals("itemName", searchBy))
            return item.itemName.like("%"+searchQuery+"%");
        else if(StringUtils.equals("createdBy", searchBy))
            return item.createdBy.like("%"+searchQuery+"%");

        return null;
    }
}

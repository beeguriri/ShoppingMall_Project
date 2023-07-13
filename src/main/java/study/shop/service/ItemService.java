package study.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.shop.dto.ItemFormDto;
import study.shop.dto.ItemSearchDto;
import study.shop.dto.MainItemDto;
import study.shop.dto.ManageItemDto;
import study.shop.entity.Item;
import study.shop.entity.ItemComment;
import study.shop.entity.ItemImg;
import study.shop.repository.ItemCommentRepository;
import study.shop.repository.ItemImgRepository;
import study.shop.repository.ItemRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;
    private final ItemCommentRepository itemCommentRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> multipartFileList) throws Exception {

        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        for(int i=0; i<multipartFileList.size(); i++) {
            ItemImg itemImg = new ItemImg(item);

            if(i==0)
                itemImg.setRepImgYn("Y");
            else
                itemImg.setRepImgYn("N");

            itemImgService.saveItemImg(itemImg, multipartFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDetail(Long itemId) {

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemComment> itemCommentList = itemCommentRepository.findByItemIdOrderByUpdatedAtDesc(itemId);

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        ItemFormDto itemFormDto = ItemFormDto.of(item);

        itemFormDto.setItemImgsList(itemImgList);
        itemFormDto.setItemCommentList(itemCommentList);

        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> multipartFileList) throws Exception{

        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        for(int i=0; i<multipartFileList.size(); i++)
            itemImgService.updateItemImg(itemImgIds.get(i), multipartFileList.get(i));

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<ManageItemDto> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}

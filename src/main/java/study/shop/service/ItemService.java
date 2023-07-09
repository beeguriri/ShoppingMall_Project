package study.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import study.shop.dto.ItemFormDto;
import study.shop.entity.Item;
import study.shop.entity.ItemImg;
import study.shop.repository.ItemImgRepository;
import study.shop.repository.ItemRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> multipartFileList) throws Exception {

        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        for(int i=0; i<multipartFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i==0)
                itemImg.setRepImgYn("Y");
            else
                itemImg.setRepImgYn("N");

            itemImgService.saveItemImg(itemImg, multipartFileList.get(i));
        }

        return item.getId();
    }

}

package study.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import study.shop.entity.ItemImg;
import study.shop.repository.ItemImgRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    @Value("${custom.path.itemImgLocation}")
    private String itemImgLocation;

    public void saveItemImg(ItemImg itemImg, MultipartFile multipartFile) throws Exception {

        String oriImgName = multipartFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if(StringUtils.hasText(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, multipartFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }
        
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);

    }
}

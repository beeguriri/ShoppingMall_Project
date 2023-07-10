package study.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import study.shop.entity.ItemImg;
import study.shop.repository.ItemImgRepository;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    @Value("${custom.path.itemImgLocation}")
    private String itemImgLocation;

    public void saveItemImg(ItemImg itemImg, MultipartFile multipartFile) throws Exception {

        String oriImgName = getOriImgName(multipartFile);
        String imgName = "";
        String imgUrl = "";

        if(StringUtils.hasText(oriImgName)){
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, multipartFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }
        
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);

    }

    public void updateItemImg(Long itemImgId, MultipartFile multipartFile) throws Exception {

        if(!multipartFile.isEmpty()) {
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(StringUtils.hasText(savedItemImg.getImgName()))
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName());

            String oriImgName = getOriImgName(multipartFile);
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, multipartFile.getBytes());
            String imgUrl = "/images/item/" + imgName;

            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);

        }
    }

    private static String getOriImgName(MultipartFile multipartFile) {
        return multipartFile.getOriginalFilename();
    }
}

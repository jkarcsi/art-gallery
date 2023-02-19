package jkarcsi.controller;

import static jkarcsi.utils.constants.GalleryMessages.ARTWORK_NOT_FOUND;
import static jkarcsi.utils.constants.GalleryMessages.BUY;
import static jkarcsi.utils.constants.GalleryMessages.PAGES_EXCEEDED;
import static jkarcsi.utils.constants.GalleryMessages.PAGINATED;
import static jkarcsi.utils.constants.GalleryMessages.RETRIEVE;
import static jkarcsi.utils.constants.GalleryMessages.WEBCLIENT_FORBIDDEN;
import static jkarcsi.utils.constants.GeneralConstants.ACCESS_BOTH;
import static jkarcsi.utils.constants.GeneralConstants.ACCESS_CLIENT;
import static jkarcsi.utils.constants.GeneralConstants.GALLERY_BASE_PATH;
import static jkarcsi.utils.constants.GeneralConstants.ID_PATH;
import static jkarcsi.utils.constants.UserMessages.ACCESS_DENIED;
import static jkarcsi.utils.constants.UserMessages.NOT_EXIST;
import static jkarcsi.utils.constants.UserMessages.TOKEN_ERROR;
import static jkarcsi.utils.constants.UserMessages.USER_ERROR;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import jkarcsi.dto.gallery.Artwork;
import jkarcsi.service.GalleryService;
import jkarcsi.utils.helpers.IncludeSwaggerDocumentation;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(GALLERY_BASE_PATH)
@Api(tags = "gallery")
@RequiredArgsConstructor
@IncludeSwaggerDocumentation
@Validated
public class GalleryController {


    private final GalleryService galleryService;

    @GetMapping(ID_PATH)
    @PreAuthorize(ACCESS_BOTH)
    @ApiOperation(value = RETRIEVE)
    @ApiResponses(value = { @ApiResponse(code = 400, message = ARTWORK_NOT_FOUND),
            @ApiResponse(code = 403, message = ACCESS_DENIED), @ApiResponse(code = 500, message = TOKEN_ERROR) })
    public ResponseEntity<Artwork> retrieveArtwork(@PathVariable final String id) {
        return ResponseEntity.ok(galleryService.retrieveArtworkById(id));
    }

    @GetMapping(params = { "page", "limit" })
    @PreAuthorize(ACCESS_BOTH)
    @ApiOperation(value = PAGINATED)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = PAGES_EXCEEDED), @ApiResponse(code = 403, message = WEBCLIENT_FORBIDDEN),
            @ApiResponse(code = 500, message = TOKEN_ERROR) })
    public ResponseEntity<Page<Artwork>> retrievePaginatedArtworks(
            @RequestParam(value = "page", required = false, defaultValue = "1") @Range(min = 1,
                    max = 100) final Integer page,
            @RequestParam(value = "limit", required = false, defaultValue = "5") @Range(min = 1,
                    max = 10000) final Integer limit) {
        final Page<Artwork> resultPage = galleryService.findPaginatedArtworks(PageRequest.of(page, limit));
        final HttpHeaders resultHeader = galleryService.enrichPaginatedResult(resultPage.getTotalPages(), page, limit);
        return ResponseEntity.ok().headers(resultHeader).body(resultPage);
    }

    @PostMapping(ID_PATH)
    @PreAuthorize(ACCESS_CLIENT)
    @ApiOperation(value = BUY)
    @ApiResponses(
            value = { @ApiResponse(code = 400, message = USER_ERROR), @ApiResponse(code = 403, message = ACCESS_DENIED),
                    @ApiResponse(code = 404, message = NOT_EXIST), @ApiResponse(code = 500, message = TOKEN_ERROR) })
    public ResponseEntity<Void> purchaseArtwork(@PathVariable final String id) {
        return ResponseEntity.ok(galleryService.purchaseArtworkById(id));
    }

}

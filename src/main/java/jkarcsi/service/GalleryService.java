package jkarcsi.service;

import static jkarcsi.utils.constants.GalleryMessages.ARTWORK_SOLD;
import static jkarcsi.utils.constants.GalleryMessages.PAGES_EXCEEDED;
import static jkarcsi.utils.constants.GeneralConstants.PAGINATION_COUNT;
import static jkarcsi.utils.constants.GeneralConstants.PAGINATION_LIMIT;
import static jkarcsi.utils.constants.GeneralConstants.PAGINATION_PAGE;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import jkarcsi.dto.gallery.Artwork;
import jkarcsi.dto.gallery.ArtworkPage;
import jkarcsi.dto.gallery.Transaction;
import jkarcsi.repository.TransactionRepository;
import jkarcsi.utils.exception.CustomGalleryException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@Transactional
@RequiredArgsConstructor
public class GalleryService {

    private final ArtworkDownloaderService artworkDownloaderService;
    private final TransactionRepository transactionRepository;

    public Artwork retrieveArtworkById(String id) {
        return artworkDownloaderService.getArtwork(id);
    }

    public Page<Artwork> findPaginatedArtworks(@PageableDefault(page = 1, value = 10000) final Pageable pageable) {

        final ArtworkPage resultPage =
                artworkDownloaderService.getArtworksPaginated(pageable.getPageNumber(), pageable.getPageSize());
        final Page<Artwork> response =
                new PageImpl<>(resultPage.getArtworks(), PageRequest.of(resultPage.getPage(), resultPage.getPageSize()),
                        resultPage.getTotalRecords());

        if (pageable.getPageNumber() > response.getTotalPages()) {
            throw new CustomGalleryException(NOT_FOUND, PAGES_EXCEEDED);
        }

        return response;
    }

    @Transactional(propagation = Propagation.NESTED, isolation = Isolation.SERIALIZABLE, rollbackFor = SQLException.class)
    public Void purchaseArtworkById(final String id) {
        if (!transactionRepository.existsByArtwork(Integer.parseInt(id))
                && artworkDownloaderService.getArtwork(id).getId() != null) {
            final Transaction transactionToSave =
                    new Transaction().setArtwork(Integer.parseInt(id)).setUser(getCurrentUser());
            transactionRepository.save(transactionToSave);
            return null;
        } else {
            throw new CustomGalleryException(FORBIDDEN, ARTWORK_SOLD);
        }
    }

    public List<Artwork> listOwnedArtworks(final String username) {
        return transactionRepository.findAllByUser(username).stream()
                .map(transaction -> artworkDownloaderService.getArtwork(String.valueOf(transaction.getArtwork())))
                .collect(Collectors.toList());
    }

    public HttpHeaders enrichPaginatedResult(final Integer count, final Integer page, final Integer limit) {
        final HttpHeaders enriched = new HttpHeaders();
        enriched.add(PAGINATION_PAGE, String.valueOf(page));
        enriched.add(PAGINATION_COUNT, String.valueOf(count));
        enriched.add(PAGINATION_LIMIT, String.valueOf(limit));
        return enriched;
    }

    private String getCurrentUser() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
                .getRemoteUser();
    }

}

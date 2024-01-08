package com.openclassrooms.mddapi.service;


import com.openclassrooms.mddapi.model.Commentary;
import com.openclassrooms.mddapi.repository.CommentaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing commentary-related operations.
 */
@AllArgsConstructor
@Service
public class CommentaryService {

    private CommentaryRepository commentaryRepository;

    /**
     * Retrieves a list of commentaries associated with an article using its ID.
     *
     * @param id The ID of the article for which to retrieve commentaries.
     * @return List of commentaries associated with the article having the given ID.
     * @see CommentaryRepository#findAllByArticle_Id(Long)
     */
    public List<Commentary> getAllByArticle_Id(Long id) {
        return commentaryRepository.findAllByArticle_Id(id);
    }

    /**
     * Saves a new commentary or updates an existing one.
     *
     * @param commentary The commentary to be saved or updated.
     * @return The saved or updated commentary.
     * @see CommentaryRepository#save(Commentary)
     */
    public Commentary save(Commentary commentary) {
        return commentaryRepository.save(commentary);
    }

}

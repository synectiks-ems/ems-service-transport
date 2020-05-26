package com.synectiks.transport.service;

import com.synectiks.transport.service.dto.DocumentsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.synectiks.transport.domain.Documents}.
 */
public interface DocumentsService {

    /**
     * Save a documents.
     *
     * @param documentsDTO the entity to save.
     * @return the persisted entity.
     */
    DocumentsDTO save(DocumentsDTO documentsDTO);

    /**
     * Get all the documents.
     *
     * @return the list of entities.
     */
    List<DocumentsDTO> findAll();

    /**
     * Get the "id" documents.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DocumentsDTO> findOne(Long id);

    /**
     * Delete the "id" documents.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

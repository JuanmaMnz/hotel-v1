package com.hotel.identity_document.Repository;

import com.hotel.identity_document.Model.Id;
import com.hotel.identity_document.Model.IdentityDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdentityDocumentRepository extends JpaRepository<IdentityDocument, Id> {
}

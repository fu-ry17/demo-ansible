package com.turnkey.turnquest.gis.quotation.repository;

import com.turnkey.turnquest.gis.quotation.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment,Long> {

    List<Attachment> findAllByOrganizationId(long organizationId);

    Attachment findByIdAndOrganizationId(long id,long organizationId);


}

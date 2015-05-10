package com.irengine.campus.cas.extension.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.irengine.campus.cas.extension.domain.UploadedFile;

public interface UploadedFileRepository extends
		CrudRepository<UploadedFile, Long> {

	@Query("SELECT u FROM UploadedFile u WHERE u.id=:id")
	UploadedFile findByFileId(@Param("id") long id);

	@Query("SELECT u FROM UploadedFile u WHERE u.entityId=:id")
	List<UploadedFile> findByEntityId(@Param("id") long id);

	@Query("SELECT u FROM UploadedFile u WHERE u.entityType=:entityType")
	Set<UploadedFile> findByEntityType(@Param("entityType") String entityType);

	@Query("SELECT u FROM UploadedFile u WHERE u.entityType=:entityType AND u.entityId=:entityId")
	List<UploadedFile> findByEntityTypeAndEntityId(
			@Param("entityType") String entityType,
			@Param("entityId") long entityId);

	@Modifying
	@Query("DELETE FROM UploadedFile u WHERE u.entityType=:entityType AND u.entityId=:entityId")
	void deleteByTypeAndEntityTypeAndEntityId(
			@Param("entityType") String entityType,
			@Param("entityId") Long entityId);
	
}

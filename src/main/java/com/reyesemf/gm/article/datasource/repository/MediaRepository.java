package com.reyesemf.gm.article.datasource.repository;

import com.reyesemf.gm.article.domain.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

}

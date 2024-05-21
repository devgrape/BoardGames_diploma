package ru.project.BoardGames.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.BoardGames.Models.Collection;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List <Collection> findCollectionByUserId(Long userId);
    Collection findCollectionByName(String name);
}
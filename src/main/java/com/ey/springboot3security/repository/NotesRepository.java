package com.ey.springboot3security.repository;

import com.ey.springboot3security.entity.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotesRepository extends JpaRepository<Notes, Integer> {

}
package com.sustech.dbproj.repository;

import com.sustech.dbproj.domain.Train;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainRepository extends JpaRepository<Train,Integer> {

    Train findByName(String name);

}

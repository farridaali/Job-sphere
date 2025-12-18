package com.jobsphere.search;

import com.jobsphere.model.builder.Job;
import java.util.*;

/**
 * el strategy bt5lyny a3ml switch fel runtime le algo ely 3ayz a3ml by search
 *
 * (MSH STATE PATTERN 3SHAN MAFYSH 7AGA MO3TAMDA 3ALA 7AGA TANYA)
 *
 * Kol ely ben3mlo en e7na ben3ml kaza strategy n3ml search byha
 * we ben5aly el context class howa ely yetlob anhy strategy n-search byha
 * wel end result heya wa7da eny al2y el job posting ely ana 3ayzo
 *
 * we 8er keda msh benda5ly kol strategy bel tany
 * ya3ny msh b7ot el search bel keyword ma3 el search bel location maslan
 */
public interface SearchStrategy {
    List<Job> search(List<Job> jobs, String criteria);
}








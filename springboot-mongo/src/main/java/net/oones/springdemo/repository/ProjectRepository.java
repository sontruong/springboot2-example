/**
 * 
 */
package net.oones.springdemo.repository;

import net.oones.springdemo.domain.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Son.Truong
 *
 */
public interface ProjectRepository extends MongoRepository<Project, Long> {

    public Project findTop1ByName(String name);

}

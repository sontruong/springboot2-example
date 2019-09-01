/**
 * 
 */
package net.oones.springdemo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.oones.springdemo.domain.Lang;

/**
 * @author Son.Truong
 *
 */
public interface LangRepository extends MongoRepository<Lang, Long> {

    public Lang findTop1ByKey(String name);

}
